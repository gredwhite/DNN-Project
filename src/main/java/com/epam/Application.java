package com.epam;


import com.epam.web.config.WebInitializer;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.ClassInheritanceHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.WebApplicationInitializer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    public static void main(String[] args) throws Exception
    {
        new Application(Arrays.asList(args));
    }

    Application(List args) throws Exception {
        int port = 8080;
        Server server = new Server(port);

        WebAppContext webAppContext = new WebAppContext();

        webAppContext.setContextPath("/");
        webAppContext.setSessionHandler(new SessionHandler());
        webAppContext.setConfigurations(new Configuration[]
                {
                    new AnnotationConfiguration() {
                    /**
                     * Jetty Annotation configuration will inspect JAR context, looking for Initializers.
                     * But, if we,re going to execute app in exploded mode (without creating a JAR), Jetty won't
                     * find any initializers. Let's put our initializer manually.
                     * @param context
                     * @throws Exception
                     */
                    @Override
                    public void preConfigure(WebAppContext context) throws Exception {
                        ConcurrentHashMap<String, ConcurrentHashSet<String>> inheritanceMap =
                                new ClassInheritanceMap();
                        ConcurrentHashSet<String> value = new ConcurrentHashSet<String>();
                        value.add(WebInitializer.class.getName());
                        inheritanceMap.put(WebApplicationInitializer.class.getName(), value);
                        context.setAttribute(CLASS_INHERITANCE_MAP, inheritanceMap);
                        _classInheritanceHandler = new ClassInheritanceHandler(inheritanceMap);
                    }
                }
                });

        webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");

        webAppContext.setResourceBase(new ClassPathResource(".").getURI().getPath());

        server.setHandler(webAppContext);
        server.start();
        server.join();
    }
}
