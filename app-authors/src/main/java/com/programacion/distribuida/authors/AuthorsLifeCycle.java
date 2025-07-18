package com.programacion.distribuida.authors;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

@ApplicationScoped // Con esto hago q sea un componente CDI
public class AuthorsLifeCycle {

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "8500")
    String consulHost;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "127.0.0.1")
    Integer consulPort;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    String serviceId;

    // Necesitamos escuchar 2 eventos: Inicializacion y cuando se detiene
    // Cuando arranca
    void init(@Observes StartupEvent event, Vertx vertx) throws Exception {
        try {
            System.out.println("Iniciando servicio de autoresss...");

            // Obtenemos host y puerto de la configuracion
            ConsulClientOptions options = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient consulClient = ConsulClient.create(vertx, options);

            serviceId = UUID.randomUUID().toString();
            var ipAddress = InetAddress.getLocalHost();

            // Listo los tags que voy a usar
            var tags = List.of(
                    "traefik.enable=true",
                    "traefik.http.routers.app-authors.rule=PathPrefix(`/app-authors`)",
                    "traefik.http.routers.app-authors.middlewares=strip-prefix-authors",
                    "traefik.http.middlewares.strip-prefix-authors.stripprefix.prefixes=/app-authors"
            );

            var checkOptions = new CheckOptions()
//                .setHttp("http://127.0.0.1:8080/ping") // Esto estatico
                    .setHttp(String.format(("http://%s:%s/ping"), ipAddress.getHostAddress(), appPort))
                    .setInterval("10s")
                    .setDeregisterAfter("20s");

            ServiceOptions serviceOptions = new ServiceOptions()
                    .setName("app-authors")
                    .setId(serviceId)
                    .setAddress(ipAddress.getHostAddress())
                    .setPort(appPort)
                    .setTags(tags)
                    .setCheckOptions(checkOptions);

            consulClient.registerServiceAndAwait(serviceOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cuando se detiene
    void stop(@Observes ShutdownEvent event, Vertx vertx) throws Exception {
        try {
            System.out.println("Deteniendo servicio de autoresss...");

            ConsulClientOptions options = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient consulClient = ConsulClient.create(vertx, options);

            consulClient.deregisterServiceAndAwait(serviceId);
        } catch (Exception e) {
            System.out.println("Deteniendo servicio de autoresss...");

            ConsulClientOptions options = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient consulClient = ConsulClient.create(vertx, options);

            consulClient.deregisterServiceAndAwait(serviceId);
        }
    }
}