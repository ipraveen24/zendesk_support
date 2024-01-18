package com.streamsets.gdprPurger;

public class UserOrg
{
	private long id;

	private String orgName;

	private boolean migrationFlag;

	@Override
	public String toString()
	{
		return "[" + orgName + "], ";
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public boolean isMigrationFlag()
	{
		return migrationFlag;
	}

	public void setMigrationFlag(boolean migrationFlag)
	{
		this.migrationFlag = migrationFlag;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void isMigrationRequired(boolean migrationFlag)
	{
		this.migrationFlag = migrationFlag;
	}
}
