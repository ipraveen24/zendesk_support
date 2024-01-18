package com.streamsets.gdprPurger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.zendesk.client.v2.model.Organization;

import com.streamsets.datapurger.util.CSVBuilder;

public class DownloadOrgData
{
	public File createOrgCSV(List<Organization> orgnizations, Logger log) throws IOException
	{
		File file = new File("organizations.csv");
		CSVBuilder csvBuilder = null;
		try
		{
			csvBuilder = new CSVBuilder(file, false);
			csvBuilder.addRow(new String[] { "id", "orgName", "isConsiderForMigration" }, true);
			for (Organization organization : orgnizations)
				csvBuilder.addRow(new String[] { "" + organization.getId(), organization.getName(), "" + false }, true);
			csvBuilder.close();
			log.info("Please find the CSV file at " + file.getAbsolutePath());
		} catch (IOException e)
		{
			log.error("Exception in generating orginization data- cause ", e.getMessage(), e);
			throw e;
		} finally
		{
			System.out.println("finally called");
			try
			{
				if (csvBuilder != null)
					csvBuilder.close();
			} catch (IOException e)//ignore exception
			{
			}
		}
		return file;
	}
}
