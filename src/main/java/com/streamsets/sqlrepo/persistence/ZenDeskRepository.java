package com.streamsets.sqlrepo.persistence;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zendesk.client.v2.model.CustomFieldValue;
import org.zendesk.client.v2.model.Metric;
import org.zendesk.client.v2.model.Organization;
import org.zendesk.client.v2.model.Ticket;

import com.streamsets.datapurger.util.CustomUtils;
import com.streamsets.datapurger.zendesk.ZendeskAPI;
import com.streamsets.datapurger.util.CustomSerializer;
import com.streamsets.sqlrepo.dao.BaseDAO;
import com.streamsets.sqlrepo.model.SMetric;
import com.streamsets.sqlrepo.model.SupportTicket;

public class ZenDeskRepository extends CustomUtils
{
	private BaseDAO baseDAO = null;

	private static final Logger log = LoggerFactory.getLogger(ZenDeskRepository.class);

	public void setBaseDAO(BaseDAO baseDAO)
	{
		this.baseDAO = baseDAO;
	}

	public void importIssueInSql(ZendeskAPI zdAPI, Ticket ticket, Organization orgnization)
	{
		log.debug("Inserting in dataBase " + ticket.getId());
		SupportTicket supportTicket = new SupportTicket();
		supportTicket.setId(ticket.getId());
		supportTicket.setOrgId(ticket.getOrganizationId() == null ? 0l : ticket.getOrganizationId());
		supportTicket.setOrgName(orgnization.getName());
		supportTicket.setAsigneeId(ticket.getAssigneeId());
		supportTicket.setCreated(ticket.getCreatedAt());
		supportTicket.setUpdated(ticket.getUpdatedAt());
		supportTicket.setStatus(ticket.getStatus().name());
		supportTicket.setTags(ticket.getTags() != null ? CustomSerializer.fromList(ticket.getTags()) : null);
		mapCustomFields(supportTicket, ticket.getCustomFields());
		SMetric sMetric = convert(zdAPI.getTicketMetricByTicket(ticket.getId()));
		baseDAO.saveOrupdate(supportTicket);
		if (sMetric != null)
			baseDAO.saveOrupdate(sMetric);
		log.info("Inserted/Updated in database issue Id:" + ticket.getId() );
	}

	private SMetric convert(Metric ticketMetric)
	{
		SMetric sMetric = null;
		if (ticketMetric != null)
		{
			sMetric = new SMetric();
			sMetric.setAgentWaitTimeMinutes(SMetric.getZendeskComboMinutes(ticketMetric.getAgentWaitTimeMinutes()));
			sMetric.setAssignedAt(ticketMetric.getAssignedAt());
			sMetric.setAssigneeStations(ticketMetric.getAssigneeStations());
			sMetric.setAssigneeUpdatedAt(ticketMetric.getAssigneeUpdatedAt());
			sMetric.setCreatedAt(ticketMetric.getCreatedAt());
			sMetric.setFirstResolutionTimeMinutes(SMetric.getZendeskComboMinutes(ticketMetric.getFirstResolutionTimeMinutes()));
			sMetric.setFullResolutionTimeMinutes(SMetric.getZendeskComboMinutes(ticketMetric.getFullResolutionTimeMinutes()));
			sMetric.setGroupStations(ticketMetric.getGroupStations());
			sMetric.setId(ticketMetric.getId());
			sMetric.setInitiallyUpdatedAt(ticketMetric.getInitiallyUpdatedAt());
			sMetric.setLastCommentAddedAt(ticketMetric.getLastCommentAddedAt());
			sMetric.setLastUpdatedAt(ticketMetric.getLastUpdatedAt());
			sMetric.setOnHoldTimeMinutes(SMetric.getZendeskComboMinutes(ticketMetric.getOnHoldTimeMinutes()));
			sMetric.setReopens(ticketMetric.getReopens());
			sMetric.setReplies(ticketMetric.getReplies());
			sMetric.setReplyTimeMinutes(SMetric.getZendeskComboMinutes(ticketMetric.getReplyTimeMinutes()));
			sMetric.setRequesterUpdatedAt(ticketMetric.getRequesterUpdatedAt());
			sMetric.setRequesterWaitTimeMinutes(SMetric.getZendeskComboMinutes(ticketMetric.getRequesterWaitTimeMinutes()));
			sMetric.setSolvedAt(ticketMetric.getSolvedAt());
			sMetric.setTicketId(ticketMetric.getTicketId());
			sMetric.setUpdatedAt(ticketMetric.getUpdatedAt());
		}
		return sMetric;
	}

	private void mapCustomFields(SupportTicket supportTickets, List<CustomFieldValue> customFields)
	{
		for (CustomFieldValue customFieldValue : customFields)
		{
			if (customFieldValue.getValue() != null)
			{
				if (PIPELINE_DESTINATION.equals(customFieldValue.getId()))
					supportTickets.setPipelineDestination(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (PRODUCT_VERSION.equals(customFieldValue.getId()))
					supportTickets.setProductVersion(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (PRODUCT_FEEDBACK.equals(customFieldValue.getId()))
					supportTickets.setProductFeedback(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (COMPONENTS.equals(customFieldValue.getId()))
					supportTickets.setComponents(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (RESOLUTION_TYPE.equals(customFieldValue.getId()))
					supportTickets.setResolutionType(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (RESOLUTION.equals(customFieldValue.getId()))
					supportTickets.setResolution(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (INSTALLATION_TYPE.equals(customFieldValue.getId()))
					supportTickets.setInstallationType(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (ENVIRONMENT_TYPE.equals(customFieldValue.getId()))
					supportTickets.setEnvironmentType(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (SCH_CLOUD_PRODUCT_VERSION.equals(customFieldValue.getId()))
					supportTickets.setSchCloudVersion(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (KB_ARTICLE.equals(customFieldValue.getId()))
					supportTickets.setKbArticle(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (RELATED_ESCALATION.equals(customFieldValue.getId()))
					supportTickets.setRelatedEsc(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (RELATED_JIRA_P.equals(customFieldValue.getId()))
					supportTickets.setRelatedJiraP(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (RELATED_JIRA.equals(customFieldValue.getId()))
					supportTickets.setRelatedJira(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (TRANSFORMER_CLUSTER_TYPE.equals(customFieldValue.getId()))
					supportTickets.setTxClusterType(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
				else if (PRIORITY.equals(customFieldValue.getId()))
					supportTickets.setPriority(CustomSerializer.fromList(Arrays.asList(customFieldValue.getValue())));
			}
		}
	}
}
