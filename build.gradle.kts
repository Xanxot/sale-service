val springBootVersion = project.findProperty("springBootVersion") as String
val flyWayVersion = project.findProperty("flyWayVersion") as String
val postgresqlVersion = project.findProperty("postgresqlVersion") as String
val lombokVersion = project.findProperty("lombokVersion") as String
val springdocVersion = project.findProperty("springdocVersion") as String
val prometheusVersion = project.findProperty("prometheusVersion") as String
val h2Version = project.findProperty("h2Version") as String

plugins {
    id("java")
    id("io.freefair.lombok") version "8.10.2"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.company"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${springBootVersion}")

    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.flywaydb:flyway-core:${flyWayVersion}")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:${flyWayVersion}")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${springdocVersion}")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("io.micrometer:micrometer-registry-prometheus:${prometheusVersion}")

    implementation("com.h2database:h2:${h2Version}")

}

tasks.getByName<Jar>("bootJar") {
    archiveBaseName.set("sales-service")
    archiveVersion.set(version.toString())
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    dependsOn(tasks.bootJar)
}
