val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val hikari_version : String by project
val postgres_version : String by project

plugins {
    application
    kotlin("jvm") version "1.7.21"
    id("io.ktor.plugin") version "2.1.3"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.7.21"
}
kotlin{
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}
tasks.register<Copy>("stage") {
    // Copy the built jar file to the stage directory
    from(tasks.getByName("build").outputs)
    into("${buildDir}/stage")
}

tasks.named("build") {
    // Ensure that the jar is built before staging
    finalizedBy("stage")
}



group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation ("org.postgresql:postgresql:$postgres_version")
    implementation( "com.zaxxer:HikariCP:$hikari_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-cio-jvm:2.1.3")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.konform:konform-jvm:0.4.0")
}