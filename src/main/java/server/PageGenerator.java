package server;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class PageGenerator {
    private static final String STATIC_DIR = "static/html";
    private static final String TEMPLATE_DIR = "static/tml";
    private static final Configuration CFG = new Configuration();
    static {
        CFG.setDefaultEncoding("UTF-8");
        CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static String getStaticPage(String filename) {
        Map<String, Object> emptyMap = new HashMap<>();

        return getPage(STATIC_DIR, filename, emptyMap);
    }

    public static String getTemplatePage(String filename, Map<String, Object> data) {
        return getPage(TEMPLATE_DIR, filename, data);
    }

    private static String getPage(String dir, String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = CFG.getTemplate(dir + File.separator + filename);
            template.process(data, stream);
        }
        catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}
