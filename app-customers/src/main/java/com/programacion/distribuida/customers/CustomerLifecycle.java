package com.programacion.distribuida.customers;

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

@ApplicationScoped
public class CustomerLifecycle {

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "127.0.0.1")
    String consulHost;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    Integer consulPort;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    String serviceId;

    void init(@Observes StartupEvent event, Vertx vertx) throws Exception {
        System.out.println("Iniciando servicio customersss...");

        ConsulClientOptions options = new ConsulClientOptions()
                .setHost(consulHost)
                .setPort(consulPort);

        ConsulClient consulClient = ConsulClient.create(vertx, options);

        serviceId = UUID.randomUUID().toString();
        var ipAddress = InetAddress.getLocalHost();

        // Listo los tags que voy a usar
        var tags = List.of(
                "traefik.enable=true",
                "traefik.http.routers.app-customers.rule=PathPrefix(`/app-customers`)",
                "traefik.http.routers.app-customers.middlewares=strip-prefix-customers",
                "traefik.http.middlewares.strip-prefix-customers.stripprefix.prefixes=/app-customers"
        );

        var checkOptions = new CheckOptions()
//                .setHttp("http://127.0.0.1:7070/ping") // Esto estatico
                .setHttp(String.format(("http://%s:%s/ping"), ipAddress.getHostAddress(), appPort))
                .setInterval("10s")
                .setDeregisterAfter("20s");

        //--registro
        ServiceOptions serviceOptions = new ServiceOptions()
                .setName("app-customers")
                .setId(serviceId)
                .setAddress(ipAddress.getHostAddress())
                .setPort(appPort)
                .setTags(tags)
                .setCheckOptions(checkOptions);

        consulClient.registerServiceAndAwait(serviceOptions);
    }

    void stop(@Observes ShutdownEvent event, Vertx vertx) {
        System.out.println("Deteniendo servicio customersss...");

        ConsulClientOptions options = new ConsulClientOptions()
                .setHost(consulHost)
                .setPort(consulPort);
        ConsulClient consulClient = ConsulClient.create(vertx, options);

        consulClient.deregisterServiceAndAwait(serviceId);

    }
}
