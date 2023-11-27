package com.umgi.enterpriseservices.sap2esgateway.fico;

import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.umgi.enterpriseservices.sap2esgateway.fico.utils.CreateEnterpriseLogger;
import com.umgi.enterpriseservices.sap2esgateway.fico.utils.UmUtils;
import com.umgi.enterpriseseservices.sap2esgateway.fico.constants.SAP2ESficoConstants;
import com.umgi.es.common.util.log.enterpriselogger.EnterpriseLogger;
import com.umgi.es.common.util.pojo.xml.EnterpriseHeader;
import com.umgi.es.common.util.pojo.xml.EnterpriseHeaderResource;
import com.umgi.es.common.util.pojo.xml.EnterpriseMessage;

/*************************************************************************************************************************
 * File Name : CreateEnterpriseMessage.java Author :
 * rafiq.mohammad@invenio-solutions.com Date : 05-Nov-2014
 * 
 * Description : The purpose of this class is to construct the enterprise
 * message with received IDOC meta data i.e. IDOC meta data into
 * EnterpriseMessage. IDOC Mapping Details:
 * 
 * 1. EnterpriseMessage
 * 
 * a. EnterpriseHeader
 * 
 * - MessageId : JOB_ID (UUID) - ThreadId : MULE Message ID - CreatedUtc : UTC
 * DateTime - Source : FICO-OUTBOUND
 * 
 * b. EnterpriseHeaderResource
 * 
 * - ResourceName : combination of IDoc type, message code and message function
 * separated with hyphen(-). i.e IDOCTYP-MESCODE-MESFCT - ResourceId : IDOC
 * Number i.e DOCNUM Note: in case of IDOC is huge location should be appended
 * of IDOC Filestored with file-name only if IDOC is above the threshold value.
 * c. EnterpriseMessage.MessageBody - only if IDOC is below the threshold value.
 * 
 *************************************************************************************************************************/
public class CreateEnterpriseMessage implements Callable {

	EnterpriseLogger emLogger = null;

