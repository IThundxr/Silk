package dev.ithundxr.silk

import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.concurrent.Callable
import kotlin.io.path.absolute
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.useLines
import kotlin.io.path.writeText

class ChangelogText(project: SilkProject): Callable<CharSequence> {
    companion object {
        private const val SEPARATOR = "---"
        private val logger = LoggerFactory.getLogger(ChangelogText::class.java)
    }

    private val text by lazy {
        logger.info("Resolving changelog")

        val changelogFile = project.extension.changelogFile.toPath()

        if (!changelogFile.exists()) {
            println("No changelog file found, creating one at \"${changelogFile.absolute()}\"")
            try {
                changelogFile.parent.createDirectories()
                changelogFile.createFile()
                changelogFile.writeText(
                    """
                        |------------------------------------------------------
                        |Version ${project.extension.modVersion}
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
            return@lazy ""
        }
        val changelog = changelogFile.useLines { lines ->
            sequence {
                val iterator = lines.iterator()
                if (!iterator.hasNext() || !iterator.next()
                        .startsWith(SEPARATOR)
                ) throw IllegalStateException("Malformed changelog: expected separator \"${SEPARATOR}\" on line 1")
                if (!iterator.hasNext()) throw IllegalStateException("Malformed changelog: expected version name on line 2")
                yield("${iterator.next()}:\n")
                if (!iterator.hasNext() || !iterator.next()
                        .startsWith(SEPARATOR)
                ) throw IllegalStateException("Malformed changelog: expected separator \"${SEPARATOR}\" on line 3")
                if (!iterator.hasNext()) throw IllegalStateException("Malformed changelog: expected description starting at line 4")
                yieldAll(iterator)
            }.takeWhile { line ->
                !line.startsWith(SEPARATOR)
            }.joinToString("\n|")
        }

        """
        |$changelog
        """.trimMargin()
    }

    override fun toString(): String {
        return text
    }

    fun asString(): String {
        return text
    }

    override fun call(): CharSequence = this.text
}
