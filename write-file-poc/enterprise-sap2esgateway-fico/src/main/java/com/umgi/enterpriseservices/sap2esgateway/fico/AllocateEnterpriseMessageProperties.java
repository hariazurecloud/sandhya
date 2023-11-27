package com.umgi.enterpriseservices.sap2esgateway.fico;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

public class AllocateEnterpriseMessageProperties implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleMessage message = eventContext.getMessage();
		
		String  resourceName="";

			if( null != message.getProperty("IDOC_MESSAGE_ID",PropertyScope.INVOCATION))
			{
					message.setProperty("messageId", message.getProperty("IDOC_MESSAGE_ID" , PropertyScope.INVOCATION), PropertyScope.SESSION);
			}else{
				message.setProperty("messageId",message.getUniqueId().toString(), PropertyScope.SESSION);
			}
			message.setProperty("messageSource" , "FICO-OUTBOUND", PropertyScope.SESSION);
			message.setProperty("messageAction" , "Publish", PropertyScope.SESSION);

			if(null != message.getProperty("DOCNUM" , PropertyScope	.INVOCATION))
			{
				message.setProperty("resourceId", message.getProperty("DOCNUM", PropertyScope.INVOCATION), PropertyScope.SESSION);
			}

			// ResourceName
			resourceName = message.getProperty("IDOCTYP",PropertyScope.INVOCATION);
			
			//MESSAGE_CODE
			if(null != message.getProperty("MESCOD",PropertyScope.INVOCATION))
			{
				resourceName = resourceName+"-"+message.getProperty("MESCOD",PropertyScope.INVOCATION);
			}
			//MESSSAGE_FUNCTION
			if(null!= message.getProperty("MESFCT",PropertyScope.INVOCATION))
			{
				resourceName = resourceName+"-"+message.getProperty("MESFCT",PropertyScope.INVOCATION);
			}
			message.setProperty("resourceName", resourceName, PropertyScope.SESSION);
				
		return message;
	}

}