	/**
	 * This method is used to get the node object from the message payload
	 * 
	 * @param message
	 * @return
	 */
	public Node getMessageBodyFromPayload(String idoc_xml) {

		Node nodeValue = null;

		try {
			nodeValue = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse((new InputSource(new StringReader(idoc_xml))))
					.getDocumentElement();

		} catch (Exception exception) {
			emLogger.error(null,null, "102005111",
					"Error occurred while constructing the Enterprise Message: "
							+ exception.getMessage());

		}
		return nodeValue;

	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {

		MuleMessage message = null;
		try {
			String resourceName;
			String fileName;

			message = eventContext.getMessage();
			// Creating enterpriseLogger
			CreateEnterpriseLogger logger = new CreateEnterpriseLogger();
			emLogger = logger.getEnterpriseLogger(message);

			emLogger.debug(null,null, "102005111",
					"Constructing EnterpriceMessage is started");

			EnterpriseMessage eMessage = new EnterpriseMessage();
			EnterpriseHeader eHeader = new EnterpriseHeader();
			EnterpriseHeaderResource eResourse = new EnterpriseHeaderResource();

			// Logging received IDOC as payload
			emLogger.debug(message.getPayload(), null,"102005111", "Current Payload");

			// Preparing EnterpriseHeader
			if(null != message.getProperty(SAP2ESficoConstants.IDOC_MESSAGE_ID,PropertyScope.OUTBOUND))
			{
				eHeader.setMessageId(message.getProperty(SAP2ESficoConstants.IDOC_MESSAGE_ID,PropertyScope.OUTBOUND).toString());
				eHeader.setThreadId(message.getProperty(SAP2ESficoConstants.IDOC_MESSAGE_ID,PropertyScope.OUTBOUND).toString());
			
			}else{
				eHeader.setMessageId(message.getUniqueId().toString());
				eHeader.setThreadId(message.getUniqueId().toString());
				
			}
			eHeader.setAction(SAP2ESficoConstants.MESSAGE_ACTION);
			eHeader.setCreatedUtc((UmUtils
					.getDateAsXMLGregorianCalendar(new Date())));
			eHeader.setSource("FICO-OUTBOUND");
			// Preparing EnterpriseHeaderResource
			
			// ResourceName
			resourceName = message.getProperty(SAP2ESficoConstants.IDOC_TYPE,PropertyScope.OUTBOUND);
			
			
			//MESSAGE_CODE
			if(null != message.getProperty(SAP2ESficoConstants.MESSAGE_CODE,PropertyScope.OUTBOUND))
			{
				resourceName = resourceName+"-"+message.getProperty(SAP2ESficoConstants.MESSAGE_CODE,PropertyScope.OUTBOUND);
			}
			//MESSSAGE_FUNCTION
			if(null!= message.getProperty(SAP2ESficoConstants.MESSSAGE_FUNCTION,PropertyScope.OUTBOUND))
			{
				resourceName = resourceName+"-"+message.getProperty(SAP2ESficoConstants.MESSSAGE_FUNCTION,PropertyScope.OUTBOUND);
				
			}

			eResourse.setResourceName(resourceName);
			
			
			String resourceID = "";

			// Set IDOC type to session variable
			if (message.getProperty(SAP2ESficoConstants.IDOC_TYPE,
					PropertyScope.OUTBOUND) != null) {
				message.setProperty("DOCTYPE", message.getProperty(
						SAP2ESficoConstants.IDOC_TYPE, PropertyScope.OUTBOUND),
						PropertyScope.SESSION);
			}

			if (message.getProperty(SAP2ESficoConstants.DOCUMENT_NUMBER,
					PropertyScope.OUTBOUND) != null) {
				resourceID = message.getProperty(
						SAP2ESficoConstants.DOCUMENT_NUMBER,
						PropertyScope.OUTBOUND);
			}
			eResourse.setResourceId(resourceID);

			// Adding EnterpriseHeaderResource into EnterpriseHeader
			eHeader.setResource(eResourse);

			// Preparing enterpriseMessageBody and map payload if IDOC is small
			if (message.getProperty("isIdocHuge", PropertyScope.OUTBOUND)
					.toString().equals("false")) {
				emLogger.debug(null, null,"102005111",
						"IDoc is small.Hence wrapping with in EnterpriseMessage");
				/*
				 * // <?xml version="1.1"?>
				 * //eMessageBody.setAny(message.getPayload());
				 * eMessageBody.setAny(new JAXBElement<String>( new
				 * QName("any"),String.class, message.getPayloadAsString()));
				 */

				EnterpriseMessage.MessageBody messegeBody = new EnterpriseMessage.MessageBody();
				Node messageBodyData = getMessageBodyFromPayload(message
						.getPayloadAsString());
				messegeBody.setAny(messageBodyData);
				eMessage.setMessageBody(messegeBody);
			} else {
				emLogger.debug(null,null, "102005111",
						"IDoc is Huge.Hence appending source with file location into EnterpriseMessage");
				fileName = message.getProperty(SAP2ESficoConstants.IDOC_FILE_LOCATION,PropertyScope.OUTBOUND)+ "/"
						+ message.getProperty(SAP2ESficoConstants.IDOC_FILE_NAME,PropertyScope.OUTBOUND)+".xml";
				emLogger.debug(null,null, "102005111", "File location: "+fileName);
				eResourse.setResourceId(fileName);
			}
			// Adding EnterpriseHeader into EnterpriseMessage which should have
			// header and messageBody
			eMessage.setEnterpriseHeader(eHeader);

			emLogger.debug(null,null, "102005111",
					"CreateEnterpriseMessage completed successfully without any errors");
			message.setOutboundProperty("isEMError", "false");
			return eMessage;
		} catch (Exception enterpriseMessageException) {
			message.setOutboundProperty("isEMError", "true");
			emLogger.error(null,null, "102005111",
					"Exception occurred during Preparing EnterpriseMessage"
							+ enterpriseMessageException.getMessage());
			throw new Exception(enterpriseMessageException);
		} finally {
			emLogger.debug(null,null, "102005111",
					"Constructing EnterpriceMessage is completed");
		}

	}

}
