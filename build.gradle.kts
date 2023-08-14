import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    groovy
    java
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.0"
}

group = "dev.ithundxr"
version = project.properties["version"]!!

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    implementation("me.modmuss50:mod-publish-plugin:${project.properties["mod-publish_version"]}")
    implementation("org.eclipse.jgit:org.eclipse.jgit:${project.properties["jgit_version"]}")
}

java {
    withSourcesJar()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-Xjvm-default=all")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    maxHeapSize = "5G"
}

gradlePlugin {
    website.set("https://ithundxr.dev")
    vcsUrl.set("https://github.com/IThundxr/silk")
    plugins {
        create("silk") {
            id = "dev.ithundxr.silk"
            displayName = "Silk"
            description = "Helper plugin for Minecraft mods"
            implementationClass = "dev.ithundxr.silk.SilkGradlePlugin"
            tags.set(listOf("fabricmc", "minecraft", "loom", "fabric-loom", "quilt-loom"))
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}
