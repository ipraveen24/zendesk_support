package com.streamsets.datapurger.zendesk;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zendesk.client.v2.Zendesk;
import org.zendesk.client.v2.model.Metric;
import org.zendesk.client.v2.model.Organization;
import org.zendesk.client.v2.model.Ticket;

import com.streamsets.datapurger.util.CustomUtils;

public class ZendeskAPI implements Closeable
{
	private static final Logger log = LoggerFactory.getLogger(ZendeskAPI.class);

	Zendesk zd = null;

	public ZendeskAPI(String baseZendeskUrl, Map<String, String> creds)
	{
		zd = new Zendesk.Builder(baseZendeskUrl).setUsername(creds.get("zendesk_email")).setToken(creds.get("zendesk_token")).setRetry(false).build();
	}

	public ZendeskAPI(String baseZendeskUrl, String email, String pass)
	{
		zd = new Zendesk.Builder(baseZendeskUrl).setUsername(email).setPassword(pass).setRetry(false).build();
	}

	private static List<String> getIntervalDataBetweenStartAndEndDate(Date startDate, Date endDate, Organization org, int queryDifference)
	{
		List<String> queries = new ArrayList<>();
		Calendar curTime = Calendar.getInstance();
		curTime.setTime(startDate);
		curTime.add(Calendar.YEAR, -1);
		Calendar endtime = Calendar.getInstance();
		endtime.setTime(endDate);
		while (curTime.compareTo(endtime) < 1)
		{
			curTime.add(Calendar.DAY_OF_MONTH, -1);
			String from = CustomUtils.sdf.format(new Date(curTime.getTimeInMillis()));
			curTime.add(Calendar.MONTH, queryDifference);
			String to = CustomUtils.sdf.format(new Date(curTime.getTimeInMillis()));
			String query = "=organization:'" + org.getName() + "' created>" + from + " created<" + to;
			queries.add(query);
		}
		return queries;
	}

	public List<Ticket> getTicketsByName(Organization org, int queryDifference)
	{
		List<String> queries = getIntervalDataBetweenStartAndEndDate(org.getCreatedAt(), new Date(), org, queryDifference);
		List<Ticket> issues = new ArrayList<>();
		int count = -1;
		for (String query : queries)
		{
			Iterable<Ticket> iterator = zd.getTicketsFromSearch(query);
			for (Ticket ticket : iterator)
			{
				issues.add(ticket);
				if (count++ % 100 == 0)
					log.info("Fetching in progress.." + (count - 1));
			}
		}
		return issues;
	}

	public Iterable<Organization> getAllOrganizations()
	{
		return zd.getOrganizations();
	}

	public void close()
	{
		zd.close();
	}

	public Metric getTicketMetricByTicket(Long id)
	{
		return zd.getTicketMetricByTicket(id);
	}

	public void deleteZendeskIssue(Ticket ticket, Organization orgnization)
	{
		zd.deleteTicket(ticket.getId());
	}
}
