package com.streamsets.sqlrepo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "support_ticket")
public class SupportTicket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "org_id")
	private Long orgId;

	@Column(name = "org_name")
	private String orgName;

	@Column(name = "tags", length = 1000)
	private String tags;

	@Column(name = "asignee_id")
	private Long asigneeId;

	@Column(name = "created_at")
	private Date created;

	@Column(name = "updated_at")
	private Date updated;

	@Column(name = "priority")
	private String priority;

	@Column(name = "pipeline_destination")
	private String pipelineDestination;

	@Column(name = "installation_type")
	private String installationType;

	@Column(name = "environment_type")
	private String environmentType;

	@Column(name = "resolution_type")
	private String resolutionType;

	@Column(name = "resolution")
	private String resolution;

	@Column(name = "components")
	private String components;

	@Column(name = "product_version")
	private String productVersion;

	@Column(name = "product_feedback")
	private String productFeedback;

	@Column(name = "sch_cloud_version")
	private String schCloudVersion;

	@Column(name = "kb_article" , length=1000)
	private String kbArticle;

	@Column(name = "related_jira")
	private String relatedJira;

	@Transient
	private String relatedJiraP;

	@Column(name = "related_esc")
	private String relatedEsc;

	@Column(name = "tx_cluster_type")
	private String txClusterType;

	@Column(name = "status")
	private String status;

	public String getRelatedJiraP()
	{
		return relatedJiraP;
	}

	public void setRelatedJiraP(String relatedJiraP)
	{
		this.relatedJiraP = relatedJiraP;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getOrgId()
	{
		return orgId;
	}

	public void setOrgId(Long orgId)
	{
		this.orgId = orgId;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getTags()
	{
		return tags;
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}

	public Long getAsigneeId()
	{
		return asigneeId;
	}

	public void setAsigneeId(Long asigneeId)
	{
		this.asigneeId = asigneeId;
	}

	public Date getCreated()
	{
		return created;
	}

	public void setCreated(Date created)
	{
		this.created = created;
	}

	public Date getUpdated()
	{
		return updated;
	}

	public void setUpdated(Date updated)
	{
		this.updated = updated;
	}

	public String getPriority()
	{
		return priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public String getPipelineDestination()
	{
		return pipelineDestination;
	}

	public void setPipelineDestination(String pipelineDestination)
	{
		this.pipelineDestination = pipelineDestination;
	}

	public String getInstallationType()
	{
		return installationType;
	}

	public void setInstallationType(String installationType)
	{
		this.installationType = installationType;
	}

	public String getEnvironmentType()
	{
		return environmentType;
	}

	public void setEnvironmentType(String environmentType)
	{
		this.environmentType = environmentType;
	}

	public String getResolutionType()
	{
		return resolutionType;
	}

	public void setResolutionType(String resolutionType)
	{
		this.resolutionType = resolutionType;
	}

	public String getComponents()
	{
		return components;
	}

	public void setComponents(String components)
	{
		this.components = components;
	}

	public String getProductVersion()
	{
		return productVersion;
	}

	public void setProductVersion(String productVersion)
	{
		this.productVersion = productVersion;
	}

	public String getProductFeedback()
	{
		return productFeedback;
	}

	public void setProductFeedback(String productFeedback)
	{
		this.productFeedback = productFeedback;
	}

	public String getSchCloudVersion()
	{
		return schCloudVersion;
	}

	public void setSchCloudVersion(String schCloudVersion)
	{
		this.schCloudVersion = schCloudVersion;
	}

	public String getKbArticle()
	{
		return kbArticle;
	}

	public void setKbArticle(String kbArticle)
	{
		this.kbArticle = kbArticle;
	}

	public String getRelatedJira()
	{
		return relatedJira != null ? relatedJira : relatedJiraP;
	}

	public void setRelatedJira(String relatedJira)
	{
		this.relatedJira = relatedJira;
	}

	public String getRelatedEsc()
	{
		return relatedEsc;
	}

	public void setRelatedEsc(String relatedEsc)
	{
		this.relatedEsc = relatedEsc;
	}

	public String getTxClusterType()
	{
		return txClusterType;
	}

	public void setTxClusterType(String txClusterType)
	{
		this.txClusterType = txClusterType;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getResolution()
	{
		return resolution;
	}

	public void setResolution(String resolution)
	{
		this.resolution = resolution;
	}
}