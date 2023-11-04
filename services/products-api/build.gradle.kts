plugins {
    id("java")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes(
            "Main-Class" to "com.shoplife.ApplicationEntryPoint"
        )
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
}


group = "com.shoplife"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.4.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.apache.kafka:kafka-clients:3.4.0")
    implementation("org.postgresql:postgresql:42.5.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")

}

