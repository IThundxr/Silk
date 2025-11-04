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

package dev.ithundxr.silk

import dev.ithundxr.silk.api.SilkGradleExtension
import dev.ithundxr.silk.impl.SilkGradleExtensionImpl
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class SilkGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            SilkGradleExtension::class.java,
            "silk",
            SilkGradleExtensionImpl::class.java,
            SilkProject(target)
        )
    }
}