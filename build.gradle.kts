plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "net.polar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.3")
    implementation("ch.qos.logback:logback-classic:1.2.8")
    implementation("it.unimi.dsi:fastutil:8.5.11")
    implementation("com.github.JCTools:JCTools:-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.mongodb:mongodb-driver-sync:4.1.1")
}


tasks {
    shadowJar {
        archiveClassifier.set("")
        manifest {
            attributes(
                "Main-Class" to "net.polar.PolarBot"
            )
        }
    }
    compileJava {
        options.encoding = "UTF-8"
    }
}