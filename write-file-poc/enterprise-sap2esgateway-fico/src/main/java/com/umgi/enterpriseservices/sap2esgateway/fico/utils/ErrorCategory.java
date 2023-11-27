package com.umgi.enterpriseservices.sap2esgateway.fico.utils;
/*************************************************************************************************************************
 * File Name 	: ErrorCategory.java 
 * Author 		: rafiq.mohammad@invenio-solutions.com 
 * Date 		: 05-Nov-2014 
 * Description 	: This is an ENUM which specifies the Error Category
 *************************************************************************************************************************/
public enum ErrorCategory {
		WARN("Warn"), FATAL("Fatal"), ERROR("Error");
	 
		private String errorCategory;
	 
		private ErrorCategory(String s) {
			errorCategory = s;
		}
	 
		public String getErrorCategory() {
			return errorCategory;
		}
	 
	

}
