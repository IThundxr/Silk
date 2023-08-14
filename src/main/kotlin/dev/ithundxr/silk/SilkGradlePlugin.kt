package dev.ithundxr.silk

import dev.ithundxr.silk.api.SilkGradleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer

@Suppress("unused") // Plugin entrypoint duh
class SilkGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (!target.pluginManager.hasPlugin("fabric-loom")) {
            target.pluginManager.apply("org.quiltmc.loom")
        }

        val project = SilkProject(target)

        project.extensions.create(SilkGradleExtension::class.java, "silk", SilkGradleExtensionImpl::class.java, project)

        setupConfigurations(project.configurations)
    }

    private fun setupConfigurations(configurations: ConfigurationContainer) {
        configurations.register("modIncludeImplementation") {
            configurations.getByName("modImplementation").extendsFrom(it)
            configurations.getByName("include").extendsFrom(it)
        }
        configurations.register("modIncludeApi") {
            configurations.getByName("modApi").extendsFrom(it)
            configurations.getByName("include").extendsFrom(it)
        }
        configurations.register("modLocalImplementation") {
            configurations.getByName("modCompileOnly").extendsFrom(it)
            configurations.getByName("modLocalRuntime").extendsFrom(it)
        }
    }
}
