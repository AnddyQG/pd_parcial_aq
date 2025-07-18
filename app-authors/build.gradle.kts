plugins {
    id("java")
    id("io.freefair.lombok") version "8.13.1"
    id("io.quarkus") version "3.22.2"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

val quarkusVersion = "3.22.2"

// Compatibilidad (compilar con JDK 21)
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}"))

    // Contenedor CDI
    implementation("io.quarkus:quarkus-arc") //Usa CDI 4.1

    // Contenedor de REST
    implementation("io.quarkus:quarkus-rest") //Es implmentacion de jax rs
    implementation("io.quarkus:quarkus-rest-jsonb")

    // Contenedor de JPA
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-postgresql")

    // Control versiones base de datos
    implementation("io.quarkus:quarkus-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Service discovery
    implementation("io.quarkus:quarkus-smallrye-stork")
    implementation("io.smallrye.stork:stork-service-discovery-consul")
    implementation("io.smallrye.reactive:smallrye-mutiny-vertx-consul-client")

    // Telemetria: metricas
    // Metricas se exponen en: http://localhost:8080/q/metrics
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-jackson")


    //health check
    implementation("io.quarkus:quarkus-smallrye-health")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}