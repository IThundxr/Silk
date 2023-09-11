package dev.ithundxr.silk

import dev.ithundxr.silk.api.PublishingConfiguration
import dev.ithundxr.silk.api.SilkGradleExtension
import dev.ithundxr.silk.api.SilkRepositoryHandler
import dev.ithundxr.silk.helpers.MavenHelper
import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.UnknownTaskException
import org.gradle.api.tasks.TaskProvider
import org.gradle.configurationcache.extensions.capitalized
import java.io.File

open class SilkGradleExtensionImpl(private val project: SilkProject) : SilkGradleExtension {
    override var changelogFile: File by defaulted { project.file("changelog.md") }

    override var modVersion: String by defaulted { project.version.toString() }

    override var displayName: String by defaulted {
        project.name.split("-").joinToString(" ") { it.capitalized() }
    }

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
            var ithundxrMaven: MavenHelper.IThundxrMaven? = null

            override var mainArtifact: Any = project.tasks.named("remapJar")

            override fun withIThundxrMaven(snapshot: Boolean) {
                ithundxrMaven = if (snapshot) MavenHelper.IThundxrMaven.SNAPSHOTS else MavenHelper.IThundxrMaven.RELEASES
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
    }
}
