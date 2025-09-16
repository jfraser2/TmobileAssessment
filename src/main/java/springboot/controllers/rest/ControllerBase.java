package springboot.controllers.rest;

import java.lang.reflect.Method;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import springboot.autowire.helpers.StringBuilderContainer;
import springboot.dto.response.NonModelAdditionalFields;
import springboot.dto.response.ResultStatus;
import springboot.enums.MapperEnum;

public abstract class ControllerBase
{
	protected static final String EOL = System.getProperty("line.separator");
	protected static final String INDENT = "  ";

	protected static final String GODD_RESPONSE_SUFFIX = "}";
	protected static final String JSON_FIELD_SEPARATOR = ",";
	
	private String generateGoodResponsePrefix(Object databaseEntityObject) {
		
		ResultStatus statusObject = new ResultStatus("OK");
		String tempJson = convertToJsonNoPrettyPrint(statusObject);
		int endIndex = tempJson.length() - 1;
		String statusJson = tempJson.substring(0, endIndex) + JSON_FIELD_SEPARATOR;
		
		String entityName = databaseEntityObject.getClass().getSimpleName();
		return (statusJson + "\"" + entityName + "\": ");
	}
	
	private String removeObjectBeginAndEnd(String objectString) {
		
		String retVar = null;
		
		if (null != objectString && objectString.length() > 3) {
			retVar = objectString.substring(1, objectString.length() - 1);
		}
		
		return retVar;
	}
	
	private String gsonConvertRawJsonToPrettyPrint(String rawJsonString)
	{
		String jsonString = null;
		
		try {
			if (null != rawJsonString && rawJsonString.length() > 0)
			{
				Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
				
				JsonElement jsonElement = JsonParser.parseString(rawJsonString);				
				jsonString = gson.toJson(jsonElement);
			}
		}
		catch(Exception jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
		
	}
	
/*	
	private String convertListToJson(List<Object> anObjectList)
	{
		String jsonString = null;
		
		try {
			if (null != anObjectList && anObjectList.size() > 0)
			{
				Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
				jsonString = gson.toJson(anObjectList);
			}
		}
		catch(Exception jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
		
	}
*/
	
	private String convertListToJsonNoPrettyPrint(List<Object> anObjectList)
	{
		String jsonString = null;
		
		try {
			if (null != anObjectList && anObjectList.size() > 0)
			{
				Gson gson = new GsonBuilder().serializeNulls().create();
				jsonString = gson.toJson(anObjectList);
			}
		}
		catch(Exception jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
		
	}

	private String convertToPrettyPrintJson(String rawJsonString)
	{
		String outputString = null;
		
		try {
			if (null != rawJsonString)
			{
				ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
				ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
				
				outputString = ow.writeValueAsString(mapper.readValue(rawJsonString, Object.class));
			}
		}
		catch(JsonProcessingException jpe)
		{
			outputString = null;
		}
		return outputString;
	}
	
/*
	private String convertToJsonPrettyPrint(Object anObject)
	{
		String jsonString = null;
		
		try {
			if (null != anObject)
			{
				ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
				ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
				
				jsonString = ow.writeValueAsString(anObject);
			}
		}
		catch(JsonProcessingException jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
	}
*/
	
	private String convertToJsonNoPrettyPrint(Object anObject)
	{
		String jsonString = null;
		
		try {
			if (null != anObject)
			{
				ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
				jsonString = mapper.writeValueAsString(anObject);
			}
		}
		catch(JsonProcessingException jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
	}
	
	protected String goodResponse(Object anObject, StringBuilderContainer aContainer, NonModelAdditionalFields nonModelAdditionalFields)
	{
		String jsonString = convertToJsonNoPrettyPrint(anObject);
		
		// Since it is Autowired clear the buffer before you use it
		aContainer.clearStringBuffer();
		StringBuilder aBuilder = aContainer.getStringBuilder();
		
		aBuilder.append(generateGoodResponsePrefix(anObject));
		aBuilder.append(jsonString);
		if (null != nonModelAdditionalFields) {
			String tempJson = convertToJsonNoPrettyPrint(nonModelAdditionalFields);
			String fixedObjectJson = removeObjectBeginAndEnd(tempJson);
			if (null != fixedObjectJson) {
				aBuilder.append(JSON_FIELD_SEPARATOR); // a comma
				aBuilder.append(fixedObjectJson);
			}
			
		}
		aBuilder.append(GODD_RESPONSE_SUFFIX);
		String rawJson = aBuilder.toString();
		
//		System.out.println("raw json is: " + rawJson);
		
		
		return convertToPrettyPrintJson(rawJson);
	}
	
	protected String goodResponseList(List<Object> anObject, StringBuilderContainer aContainer)
	{
		String jsonString = convertListToJsonNoPrettyPrint(anObject);
		
		// Since it is Autowired clear the buffer before you use it
		aContainer.clearStringBuffer();
		StringBuilder aBuilder = aContainer.getStringBuilder();
		
		Object tempObject = anObject.get(0);
		
		aBuilder.append(generateGoodResponsePrefix(tempObject));
		aBuilder.append(jsonString);
		aBuilder.append(GODD_RESPONSE_SUFFIX);
		String rawJson = aBuilder.toString();

//		System.out.println("raw json is: " + rawJson);
		
		return gsonConvertRawJsonToPrettyPrint(rawJson);
	}

	protected Method getMethodOfClass(Class<?> aClass, String methodName)
	{
		Method retVar = null;
		
		if (null != aClass && null != methodName)
		{
			Method [] classMethods = aClass.getMethods();
			
			for (Method method : classMethods)
			{
				if (methodName.equals(method.getName()))
				{
					retVar = method;
					break;
				}
			}
		}
		
		return retVar;
		
	}
	
	protected HttpHeaders createResponseHeader(HttpServletRequest request)
	{
		// support CORS
//		System.out.println("Access-Control-Allow-Origin is: " + request.getHeader("Origin"));
		HttpHeaders aResponseHeader = new HttpHeaders();
		
		if (null != request) {
			String tempOrigin = request.getHeader("Origin");
			if (null != tempOrigin && tempOrigin.length() > 0) {
				aResponseHeader.add("Access-Control-Allow-Origin", tempOrigin);
			}	
//			aResponseHeader.add("Access-Control-Allow-Origin", "*");
		}
		aResponseHeader.add("Content-Type", "application/json");
		
		return aResponseHeader;
		
	}
	
}
