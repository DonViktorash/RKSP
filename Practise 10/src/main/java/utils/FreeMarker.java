package utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

public class FreeMarker {
    private static final String TEMPLATES_PATH =
            "C:/Users/alyau/Desktop/РКСП/Исходники/PR_10/src/main/templates";
    private static final Configuration cfg = configureFreeMarker();

    private static Configuration configureFreeMarker() {
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
            configuration.setDirectoryForTemplateLoading(
                    new File(TEMPLATES_PATH)
            );
            return configuration;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHtml(String name, Map<String, Object> root) {
        try {
            Template template = cfg.getTemplate(name + ".ftl");
            StringWriter stringWriter = new StringWriter();
            template.process(root, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
