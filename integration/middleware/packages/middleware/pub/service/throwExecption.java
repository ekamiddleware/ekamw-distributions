package packages.middleware.pub.service;
import com.eka.middleware.service.DataPipeline;
import com.eka.middleware.service.ServiceUtils;
import com.eka.middleware.template.SnippetException;
public final class throwExecption{
	public static final void main(DataPipeline dataPipeline) throws SnippetException{
String msg=dataPipeline.getString("msg");
if(msg==null)
  msg="Default exception";
throw new SnippetException(dataPipeline,"From Service: " + msg, new Exception(msg));
	}

}