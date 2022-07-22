package packages.middleware.pub.server.core;
import com.eka.middleware.service.DataPipeline;
import com.eka.middleware.service.RuntimePipeline;
import com.eka.middleware.service.ServiceUtils;
import com.eka.middleware.template.SnippetException;
import com.eka.middleware.template.SystemException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.*;
import java.util.UUID;
public final class getStartUpServices{
	public static final void main(DataPipeline dataPipeline) throws SnippetException{
try {
            String pathname = ServiceUtils.getServerProperty("middleware.server.home.dir") + "packages" + File.separator;
            String[] str = new File(pathname).list();
            Set<String> ignorableItems = new HashSet<>();
            ignorableItems.add("global");
            List<Map<String, String>> servicesColl = new ArrayList<>();
            for (String fileName: str) {
                if (!ignorableItems.contains(fileName)) {
                    File config = new File(pathname + fileName + File.separator + "dependency" + File.separator + "config");
                    if (config.isDirectory()) {
                        try {
                            FileReader fileReader = new FileReader(config.getAbsolutePath() + File.separator + "package.properties");
                            Properties properties = new Properties();
                            properties.load(fileReader);

                            String[] services = properties.get("org.eka.middleware.service.startup").toString().split(",");
                            for (String service: services) {
                                try {
                                    //String uuid = UUID.randomUUID().toString();
                                    //RuntimePipeline rp = RuntimePipeline.create(uuid, uuid, null, "GET/execute/" + service + ".main",
                                      //      "/execute/" + service + ".main");
                                    //rp.dataPipeLine.apply(service);
                                  Map<String, String> map = new HashMap<>();
                                  map.put("fqn", service);
                                    servicesColl.add(map);
                                } catch (Exception e) {
                                    throw new SystemException("EKA_MWS_1008", e);
                                }
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
  			
            dataPipeline.put("services", servicesColl);
        } catch (Exception e) {
            throw new SnippetException(dataPipeline, e.getMessage(), e);
        }
	}

}