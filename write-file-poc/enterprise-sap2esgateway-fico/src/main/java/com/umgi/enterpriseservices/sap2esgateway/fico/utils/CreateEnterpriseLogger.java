package com.umgi.enterpriseservices.sap2esgateway.fico.utils;

import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;

import com.umgi.enterpriseseservices.sap2esgateway.fico.constants.SAP2ESficoConstants;
import com.umgi.es.common.util.log.enterpriselogger.EnterpriseLogger;

public class CreateEnterpriseLogger {

	EnterpriseLogger enterpriseLogger=null;
	
	public String elLoggerName;
	public String elServiceName;
	
	public String getElLoggerName() {
		return elLoggerName;
	}
	public void setElLoggerName(String elLoggerName) {
		this.elLoggerName = elLoggerName;
	}
	
	public String getElServiceName() {
		return elServiceName;
	}
	public void setElServiceName(String elServiceName) {
		this.elServiceName = elServiceName;
	}
	
	
	public EnterpriseLogger getEnterpriseLogger(MuleMessage message)
	{
		elLoggerName=message.getProperty("loggerName", PropertyScope.SESSION);
		elServiceName=message.getProperty("loggerServiceName", PropertyScope.SESSION);
		
		if(elLoggerName !=null && elServiceName !=null)
		{
			enterpriseLogger = new EnterpriseLogger(elLoggerName, elServiceName);
		}else{
			enterpriseLogger = new EnterpriseLogger(SAP2ESficoConstants.L_LOGGER_NAME, SAP2ESficoConstants.L_SERVICE_NAME);
		}
		
		return enterpriseLogger;
	}
}
