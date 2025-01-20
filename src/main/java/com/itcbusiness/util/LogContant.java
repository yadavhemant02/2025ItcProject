package com.itcbusiness.util;

import org.springframework.stereotype.Component;

@Component
public class LogContant {

	public static final String logcontrollersuccessadd = "Message| succesfully store information in controllerLyear";
	public static final String logontrollersuccessfind = "Message| succesfully find information in controllerLyear";
	public static final String logcontrolleruccessdelete = "Message| succesfully delete information in controllerLyear";
	public static final String logcontrollersuccessupdate = "Message| succesfully update information in controllerLyear";

	public static final String logservicesuccessadd = "Message| succesfully store information in serviceLyear";
	public static final String logservicesuccessfind = "Message| succesfully find information in serviceLyear";
	public static final String logservicesuccessdelete = "Message| succesfully delete information in serviceLyear";
	public static final String logservicesuccessupdate = "Message| succesfully update information in serviceLyear";

	public static final String logcontrollererroradd = "Error| unable store information in controllerLyear";
	public static final String logcontrollererrorfind = "Error| unable find information in controllerLyear";
	public static final String logcontrollererrordelete = "Error| unable delete information in controllerLyear";
	public static final String logcontrollererrorupdate = "Error| unable update information in controllerLyear";

	public static final String logserviceerroradd = "Error| unable to store information in serviceLyear";
	public static final String logserviceerrorfind = "Error| unable find information in serviceLyear";
	public static final String logserviceerrordelete = "Error| unable delete information in serviceLyear";
	public static final String logserviceerrorupdate = "Error| unable update information in serviceLyear";

}
