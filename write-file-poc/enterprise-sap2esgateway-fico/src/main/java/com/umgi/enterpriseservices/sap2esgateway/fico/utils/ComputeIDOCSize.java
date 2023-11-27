package com.umgi.enterpriseservices.sap2esgateway.fico.utils;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

/*************************************************************************************************************************
 * File Name : ComputeIDOCSize.java Author :
 * rafiq.mohammad@invenio-solutions.com Date : 05-Nov-2014 Description : The
 * purpose of this class is just to Compute the size of IDOCSize in megabytes
 * and return back the actual mule message received
 * 
 *************************************************************************************************************************/

public class ComputeIDOCSize implements Callable {

	private static final long MEGABYTE = 1024L * 1024L;

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {

		MuleMessage message = eventContext.getMessage();
		// Creating enterpriseLogger
		String payload = message.getPayloadAsString();
		long size = 0L;
		// computing payload size in bytes
		size = payload.getBytes().length;
		// bytes to megabytes
		

		// IDOC_THRESHOLD
		//int thresholdValue = 1;
		long thresholdValue = MEGABYTE; //Default set to 1MB
		if (message.getOutboundProperty("IDOC_THRESHOLD") != null) {
//			thresholdValue = Integer.parseInt(message.getOutboundProperty("IDOC_THRESHOLD").toString());
			thresholdValue = Long.parseLong(message.getOutboundProperty("IDOC_THRESHOLD").toString());
		}

		if (size >= thresholdValue) {
			message.setOutboundProperty("isIdocHuge", true);
		} else {
			message.setOutboundProperty("isIdocHuge", false);
		}
		return message;
	}

}
