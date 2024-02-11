/*
package org.anuran.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.inject.Inject;
import jakarta.enterprise.event.Observes;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;


public class CorsConfig {

    @Inject
    CorsFilter corsFilter;

    void onStart(@Observes StartupEvent event) {

        corsFilter.setAllowedMethods("OPTIONS,GET,POST,PUT,DELETE,HEAD");
        corsFilter.setAllowedHeaders("Content-Type,Authorization");
        corsFilter.setExposedHeaders("Content-Type,Authorization");
        corsFilter.setAllowCredentials(true);
    }
}*/
