package dev.ithundxr.silk


import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

class SilkBaseFunctionalTest extends Specification {
    @TempDir File testProjectDir
    File buildFile
    File propertiesFile

    def setup() {
        buildFile = new File(testProjectDir, 'build.gradle')
        buildFile << """
            plugins {
                id 'dev.ithundxr.silk'
            }

            dependencies {
                minecraft "com.mojang:minecraft:1.18.2"
                mappings "net.fabricmc:yarn:1.18.2+build.1:v2"
                modImplementation "net.fabricmc:fabric-loader:0.13.3"
            }
        """
        propertiesFile = new File(testProjectDir, "gradle.properties")

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("--stacktrace")
            .withDebug(true)
            .build()

        then:
        !result.output.isEmpty()
    }
}
