package springboot.enums;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum LocalDateTimeEnum {
	INSTANCE;
	private final ZoneId defaultZoneId = ZoneId.of("America/Chicago");
	private final ZoneId UTC_ZONE_ID = ZoneOffset.UTC; // Zulu or London Time +00:00
	
	public final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
	public final String DATE_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss[xx][XX]";
	public final String DATE_FORMAT3 = "MM-dd-yyyy HH:mm:ss z (XXX)";
	
	private ZoneId zoneId = null;

	private LocalDateTimeEnum() {
	    // Perform any configuration here.
	    this.zoneId = this.defaultZoneId;
	}

	public void resetDefaultZoneId() {
	    this.zoneId = this.defaultZoneId;
	}
	
	private boolean validString(String aString) {
		boolean retVar = false;
		
		if (null != aString && aString.length() > 0) {
			retVar = true;
		}
		 return retVar;	
	}
	
	private ZoneId getZoneId(String newZoneId) {
		
		ZoneId tempZoneId = null;
		if (validString(newZoneId)) {
			// If the zone ID equals 'Z', the result is ZoneOffset.UTC
			try {
				tempZoneId = ZoneId.of(newZoneId);
			} catch (DateTimeException dte) { // if the zone ID has an invalid format
				tempZoneId = null;
			}
		}
		
		return tempZoneId;
		
	}
	
	public void setZoneId(String newZoneId) {
		if (validString(newZoneId)) {
			// If the zone ID equals 'Z', the result is ZoneOffset.UTC
			ZoneId tempZoneId = getZoneId(newZoneId);
			if (null != tempZoneId) {
				this.zoneId = tempZoneId;
			}
		}
	}
	
	public LocalDateTime now() {
	    Instant instant = Instant.now(); // Current instant from London(Greenwich)
		return  LocalDateTime.ofInstant(instant, this.zoneId);
	}
	
	public LocalDateTime now(ZoneId aZoneId) {
		LocalDateTime retVar = null;
		
	    Instant instant = Instant.now(); // Current instant from London(Greenwich)
	    if (null != aZoneId) {
	    	retVar = LocalDateTime.ofInstant(instant, aZoneId);
	    }
	    
		return  retVar;
	}
	
	public LocalDateTime now(String aZoneId) {
		LocalDateTime retVar = null;
		
	    Instant instant = Instant.now(); // Current instant from London(Greenwich)
	    
	    if (validString(aZoneId)) {
	    	ZoneId tempZoneId = getZoneId(aZoneId);
	    	if (null != tempZoneId) {
	    		retVar = LocalDateTime.ofInstant(instant, tempZoneId);
	    	}
	    }
	    
		return  retVar;
	}
	
	public LocalDateTime createLocalDateTime(String date, String dateFormat) {
		LocalDateTime retVar = null;
		
		if (validString(date) && validString(dateFormat)) {
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withZone(this.zoneId);
				retVar = LocalDateTime.parse(date, dtf);
			} catch(Exception e) {
				retVar = null;
			}
		}
		
		return retVar;
	}
	
	public LocalDateTime createLocalDateTime(String date, String dateFormat, String theZone) {
		LocalDateTime retVar = null;
		
		if (validString(date) && validString(dateFormat) && validString(theZone)) {
			ZoneId tempZoneId = getZoneId(theZone);
			try {
				if (null != tempZoneId) {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withZone(tempZoneId);
					retVar = LocalDateTime.parse(date, dtf);
				}	
			} catch(Exception e) {
				retVar = null;
			}
		}
		
		return retVar;
	}
	
	public Long convertLocalDateToMilliseconds(LocalDateTime orig) {
		
		Long retVar = null;
		
		if (null != orig) {
		    // Convert LocalDateTime to Instant using the ZoneId
	        Instant instant = orig.atZone(UTC_ZONE_ID).toInstant();
			
			retVar = Long.valueOf(instant.toEpochMilli());
		}	
		
		return retVar;
	}

	public Long convertDateStringToMilliseconds(String date, String dateFormat) {
		Long retVar = null;
		
		if (validString(date) && validString(dateFormat)) {
			LocalDateTime orig = createLocalDateTime(date, dateFormat);
			if (null != orig) {
				retVar = convertLocalDateToMilliseconds(orig);
			}
		}	
		
		return retVar;
	}
	
	public Long convertDateStringToMilliseconds(String date, String dateFormat, String theZone) {
		Long retVar = null;
		
		if (validString(date) && validString(dateFormat) && validString(theZone))  {
			LocalDateTime orig = createLocalDateTime(date, dateFormat, theZone);
			if (null != orig) {
				retVar = convertLocalDateToMilliseconds(orig);
			}
		}	
		
		return retVar;
	}
	
	public String writeDateString(LocalDateTime theTime, String dateFormat) {
		String retVar = null;
		
		if (null != theTime && validString(dateFormat)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(this.zoneId);			
			retVar = theTime.format(formatter);
		}
		
		return retVar;
	}
	
	public String writeDateString(LocalDateTime theTime, String dateFormat, String theZone) {
		String retVar = null;
		
		if (null != theTime && validString(dateFormat) && validString(theZone)) {
			ZoneId tempZoneId = getZoneId(theZone);
			if (null != tempZoneId) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(tempZoneId);			
				retVar = theTime.format(formatter);
			}	
		}
		
		return retVar;
	}
	
	public String convertZuluMillisecondsToDateString(Long milliSeconds, String dateFormat) {
		String retVar = null;
		
		if (null != milliSeconds && validString(dateFormat)) {
			Instant i = Instant.ofEpochSecond(milliSeconds);
			ZonedDateTime zulu = ZonedDateTime.ofInstant(i, UTC_ZONE_ID);
			
			ZoneId usingZone = this.zoneId;
			if (null != usingZone) {
		        ZonedDateTime targetZonedDateTime = zulu.withZoneSameInstant(usingZone);
		        LocalDateTime targetLocalDateTime = targetZonedDateTime.toLocalDateTime();				
				retVar = writeDateString(targetLocalDateTime, dateFormat);
			}	
		}
		
		return retVar;
	}
	
	public String convertZuluMillisecondsToDateString(Long milliSeconds, String dateFormat, String timeZoneId) {
		String retVar = null;
		
		if (null != milliSeconds && validString(dateFormat) && validString(timeZoneId)) {
			Instant i = Instant.ofEpochSecond(milliSeconds);
			ZonedDateTime zulu = ZonedDateTime.ofInstant(i, UTC_ZONE_ID);
			
			ZoneId usingZone = getZoneId(timeZoneId);
			if (null != usingZone) {
				ZonedDateTime targetZonedDateTime = zulu.withZoneSameInstant(usingZone);
		        LocalDateTime targetLocalDateTime = targetZonedDateTime.toLocalDateTime();				
				retVar = writeDateString(targetLocalDateTime, dateFormat, timeZoneId);
			}	
		}
		
		return retVar;
	}

}
