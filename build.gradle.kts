plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.kkuzmin.drugstoremachine"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.springframework.boot:spring-boot:3.5.9")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.9")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // Jackson extensions for Kotlin for working with JSON
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin reflection library, required for working with Spring

    testImplementation("org.spockframework:spock-core:2.4-groovy-4.0")
    testImplementation("org.codehaus.groovy:groovy:4.0.16")
    testImplementation("org.mockito:mockito-core:5.5.0")

    // Byte-buddy for runtime proxy generation (required for spying on concrete classes)
    testImplementation("net.bytebuddy:byte-buddy:1.14.8")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}