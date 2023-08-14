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

val functionalTest: SourceSet by sourceSets.creating

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "Fabric"
        setUrl("https://maven.fabricmc.net")
    }
    maven {
        name = "Quilt"
        setUrl("https://maven.quiltmc.org/repository/release")
    }
}

val cursegradle_version: String by project
val loom_version: String by project
val jgit_version: String by project
val minotaur_version: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    implementation("gradle.plugin.com.matthewprenger:CurseGradle:$cursegradle_version")
    implementation("org.quiltmc:loom:$loom_version")
    implementation("org.eclipse.jgit:org.eclipse.jgit:$jgit_version")
    implementation("com.modrinth.minotaur:Minotaur:$minotaur_version")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    "functionalTestImplementation"(platform("org.spockframework:spock-bom:2.0-groovy-3.0"))
    "functionalTestImplementation"("org.spockframework:spock-core")
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

val functionalTestTask = tasks.register<Test>("functionalTest") {
    description = "Runs the functional tests."
    group = "verification"
    testClassesDirs = functionalTest.output.classesDirs
    classpath = functionalTest.runtimeClasspath
    mustRunAfter(tasks.test)
}

tasks.check {
    dependsOn(functionalTestTask)
}

gradlePlugin {
    website.set("https://ithundxr.dev")
    vcsUrl.set("https://github.com/IThundxr/silk")
    testSourceSets(functionalTest)
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
