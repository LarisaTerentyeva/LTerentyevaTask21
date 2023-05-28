package org.example.utils.config;
import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ServiceConfig extends Config {
    @Key("login_url")
    String loginUrl();

    @Key("api_test_url")
    String apiUrl();

    @Key("api_base_path")
    String basePath();

    @Key("client_id")
    String clientId();

}