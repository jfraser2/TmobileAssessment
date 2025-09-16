package springboot.controllers;


import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

// info from - https://stackoverflow.com/questions/31134333/this-application-has-no-explicit-mapping-for-error
@Controller
public class AppErrorController
	implements ErrorController
{
	private ErrorAttributes errorAttributes;
	
    public AppErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }
    
	/**
     * Supports other formats like JSON, XML
     * @param request
     * @return
     */
    @RequestMapping(value = "/error")
    @ResponseBody
    public ResponseEntity<?> error(WebRequest request) {
        Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
        
        String path = (String) body.get("path");
        ResponseEntity<?> aResponse = null;
        HttpStatus status = getStatus(request);
        
        if (null != path)
        {
        	/**
        	 * 
        	 *  answer 37 for site - https://stackoverflow.com/questions/10883211/deadly-cors-when-http-localhost-is-the-origin
        	 *  Add a Chrome Extension Allow-Control-Allow-Origin: * 
        	 *  Extension Url is: https://chromewebstore.google.com/detail/allow-cors-access-control/lhobafahddgcelffkeicbaginigeejlf?hl=en<br/>
        	 *  In the extension add the URL to swagger: http://localhost:8080/swagger-ui/index.html
        	 * 
        	 */
        	if (path.contains("/swagger-ui/index.html"))
        	{
        		StringBuilder outString = new StringBuilder("");
        		outString.append("<p>");
        		outString.append("<h2>");
        		outString.append("Cors has blocked Swagger!!!");
        		outString.append("</h2>");
        		outString.append("To Fix this temporarily Comment out @Configuration <br/>");
        		outString.append("in CorsConfig.java. Then recompile and restart.<br/><br/>");
        		outString.append("Alternatively you can add an Extension to Chrome<br/>");
        		outString.append("The Name is Access-Control-Allow-Origin<br/>");
        		outString.append("The URL is https://chromewebstore.google.com/detail/allow-cors-access-control/lhobafahddgcelffkeicbaginigeejlf?hl=en<br/>");
        		outString.append("In the extension add the URL to swagger: http://localhost:8080/swagger-ui/index.html<br/>");
        		outString.append("</p>");
            	aResponse = new ResponseEntity<>(outString.toString(), status);
        		outString = null;
        	}
        	else
        	{
            	aResponse = new ResponseEntity<Map<String, Object>>(body, status);
        	}
        }
        else
        {
        	aResponse = new ResponseEntity<Map<String, Object>>(body, status);
        }
        
        return aResponse;
    }
    
    private boolean getTraceParameter(WebRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }
    
    private Map<String, Object> getErrorAttributes(WebRequest request,
            boolean includeStackTrace)
    {
    		ErrorAttributeOptions theOptions = null;
    		if (includeStackTrace) {
    			theOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE);
    		} else {
    			theOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);
    		}
    		return this.errorAttributes.getErrorAttributes(request,	theOptions);
    }
    
    private HttpStatus getStatus(WebRequest request) {
    	 
        Integer statusCode = (Integer) request
                .getAttribute("jakarta.servlet.error.status_code", WebRequest.SCOPE_REQUEST);
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    
}
