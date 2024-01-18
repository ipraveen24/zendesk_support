package com.streamsets.gdprPurger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.streamsets.datapurger.util.CustomUtils;
import com.streamsets.datapurger.zendesk.ZendeskAPI;
import com.streamsets.supportlibrary.aws.AWSSecret;

public class GDPRpurge
{
	private static final Logger log = LoggerFactory.getLogger(GDPRpurge.class);

	public static void main(String[] args) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(new File(args[0]).getPath());
			CustomUtils customUtils = (CustomUtils) context.getBean("customUtils");
			ZendeskAPI zdAPI = null;
			if (customUtils.isUseAWScredentials())
			{
				AWSSecret secret = new AWSSecret();
				zdAPI = new ZendeskAPI(customUtils.getBaseZendeskUrl(), secret.getSecret(System.getenv("SECRET_NAME"), System.getenv("AWS_REGION")));
			}
			else
				zdAPI = new ZendeskAPI(customUtils.getBaseZendeskUrl(), customUtils.getEmail(), customUtils.getPass());
			ZendeskDataPurger zenDeskDataPurger = new ZendeskDataPurger(context, zdAPI, customUtils.getQueryDifference(), customUtils.isDeleteIssues());
			zenDeskDataPurger.process(br);
		} catch (Exception ex)
		{
			log.error("Exception in tool ", ex.getMessage(), ex);
			throw ex;
		} finally
		{
			br.close();
		}
		System.exit(0);
	}
}
