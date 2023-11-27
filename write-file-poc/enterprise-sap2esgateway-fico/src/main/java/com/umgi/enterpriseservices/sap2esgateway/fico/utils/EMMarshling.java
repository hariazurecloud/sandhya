package com.umgi.enterpriseservices.sap2esgateway.fico.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.umgi.es.common.util.pojo.xml.EnterpriseMessage;

public class EMMarshling {

	
	public Object generateSegmentXMLMsg(EnterpriseMessage payload) throws JAXBException {
	
		
		String legalEntityXMLMessage = new String();
		
		if (payload != null) {
			legalEntityXMLMessage = getXMLMessageFromPojo(payload);
		}

		return legalEntityXMLMessage;
		
		
		
	}
	
	public static String getXMLMessageFromPojo(EnterpriseMessage enterpriseMessage)
			throws JAXBException {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(EnterpriseMessage.class);
		
		// Generate legalEntity XML message.
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration",
						Boolean.FALSE);

				// Make XML pretty format.
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						Boolean.TRUE);
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

				StringWriter stringWriter = new StringWriter();

				// Perform Marshaling operation
				jaxbMarshaller.marshal(enterpriseMessage, stringWriter);


				return stringWriter.toString();
		
	}
}
