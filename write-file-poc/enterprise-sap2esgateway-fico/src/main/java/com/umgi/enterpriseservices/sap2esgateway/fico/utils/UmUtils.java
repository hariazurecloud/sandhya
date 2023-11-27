package com.umgi.enterpriseservices.sap2esgateway.fico.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/*************************************************************************************************************************
 * File Name 	: CreateEnterpriseMessage.java 
 * Author 		: rafiq.mohammad@invenio-solutions.com 
 * Date 		: 05-Nov-2014 
 * Description 	: This is an Util class returns GregorianCalendar by sending date as input-parameter. 
 *************************************************************************************************************************/
public class UmUtils {
	
	/**
	 * This method is used to get the date in XMLGregorianCalendar format
	 * 
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar getDateAsXMLGregorianCalendar(Date date) {

		XMLGregorianCalendar resultDate = null;
		try {
			GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar
					.getInstance();
			gregorianCalendar.setTime(date);
			resultDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					gregorianCalendar);
		} catch (DatatypeConfigurationException e) {

			e.printStackTrace();
		}
		return resultDate;
	}

}
