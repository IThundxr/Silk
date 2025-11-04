plugins {
    id("java")
    kotlin("jvm") version "2.2.21"
    id("com.gradle.plugin-publish") version "2.0.0"
}

group = "dev.ithundxr"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
}

tasks.test {
    useJUnitPlatform()
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
            tags.set(listOf("minecraft"))
        }
    }
}

val functionalTestSourceSet = sourceSets.create("functionalTest")

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}