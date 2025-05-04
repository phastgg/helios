plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
}

group = "gg.phast"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("helios") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "phastgg"
            url = uri("https://repo.phast.gg/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME") ?: property("mavenUser").toString()
                password = System.getenv("MAVEN_SECRET") ?: property("mavenPassword").toString()
            }
        }
    }
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) options.release = targetJavaVersion
}
