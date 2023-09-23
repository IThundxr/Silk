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

val cursegradle_version: String by project
val jgit_version: String by project
val minotaur_version: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    implementation("org.eclipse.jgit:org.eclipse.jgit:$jgit_version")
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
