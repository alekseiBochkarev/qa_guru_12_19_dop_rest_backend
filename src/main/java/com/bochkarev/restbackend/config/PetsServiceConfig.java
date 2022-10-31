package com.bochkarev.restbackend.config;
import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:application.properties"
})
public interface PetsServiceConfig extends Config{
    @Key("remote-host")
    String remote_host();

    @Key("application-host")
    String application_host();

    @Key("server.port")
    String application_port();
}
