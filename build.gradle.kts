plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
}

group = "ru.DmN.siberia"
version = "1.6.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
    testImplementation(kotlin("test"))
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    build {
        dependsOn(register<Jar>("fatJar") {
            dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
            archiveClassifier.set("standalone") // Naming the jar
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            manifest { attributes(mapOf("Main-Class" to "ru.DmN.pht.base.ConsoleOld")) } // Provided we set it up in the application plugin configuration
            val sourcesMain = sourceSets.main.get()
            val contents = configurations.runtimeClasspath.get()
                .map { if (it.isDirectory) it else zipTree(it) } +
                    sourcesMain.output
            from(contents)
        })
    }

    java {
        withSourcesJar()
    }

    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "Project-Siberia"
            version = project.version as String
            from(components["java"])
        }
    }
}