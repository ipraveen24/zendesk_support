package com.streamsets.gdprPurger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.zendesk.client.v2.model.Organization;
import org.zendesk.client.v2.model.Ticket;

import com.streamsets.datapurger.util.CSVBuilder;
import com.streamsets.datapurger.util.CSVParser;
import com.streamsets.datapurger.util.CustomUtils;
import com.streamsets.datapurger.zendesk.ZendeskAPI;
import com.streamsets.sqlrepo.persistence.ZenDeskRepository;

public class ZendeskDataPurger
{
	private static final Logger log = LoggerFactory.getLogger(ZendeskDataPurger.class);

	private List<String> header = Arrays.asList("ticketID", "organization name", "orgnization id", "deleted from zendesk", "status", "error");

	private ZenDeskRepository zenDeskRepository;

	private CSVBuilder builder;

	private List<Organization> orgnizations;

	private ZendeskAPI zdAPI;

	private int queryDifference;

	private boolean isDeleteIssues;

	public ZendeskDataPurger(FileSystemXmlApplicationContext context, ZendeskAPI zdAPI, int queryDifference, boolean isDeleteIssues) throws IOException
	{
		orgnizations = new ArrayList<>();
		this.zdAPI = zdAPI;
		for (Organization organization : zdAPI.getAllOrganizations())
			orgnizations.add(organization);
		this.zenDeskRepository = (ZenDeskRepository) context.getBean("zenDeskRepository");
		this.queryDifference = queryDifference;
		this.isDeleteIssues = isDeleteIssues;
	}

	public void process(BufferedReader br) throws Exception
	{
		while (true)
		{
			try
			{
				switch (displayOptions(br)) {
					case 1:
						DownloadOrgData downloadOrgData = new DownloadOrgData();
						downloadOrgData.createOrgCSV(orgnizations, log);
						break;
					case 2:
						processCSVOption(br);
						break;
					case 3:
						processSingleOrgnization(br);
						break;
					case 99:
						return;
					default:
						break;
				}
			} catch (Exception e)
			{
				log.error("error: " + e.getMessage(), e);
			} finally
			{
				if (builder != null)
					builder.close();
			}
		}
	}

	private void processSingleOrgnization(BufferedReader br) throws IOException
	{
		log.info("Enter the name of Zendesk Orgnization for migration ");
		String orgName = br.readLine();
		List<UserOrg> userOrgs = this.getOrganization(orgName);
		if (userOrgs.size() == 0)
		{
			log.info(orgName + ": organization not found, Please try again");
			return;
		}
		if (userOrgs.size() > 1)
		{
			int i = 0;
			for (; i < userOrgs.size(); i++)
				log.info(i + ". " + userOrgs.get(i).getOrgName());
			log.info("Multiple organizations found, please select the orgnization: [0-" + --i + "]");
			i = Integer.parseInt(br.readLine());
			if (i < userOrgs.size() && i > -1)
			{
				UserOrg userOrg = userOrgs.get(i);
				userOrgs = new ArrayList<>();
				userOrgs.add(userOrg);
			}
			else
			{
				log.info("Range not found, please try again");
				return;
			}
		}
		process(br, userOrgs);
	}

	private void process(BufferedReader br, List<UserOrg> userOrgs) throws IOException
	{
		log.info("Processing: " + userOrgs + "\nPlease CROSS-CHECK orgnization name, continue? (YES/NO)");
		if ("YES".equals(br.readLine()))
		{
			if (isDeleteIssues)
			{
				log.info("\n\n\t\t******WARRNING****** \nDo you want to DELETE all ISSUES from Organization too? (DELETE/NO)");
				if (!"DELETE".equals(br.readLine()))
				{
					isDeleteIssues = false;
					log.info("Issues will NOT be deleted, Only dumping stats in database");
				}
			}
			archiveZendeskData(userOrgs);
		}
	}

	private void processCSVOption(BufferedReader br) throws IOException
	{
		List<UserOrg> userOrgs = processInputCSV(br);
		if (userOrgs != null && userOrgs.size() > 0)
			process(br, userOrgs);
		else
			log.info("No orgnization found in CSV");
	}

	private List<UserOrg> processInputCSV(BufferedReader br) throws IOException
	{
		log.info("Enter the csv file path ");
		File filename = new File(br.readLine());
		log.info("Input CSV file path - " + filename.getAbsolutePath());
		return getUserOrgsFromFile(CSVParser.parse(filename));
	}

