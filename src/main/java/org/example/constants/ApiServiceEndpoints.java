package org.example.constants;

import org.aeonbits.owner.ConfigFactory;
import org.example.utils.config.ServiceConfig;

public class ApiServiceEndpoints {
    private ApiServiceEndpoints() {
        throw new IllegalStateException("Constants class");
    }

    private static final String VERSION = ConfigFactory.create(ServiceConfig.class).basePath();
    private static final String LOGIN = ConfigFactory.create(ServiceConfig.class).loginUrl();

    public static final String GET_TOKEN = LOGIN + "v1/token";
    public static final String PASSENGER = VERSION + "passenger";
    public static final String PASSENGER_BY_ID = VERSION + "passenger/%s";

}