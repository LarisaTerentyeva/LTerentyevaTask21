package org.example.utils.config;
import org.aeonbits.owner.Config;

public interface UserConfig extends Config {

    @DefaultValue("api-user4@iwt.net")
    String user();

    @DefaultValue("b3z0nV0cLO")
    String password();

}