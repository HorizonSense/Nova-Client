plugins {
    id("java-library")
    kotlin("jvm")
}

group = "com.radiantbyte.novarelay"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)

    implementation(platform(libs.log4j.bom))
    implementation(libs.log4j.api)
    implementation(libs.log4j.core)
    implementation(libs.jose4j)
    implementation(libs.jackson.databind)
    implementation(libs.kotlinx.coroutines.core)
    
    // Use api to expose these to the app module
    api(libs.minecraft.auth)
    api("org.cloudburstmc.protocol:bedrock-connection:3.0.0.Beta12-SNAPSHOT")
    api(libs.bundles.netty)
    
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
