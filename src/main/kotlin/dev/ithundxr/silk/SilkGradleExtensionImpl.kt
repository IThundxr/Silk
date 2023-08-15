package dev.ithundxr.silk

import dev.ithundxr.silk.api.PublishingConfiguration
import dev.ithundxr.silk.api.SilkGradleExtension
import dev.ithundxr.silk.api.SilkRepositoryHandler
import dev.ithundxr.silk.helpers.CurseGradleHelper
import dev.ithundxr.silk.helpers.MavenHelper
import dev.ithundxr.silk.helpers.ModrinthHelper
import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.UnknownTaskException
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.configurationcache.extensions.capitalized
import java.io.File
import java.net.URL

open class SilkGradleExtensionImpl(private val project: SilkProject) : SilkGradleExtension {
    override var loader: String? by defaulted { null }

    override var loom: String by defaulted { "null" } withListener { value ->
        when (value) {
            "fabric" -> {
                project.dependencies.add("implementation",
                    "fabric-loom:1.2-SNAPSHOT")
                project.pluginManager.apply("org.quiltmc.loom")
            }
            "quilt" -> {
                project.dependencies.add("implementation",
                    "org.quiltmc.loom:loom-plugin:1.+")
                project.pluginManager.apply("org.quiltmc.loom")
            }
            "architectury" -> {
                project.dependencies.add("implementation",
                    "dev.architectury.loom:loom-plugin:1.1.+")
                project.pluginManager.apply("dev.architectury.loom")
            }
            "null" -> println("No loom selected")
            else -> throw IllegalArgumentException("Unsupported loom value: $value")
        }
    }

    override var changelogFile: File by defaulted { project.file("changelog.md") }

    override var javaVersion: Int by defaulted { 17 } withListener { value ->
        project.tasks.withType(JavaCompile::class.java).configureEach {
            it.options.release.set(value)
        }
    }

    override var modVersion: String by defaulted { project.version.toString() }

    override var displayName: String by defaulted {
        project.name.split("-").joinToString(" ") { it.capitalized() }
    }

    override var owners: String by defaulted {
        project.group.toString().split('.').last().capitalized()
    }

    override var github: URL? by defaulted { URL("https://github.com/$owners/$displayName") }

    override var changelogUrl: URL? by defaulted { URL("$github/blob/$modVersion/changelog.md") }

    override val repositories: SilkRepositoryHandler
        get() = SilkRepositoryHandlerImpl(this.project.repositories)

    override fun repositories(action: Action<SilkRepositoryHandler>) {
        action.execute(this.repositories)
    }

    override fun repositories(action: SilkRepositoryHandler.() -> Unit) {
        this.repositories.action()
    }

    override fun configurePublishing(action: Action<PublishingConfiguration>) {
        val cfg = object: PublishingConfiguration {
            var curseforge = false
            var modrinth = false
            var ithundxrMaven: MavenHelper.IThundxrMaven? = null

            override var mainArtifact: Any = project.tasks.named("remapJar")

            override fun withIThundxrMaven(snapshot: Boolean) {
                ithundxrMaven = if (snapshot) MavenHelper.IThundxrMaven.SNAPSHOTS else MavenHelper.IThundxrMaven.RELEASES
            }

            override fun withCurseforgeRelease() {
                curseforge = true
            }

            override fun withModrinthRelease() {
                modrinth = true
            }
        }

        action.execute(cfg)

        MavenHelper.configureDefaults(project, cfg.ithundxrMaven)

        val checkGitStatus: TaskProvider<CheckGitTask> =
            project.tasks.register("checkGitStatus", CheckGitTask::class.java, project)

        val release: TaskProvider<Task> = project.tasks.register("release") {
            it.group = "publishing"
            it.description = "Releases a new version to Maven, Curseforge and Modrinth"
            it.dependsOn(checkGitStatus)
        }

        fun configureReleaseSubtask(name: String) {
            try {
                val subtask = project.tasks.named(name)
                subtask.configure { it.mustRunAfter(checkGitStatus) }
                release.configure { it.dependsOn(subtask) }
            } catch (_: UnknownTaskException) {
                release.configure {
                    it.doFirst { project.logger.warn("Task $name not found; skipping it for release") }
                }
            }
        }

        if (cfg.ithundxrMaven != null) {
            configureReleaseSubtask("publish")
        }

        if (cfg.curseforge) {
            CurseGradleHelper.configureDefaults(project, cfg.mainArtifact)
            configureReleaseSubtask("curseforge")
        }

        if (cfg.modrinth) {
            ModrinthHelper.configureDefaults(project, cfg.mainArtifact)
            configureReleaseSubtask("modrinth")
        }
    }
}
