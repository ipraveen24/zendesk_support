package com.streamsets.sqlrepo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.zendesk.client.v2.model.Metric.ZendeskComboMinutes;

@Entity
@Table(name = "metrics")
public class SMetric implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ticketId")
	protected Long ticketId;

	private Long id;

	protected Long groupStations;

	protected Long assigneeStations;

	protected Long reopens;

	protected Long replies;

	protected Date assigneeUpdatedAt;

	protected Date requesterUpdatedAt;

	protected Date lastUpdatedAt;

	protected Date initiallyUpdatedAt;

	protected Date assignedAt;

	protected Date solvedAt;

	protected Date lastCommentAddedAt;

	protected String replyTimeMinutes;

	protected String firstResolutionTimeMinutes;

	protected String fullResolutionTimeMinutes;

	protected String agentWaitTimeMinutes;

	protected String requesterWaitTimeMinutes;

	protected String onHoldTimeMinutes;

	protected Date createdAt;

	protected Date updatedAt;

	public Long getTicketId()
	{
		return ticketId;
	}

	public void setTicketId(Long ticketId)
	{
		this.ticketId = ticketId;
	}

	public Long getGroupStations()
	{
		return groupStations;
	}

	public void setGroupStations(Long groupStations)
	{
		this.groupStations = groupStations;
	}

	public Long getAssigneeStations()
	{
		return assigneeStations;
	}

	public void setAssigneeStations(Long assigneeStations)
	{
		this.assigneeStations = assigneeStations;
	}

	public Long getReopens()
	{
		return reopens;
	}

	public void setReopens(Long reopens)
	{
		this.reopens = reopens;
	}

	public Long getReplies()
	{
		return replies;
	}

	public void setReplies(Long replies)
	{
		this.replies = replies;
	}

	public Date getAssigneeUpdatedAt()
	{
		return assigneeUpdatedAt;
	}

	public void setAssigneeUpdatedAt(Date assigneeUpdatedAt)
	{
		this.assigneeUpdatedAt = assigneeUpdatedAt;
	}

	public Date getRequesterUpdatedAt()
	{
		return requesterUpdatedAt;
	}

	public void setRequesterUpdatedAt(Date requesterUpdatedAt)
	{
		this.requesterUpdatedAt = requesterUpdatedAt;
	}

	public Date getLastUpdatedAt()
	{
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt)
	{
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public Date getInitiallyUpdatedAt()
	{
		return initiallyUpdatedAt;
	}

	public void setInitiallyUpdatedAt(Date initiallyUpdatedAt)
	{
		this.initiallyUpdatedAt = initiallyUpdatedAt;
	}

	public Date getAssignedAt()
	{
		return assignedAt;
	}

	public void setAssignedAt(Date assignedAt)
	{
		this.assignedAt = assignedAt;
	}

	public Date getSolvedAt()
	{
		return solvedAt;
	}

	public void setSolvedAt(Date solvedAt)
	{
		this.solvedAt = solvedAt;
	}

	public Date getLastCommentAddedAt()
	{
		return lastCommentAddedAt;
	}

	public void setLastCommentAddedAt(Date lastCommentAddedAt)
	{
		this.lastCommentAddedAt = lastCommentAddedAt;
	}

	public Date getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(Date createdAt)
	{
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getReplyTimeMinutes()
	{
		return replyTimeMinutes;
	}

	public void setReplyTimeMinutes(String replyTimeMinutes)
	{
		this.replyTimeMinutes = replyTimeMinutes;
	}

	public String getFirstResolutionTimeMinutes()
	{
		return firstResolutionTimeMinutes;
	}

	public void setFirstResolutionTimeMinutes(String firstResolutionTimeMinutes)
	{
		this.firstResolutionTimeMinutes = firstResolutionTimeMinutes;
	}

	public String getFullResolutionTimeMinutes()
	{
		return fullResolutionTimeMinutes;
	}

	public void setFullResolutionTimeMinutes(String fullResolutionTimeMinutes)
	{
		this.fullResolutionTimeMinutes = fullResolutionTimeMinutes;
	}

	public String getAgentWaitTimeMinutes()
	{
		return agentWaitTimeMinutes;
	}

	public void setAgentWaitTimeMinutes(String agentWaitTimeMinutes)
	{
		this.agentWaitTimeMinutes = agentWaitTimeMinutes;
	}

	public String getRequesterWaitTimeMinutes()
	{
		return requesterWaitTimeMinutes;
	}

	public void setRequesterWaitTimeMinutes(String requesterWaitTimeMinutes)
	{
		this.requesterWaitTimeMinutes = requesterWaitTimeMinutes;
	}

	public String getOnHoldTimeMinutes()
	{
		return onHoldTimeMinutes;
	}

	public void setOnHoldTimeMinutes(String onHoldTimeMinutes)
	{
		this.onHoldTimeMinutes = onHoldTimeMinutes;
	}

	public static String getZendeskComboMinutes(ZendeskComboMinutes zendeskComboMinutes)
	{
		return "B:" + zendeskComboMinutes.getBusinessMinutes() + ",C:" + zendeskComboMinutes.getCalendarMinutes();
	}
}