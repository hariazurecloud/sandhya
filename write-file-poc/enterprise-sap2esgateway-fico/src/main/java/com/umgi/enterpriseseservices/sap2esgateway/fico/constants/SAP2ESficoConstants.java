package com.umgi.enterpriseseservices.sap2esgateway.fico.constants;

/*************************************************************************************************************************
 * File Name 	: SAP2ESFICOConstants.java 
 * Author 		: rafiq.mohammad@invenio-solutions.com 
 * Date 		: 05-Nov-2014 
 * Description :The purpose of this class is to declare all the constants used in SAP2ES FICO
 *************************************************************************************************************************/

public interface SAP2ESficoConstants {
	
	public static final String L_LOGGER_NAME="com.umgi.es.sap2esfico";
	
	public static final String L_SERVICE_NAME="SAP2ES_FICO";
	
	public static final String IDOC_TYPE = "IDOCTYP";
	
	public static final String IDOC_MESSAGE_ID = "IDOC_MESSAGE_ID";
	
	public static final String MESSAGE_ID = "messageId";
	
	public static final String MESSAGE_ACTION = "Publish";
	
	public static final String MESSAGE_CODE = "MESCOD";
	
	public static final String MESSSAGE_FUNCTION = "MESFCT";
	
	public static final String DOCUMENT_NUMBER = "DOCNUM";
	
	// actual value is computed in mule flow
	public static final String IDOC_FILE_LOCATION = "IDOC_FILE_LOCATION";
	
	// actual value is computed in mule flow
	public static final String IDOC_FILE_NAME = "IDOC_FILE_NAME";
	
	public static final String DATE_FORMAT = "yyyyMMdd.HHmmss";
	
	public static final String SERVICE_NAME = "SAP2ES-FICO";

	public static final String APIGATEWAY_ERROR_DESCRIPTION = "The API Gateway is not available (or) not running";

	public static final String DETAILS = "The API Gateway is not running. Please verify the API Gateway status";

}
