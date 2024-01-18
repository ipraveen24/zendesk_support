package com.streamsets.datapurger.util;

import java.text.SimpleDateFormat;

public class CustomUtils
{
	public static final Long PIPELINE_DESTINATION = 1500006978082l;

	public static final Long PRODUCT_VERSION = 80670167l;

	public static final Long PRODUCT_FEEDBACK = 360028893173l;

	public static final Long COMPONENTS = 360009237153l;

	public static final Long RESOLUTION_TYPE = 360000347207l;

	public static final Long RESOLUTION = 32244328l;

	public static final Long INSTALLATION_TYPE = 360046022434l;

	public static final Long ENVIRONMENT_TYPE = 360008830533l;

	public static final Long SCH_CLOUD_PRODUCT_VERSION = 360032637693l;

	public static final Long KB_ARTICLE = 360000358088l;

	public static final Long RELATED_JIRA_P = 77967547l;

	public static final Long RELATED_ESCALATION = 360015203274l;

	public static final Long RELATED_JIRA = 360015275973l;

	public static final Long TRANSFORMER_CLUSTER_TYPE = 360045997893l;

	public static final Long PRIORITY = 31854147l;

	public static final String SUCCESS = "SUCCESS";

	public static final String IGNORED = "IGNORED";

	private String baseZendeskUrl;

	private String email;

	private String pass;

	private boolean useAWScredentials;

	private int queryDifference;

	private boolean deleteIssues;

	public boolean isDeleteIssues()
	{
		return deleteIssues;
	}

	public void setDeleteIssues(boolean deleteIssues)
	{
		this.deleteIssues = deleteIssues;
	}

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public int getQueryDifference()
	{
		return queryDifference;
	}

	public void setQueryDifference(int queryDifference)
	{
		this.queryDifference = queryDifference;
	}

	public String getBaseZendeskUrl()
	{
		return baseZendeskUrl;
	}

	public void setBaseZendeskUrl(String baseZendeskUrl)
	{
		this.baseZendeskUrl = baseZendeskUrl;
	}

	public boolean isUseAWScredentials()
	{
		return useAWScredentials;
	}

	public void setUseAWScredentials(boolean useAWScredentials)
	{
		this.useAWScredentials = useAWScredentials;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}
}
