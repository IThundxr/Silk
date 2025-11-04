/*
 * Chenille
 * Copyright (C) 2022-2025 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */

package dev.ithundxr.silk

import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.io.path.*

class ChangelogText(project: SilkProject) {
    companion object {
        private const val SEPARATOR = "---"
        private val logger = LoggerFactory.getLogger(ChangelogText::class.java)
    }

    private val text by lazy {
        logger.info("Resolving changelog")

        val changelogFile = project.extension.changelogFile.toPath()

        if (!changelogFile.exists()) {
            logger.warn("No changelog file found, creating one at \"${changelogFile.absolute()}\"")
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
                logger.error("Unable to write changelog file: " + e.message)
                e.printStackTrace()
            }
            return@lazy ""
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

        """
        |$changelog
        |
        |
        | see full changelog [here](${project.extension.changelogUrl ?: return@lazy changelog} "Changelog")
        """.trimMargin()
    }

    override fun toString(): String = this.text

    fun get(): CharSequence = this.text
}