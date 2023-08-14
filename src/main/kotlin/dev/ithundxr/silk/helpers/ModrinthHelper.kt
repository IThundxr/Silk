package dev.ithundxr.silk.helpers

import com.modrinth.minotaur.ModrinthExtension
import dev.ithundxr.silk.SilkProject

@Deprecated("Deprecated")
internal object ModrinthHelper {
    fun configureDefaults(project: SilkProject, mainArtifact: Any) {
        project.pluginManager.apply("com.modrinth.minotaur")

        project.extensions.configure(ModrinthExtension::class.java) { ext ->
            if (project.hasProperty("modrinth_api_key")) {
                ext.token.set(project.findProperty("modrinth_api_key")!!.toString())
            } else {
                println("Modrinth API Key not configured; please define the 'modrinth_key' user property before release")
                return@configure
            }
            if (project.hasProperty("modrinth_id")) {
                ext.projectId.set(project.findProperty("modrinth_id")!!.toString())
                ext.versionNumber.set(project.version.toString())
                ext.uploadFile.set(mainArtifact)
                ext.changelog.set(project.providers.provider(project.changelog).map { it.toString() })
                "${project.findProperty("curseforge_versions")}".split("; ").forEach {
                    ext.gameVersions.add(it)
                }
                if (project.isFabricMod) {
                    ext.loaders.add("fabric")
                }
                ext.loaders.add("quilt")
            } else {
                println("Modrinth Project ID not configured; please define the 'modrinth_id' project property before release")
            }
        }
    }
}
