package packages.middleware.pub.service;

import com.eka.middleware.service.DataPipeline;
import com.eka.middleware.service.ServiceUtils;
import com.eka.middleware.template.SnippetException;
import com.eka.middleware.flow.FlowResolver;
import java.io.File;
import java.io.FileInputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.*;

public final class auditLogging {

	static JsonObject mainflowJsonObject=null;
	static final String syncBlock=new String("sync");
	public static final void main(DataPipeline dataPipeline) throws SnippetException{
		String fqn="packages.middleware.pub.service.auditLogging";
		long nanoSec=0;
		String logRequest = null;
		String logResponse = null;
		String requestJson="";
		String responseJson="";
	    Date dateTimeStmp=null;
	    long startTime=0;
	    String stopRecursiveLogging=dataPipeline.getString("stopRecursiveLogging");
	    if(stopRecursiveLogging==null && !fqn.equalsIgnoreCase("packages.middleware.pub.service.auditLogging")){
	    	nanoSec=System.nanoTime();
			logRequest = dataPipeline.getMyConfig("logRequest");
			logResponse = dataPipeline.getMyConfig("logResponse");
			requestJson="";
			responseJson="";
			if("true".equalsIgnoreCase(logRequest))
				requestJson=dataPipeline.toJson();
	    	dateTimeStmp=new Date();
	    	startTime=System.currentTimeMillis();
	    }
	    
	    
		try{
		  if(mainflowJsonObject==null)
			synchronized(syncBlock){
			  String location = ServiceUtils.getPackagesPath();
			  String flowRef = location+"packages/middleware/pub/service/auditLogging.flow";
			  if(mainflowJsonObject==null)
				  mainflowJsonObject = Json.createReader(new FileInputStream(new File(flowRef))).readObject();
			}
		  FlowResolver.execute(dataPipeline,mainflowJsonObject);
		}catch(Throwable e) {
			dataPipeline.clear();
			dataPipeline.put("error", e.getMessage());
			dataPipeline.setResponseStatus(500);
			dataPipeline.put("status", "Service error");
			new SnippetException(dataPipeline,"Failed to execute auditLogging", new Exception(e));
		}finally{
			
			if(stopRecursiveLogging==null && !fqn.equalsIgnoreCase("packages.middleware.pub.service.auditLogging")){
				if("true".equalsIgnoreCase(logResponse))
					responseJson=dataPipeline.toJson();
			    long endTime=System.currentTimeMillis();
				Map<String,String> auditLog=new HashMap();
				auditLog.put("correlationId",dataPipeline.getCorrelationId());
				auditLog.put("sessionId",dataPipeline.getSessionId());
				auditLog.put("dateTimeStmp",dateTimeStmp+"");
				auditLog.put("duration",(endTime-startTime)+"");
				if (null == dataPipeline.getString("error")) {
				    auditLog.put("error","");
				} else {
				    auditLog.put("error",dataPipeline.getString("error"));
				}

				auditLog.put("fqn",fqn);
				auditLog.put("request",requestJson);
				auditLog.put("response",responseJson);
				auditLog.put("nanoInstance",nanoSec+"");
				Map<String,Object> asyncInputDoc=new HashMap();
				asyncInputDoc.put("auditLog",auditLog);
				asyncInputDoc.put("stopRecursiveLogging","true");
				dataPipeline.put("asyncInputDoc",asyncInputDoc);
				dataPipeline.applyAsync("packages.middleware.pub.service.auditLogging");
				dataPipeline.drop("asyncInputDoc");
				dataPipeline.drop("asyncOutputDoc");
			}
		}
	}
}
