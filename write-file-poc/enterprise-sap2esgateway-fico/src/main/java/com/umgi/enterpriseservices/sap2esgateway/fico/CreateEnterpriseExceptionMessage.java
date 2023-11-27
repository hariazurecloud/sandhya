package com.umgi.enterpriseservices.sap2esgateway.fico;

import java.io.StringReader;
import java.net.InetAddress;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import org.mule.api.ExceptionPayload;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.umgi.enterpriseservices.sap2esgateway.fico.utils.CreateEnterpriseLogger;
import com.umgi.enterpriseservices.sap2esgateway.fico.utils.ErrorCategory;
import com.umgi.enterpriseservices.sap2esgateway.fico.utils.UmUtils;
import com.umgi.enterpriseseservices.sap2esgateway.fico.constants.SAP2ESficoConstants;
import com.umgi.es.common.util.log.enterpriselogger.EnterpriseLogger;
import com.umgi.es.common.util.pojo.xml.EnterpriseException;
import com.umgi.es.common.util.pojo.xml.EnterpriseException.OriginalMessage;
import com.umgi.es.common.util.pojo.xml.EnterpriseMessage;

/*************************************************************************************************************************
 * File Name : CreateEnterpriseExceptionMessage.java Author :
 * rafiq.mohammad@invenio-solutions.com Date : 05-Nov-2014 Description : The
 * purpose of this class is to construct the EnterpriseExceptionMessage with
 * exception occurred.
 * 
 * Mapping Details:
 * 
 * exceptionId : UUID messageId : UUID createdUtc : Date errorCode : exception
 * payload code description : exception message details : exception cause
 * category : Warn or Fatal or Error retriable : FALSE resourceName : NR
 * optional resourceId : NR optional service : SAP2ES FICO/ SAP2ES HCM machine :
 * HostName originalPayload : ??
 *************************************************************************************************************************/
public class CreateEnterpriseExceptionMessage implements Callable {

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
					"Error occurred while constructing the Enterprise Exception Message: "
							+ exception.getMessage());

		}
		return nodeValue;

	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {

		// Creating enterpriseLogger
		CreateEnterpriseLogger logger = new CreateEnterpriseLogger();
		emLogger = logger.getEnterpriseLogger(eventContext.getMessage());

		return prepareEEMessage(eventContext.getMessage());
	}

	public void populateIDOC(MuleMessage message,
			EnterpriseException errorMessage) {
		if (message.getInboundProperty("isIdocHuge") != null
				&& message.getInboundProperty("isIdocHuge").toString()
						.equals("true")) {
			emLogger.debug(null,null, "102005111",
					"populating resourceId with the address of idoc saved location with file name");

			if (message.getInboundProperty("IDOC_FILE_LOCATION") != null
					&& message.getInboundProperty("IDOC_FILE_NAME") != null) {
				errorMessage.setResourceId(message.getInboundProperty(
						"IDOC_FILE_LOCATION").toString()
						+ "/"
						+ message.getInboundProperty("IDOC_FILE_NAME")
								.toString());
				
			}
		} else {
			emLogger.debug(null, null,"102005111",
					"Appending original payload under EnterpriseExceptionMessage ");
			
		}
	}

	public Object prepareEEMessage(MuleMessage message) {
		emLogger.debug(null, null,"102005111",
				"Constructing EnterpriseExceptionMessage started");
		
		ExceptionPayload exceptionPayload = message.getExceptionPayload();
		EnterpriseException errorMessage = new EnterpriseException();
	
		try {
			
			errorMessage.setService(SAP2ESficoConstants.SERVICE_NAME);
			errorMessage.setMachine(InetAddress.getLocalHost().getHostName());
			if (null != message
					.getProperty(SAP2ESficoConstants.MESSAGE_ID,
							PropertyScope.SESSION)) {
				
				errorMessage.setMessageId(message
						.getProperty(SAP2ESficoConstants.MESSAGE_ID,
								PropertyScope.SESSION).toString());
			} else {
				
				errorMessage.setMessageId(message.getUniqueId().toString());
			}
			errorMessage.setExceptionId(java.util.UUID.randomUUID().toString());
			errorMessage.setCreatedUtc(UmUtils
					.getDateAsXMLGregorianCalendar(new Date()));
			
			if(null != message.getProperty("resourceId", PropertyScope.SESSION)){
				errorMessage.setResourceId(message.getProperty("resourceId", PropertyScope.SESSION).toString());
			}
			if(null != message.getProperty("resourceName", PropertyScope.SESSION)){
				errorMessage.setResourceName(message.getProperty("resourceName", PropertyScope.SESSION).toString());
			}
			errorMessage.setRetriable(false);
			// In case of API gateway exception
			if (exceptionPayload == null) {
				errorMessage
						.setDescription(SAP2ESficoConstants.APIGATEWAY_ERROR_DESCRIPTION);
				errorMessage.setDetails(SAP2ESficoConstants.DETAILS);
			} else {
				
				errorMessage.setErrorCode(exceptionPayload.getCode());
				errorMessage.setDescription(exceptionPayload.getException()
						.getMessage());
				errorMessage.setDetails(exceptionPayload.getException()
						.getCause().getMessage());
			}
			// In case exception not raised during EM construction
			if (message.getInboundProperty("isEMError") != null
					&& message.getInboundProperty("isEMError").equals("true")) {
					populateIDOC(message, errorMessage);
			}
			errorMessage.setCategory(ErrorCategory.ERROR.getErrorCategory());
			OriginalMessage originalPayload = new OriginalMessage();
			if(null != message.getProperty("eMessage", PropertyScope.INVOCATION)){
            	EnterpriseMessage eMessage = (EnterpriseMessage)message.getProperty("eMessage", PropertyScope.INVOCATION);
            	originalPayload.setAny(eMessage);
            }else{
            	String originalMesage = "originalMessage";
            	originalPayload.setAny(originalMesage);
            }
			errorMessage.setOriginalMessage(originalPayload);
			} catch (Exception exception) {
			emLogger.error(null,null, "102005111",
					"Error occurred while constructing the Enterprise Exception Message: "
							+ exception.getMessage());
			errorMessage.setDetails(exception.getMessage());
			return errorMessage;
		} finally {
			emLogger.debug(null, null,"102005111",
					"Constructing EnterpriseExceptionMessage completed");
		}

		return errorMessage;
	}
}
