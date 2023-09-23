package dev.ithundxr.silk

import org.gradle.api.Project
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.useLines
import kotlin.io.path.writeText

class ChangelogTextNew {
    companion object {
        private const val SEPARATOR = "---"
        private val logger = LoggerFactory.getLogger(ChangelogTextNew::class.java)

        @JvmStatic
        fun getChangelogText(project: Project, changelogFilePath: String): CharSequence {
            logger.info("Resolving changelog")

            val changelogFile = project.file(changelogFilePath).toPath()

            if (!changelogFile.exists()) {
                println("No changelog file found, creating one at \"${changelogFile.toFile().absolutePath}\"")
                try {
                    changelogFile.parent.createDirectories()
                    changelogFile.toFile().createNewFile()
                    changelogFile.writeText(
                        """
                        |------------------------------------------------------
                        |Version 1.0.0
                        |------------------------------------------------------
                        |Additions
                        |- None
                        |
                        |Changes
                        |- None
                        |
                        |Bug Fixes
                        |- None
                        |
                        """.trimMargin()
                    )
                } catch (e: IOException) {
                    println("Unable to write changelog file: " + e.message)
                    e.printStackTrace()
                }
                return ""
            }
            val changelog = changelogFile.useLines { lines ->
                sequence {
                    val iterator = lines.iterator()
                    if (!iterator.hasNext() || !iterator.next().startsWith(SEPARATOR))
                        throw IllegalStateException("Malformed changelog: expected separator \"${SEPARATOR}\" on line 1")
                    if (!iterator.hasNext()) throw IllegalStateException("Malformed changelog: expected version name on line 2")
                    yield("${iterator.next()}:\n")
                    if (!iterator.hasNext() || !iterator.next().startsWith(SEPARATOR))
                        throw IllegalStateException("Malformed changelog: expected separator \"${SEPARATOR}\" on line 3")
                    if (!iterator.hasNext()) throw IllegalStateException("Malformed changelog: expected description starting at line 4")
                    yieldAll(iterator)
                }.takeWhile { line ->
                    !line.startsWith(SEPARATOR)
                }.joinToString("\n|")
            }

            return """
            |$changelog
            """.trimMargin()
        }
    }
}
