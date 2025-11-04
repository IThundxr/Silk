/*
 * Silk
 * Copyright (c) 2025 IThundxr
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.ithundxr.silk.impl

import dev.ithundxr.silk.SilkProject
import dev.ithundxr.silk.api.SilkGradleExtension
import dev.ithundxr.silk.defaulted
import java.io.File

open class SilkGradleExtensionImpl(private val project: SilkProject) : SilkGradleExtension {
    override var changelogFile: File by defaulted { project.file("changelog.md") }
    override var changelogUrl: String? by defaulted { null }
    override val changelog: CharSequence get() = project.changelog.get()

    override var modVersion: String by defaulted { project.version.toString() }
}