package packages.middleware.pub.xml;
import com.eka.middleware.service.DataPipeline;
import com.eka.middleware.service.ServiceUtils;
import com.eka.middleware.template.SnippetException;
import java.util.*;
import java.util.regex.Pattern;
public final class fromXML{
	public static final void main(DataPipeline dataPipeline) throws SnippetException{
try{
  String xml=dataPipeline.getString("xml");
  String tokens[]=xml.split(Pattern.quote("?>"));
  if(tokens.length>1)
  	xml=tokens[1];
  Map<String,Object> map=ServiceUtils.xmlToMap("<root>"+xml+"</root>");
  //dataPipeline.log(jsonString);
  //System.out.println(map);
  dataPipeline.put("output",map);
  //dataPipeline.logDataPipeline();
}catch(Exception e){
	dataPipeline.clear();
  	dataPipeline.put("error",e.getMessage());
  	new SnippetException(dataPipeline,"Sneppet exception", new Exception(e));
}
	}

}