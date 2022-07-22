package packages.middleware.pub.string;
import com.eka.middleware.service.DataPipeline;
import com.eka.middleware.service.ServiceUtils;
import com.eka.middleware.template.SnippetException;
public final class escapeSQL{
	public static final void main(DataPipeline dataPipeline) throws SnippetException{
try {
  
  String input = dataPipeline.getString("input");
  dataPipeline.put("output", input.replaceAll("'", "\""));
  
} catch (Exception e) {
  throw new SnippetException(dataPipeline, "packages.middleware.pub.string.patternQuote" , e);
}

	}

}