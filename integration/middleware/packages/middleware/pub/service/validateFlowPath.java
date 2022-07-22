package packages.middleware.pub.service;
import com.eka.middleware.service.DataPipeline;
import com.eka.middleware.service.ServiceUtils;
import com.eka.middleware.template.SnippetException;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import com.eka.middleware.server.ServiceManager;

public final class validateFlowPath{
	public static final void main(DataPipeline dataPipeline) throws SnippetException{
try {
		String serviceFqn=dataPipeline.getString("serviceFqn");
        serviceFqn = StringUtils.replace(serviceFqn, ".", File.separator);
        String packageDirectory= ServiceManager.packagePath + serviceFqn + ".flow";
        File file=new File(packageDirectory );
  		
        //dataPipeline.log("***********************************************\n"+list);
        dataPipeline.put("exist", file.exists());
  } catch (Exception e) {
		dataPipeline.clear();
  		dataPipeline.put("error",e.getMessage());
    	throw new SnippetException(dataPipeline,"Snippet exception", new Exception(e));
  }



	}

}