	private void archiveZendeskData(List<UserOrg> userOrgs) throws IOException
	{
		File file = new File("output-" + System.currentTimeMillis() + ".csv");
		this.builder = new CSVBuilder(file, true);
		builder.addRow(header, false);
		List<Long> ticketList = new ArrayList<>();
		for (UserOrg userOrg : userOrgs)
		{
			for (Organization orgnization : orgnizations)
			{
				if (userOrg.getId() == orgnization.getId())
				{
					log.info("Fetching issues from Zendesk: " + orgnization.getName());
					int totalCount = 0;
					int processedCount = 0;
					for (Ticket ticket : zdAPI.getTicketsByName(orgnization, this.queryDifference))
					{
						totalCount++;
						if (ticket.getUrl().contains("/api/v2/tickets/"))
						{
							log.info("Processing issue id: " + ticket.getId());
							if (ticketList.contains(ticket.getId()))
								addRecCSV(orgnization, ticket, CustomUtils.IGNORED, "Duplicate issue found in Zendesk API", false);
							else if (orgnization.getId().equals(ticket.getOrganizationId()))
							{
								processedCount++;
								ticketList.add(ticket.getId());
								zenDeskRepository.importIssueInSql(zdAPI, ticket, orgnization);
								if (isDeleteIssues)
									zdAPI.deleteZendeskIssue(ticket, orgnization);
								addRecCSV(orgnization, ticket, CustomUtils.SUCCESS, "", true);
							}
							else
								addRecCSV(orgnization, ticket, CustomUtils.IGNORED, "OrgId not matched", false);
						}
						else
							builder.addRow(Arrays.asList("" + ticket.getId(), orgnization.getName(), " " + orgnization.getId() + "\t", "Deleted " + CustomUtils.IGNORED, "Ignoring as not a ticket - " + ticket.getUrl()), false).flush();
					}
					log.info("**Total PROCESSED count:" + processedCount + ", for : " + orgnization.getName() + ", **Including Invalid, count:" + totalCount);
					break;
				}
			}
		}
		log.info("Output file generated at " + file.getAbsolutePath() + " successfully process total - " + builder.getRecordCounter());
	}

	private void addRecCSV(Organization orgnization, Ticket ticket, String status, String error, boolean recordCounter) throws IOException
	{
		builder.addRow(Arrays.asList("" + ticket.getId(), orgnization.getName(), " " + orgnization.getId() + "\t", "Deleted " + isDeleteIssues, status, error), recordCounter).flush();
		if (status.equals(CustomUtils.IGNORED))
			log.warn(error);
	}

	private List<UserOrg> getUserOrgsFromFile(ArrayList<List<String>> fileEntries)
	{
		List<UserOrg> userOrgs = new ArrayList<>();
		for (List<String> list : fileEntries)
		{
			UserOrg organization = new UserOrg();
			if (list.size() > 2 && Boolean.parseBoolean(list.get(2)) == true)
			{
				organization.isMigrationRequired(true);
				organization.setId(Long.parseLong(list.get(0)));
				organization.setOrgName(list.get(1));
				userOrgs.add(organization);
			}
		}
		return userOrgs;
	}

	private List<UserOrg> getOrganization(String orgName)
	{
		List<UserOrg> userOrgs = new ArrayList<>();
		for (Organization organization : orgnizations)
			if (organization.getName().toLowerCase().contains(orgName.toLowerCase()))
			{
				UserOrg userOrg = new UserOrg();
				userOrg.setId(organization.getId());
				userOrg.setMigrationFlag(true);
				userOrg.setOrgName(organization.getName());
				userOrgs.add(userOrg);
			}
		return userOrgs;
	}

	private int displayOptions(BufferedReader br) throws NumberFormatException, IOException
	{
		log.info("\nZendesk data DELETION and Migration Tool Menu");
		log.info("=================================================");
		log.info("1. Download the Orgnization List as CSV file");
		log.info("2. Input CSV file to upload Stats in MySql and delete the data from Zendesk");
		log.info("3. Input one organization name to upload and delete data from Zendesk");
		log.info("99. Exit");
		System.out.print("Option? ");
		return Integer.parseInt(br.readLine());
	}
}
