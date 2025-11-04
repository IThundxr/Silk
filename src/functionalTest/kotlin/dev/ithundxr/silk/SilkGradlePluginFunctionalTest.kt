package dev.ithundxr.silk

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SilkGradlePluginFunctionalTest {
    @field:TempDir
    lateinit var projectDir: File

    private val settingsFile by lazy { projectDir.resolve("settings.gradle.kts") }
    private val buildFile by lazy { projectDir.resolve("build.gradle.kts") }
    private val changelogFile by lazy { projectDir.resolve("myChangelog.md") }

    @Test
    fun testChangelog() {
        settingsFile.writeText("")
        buildFile.writeText(
            """
            plugins {
                id("dev.ithundxr.silk")
            }
            
            silk {
                changelogFile = project.file("myChangelog.md")
            }
            
            tasks.register("printChangelog") {
                doLast {
                    println(">>> CHANGELOG OUTPUT <<<")
                    println(silk.changelog)
                }
            }
            """.trimIndent()
        )
        changelogFile.writeText(
            """
            ------------------------------------------------------
            Version 1.0.0
            ------------------------------------------------------
            Additions
                - None
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("printChangelog")
            .forwardOutput()
            .build()

        val task = result.task(":printChangelog")
        assertEquals(TaskOutcome.SUCCESS, task?.outcome)
        assertTrue(
            result.output.contains(
                """
            >>> CHANGELOG OUTPUT <<<
            Version 1.0.0:

            Additions
                - None
            """.trimIndent()
            )
        )
    }
}