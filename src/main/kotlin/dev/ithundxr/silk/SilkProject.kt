package dev.ithundxr.silk

import dev.ithundxr.silk.api.SilkGradleExtension
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.gradle.api.Project

class SilkProject(private val project: Project): Project by project {
    val git: JGitWrapper? by lazy {
        try {
            JGitWrapper(Git.open(rootDir))
        } catch (e: RepositoryNotFoundException) {
            null
        }
    }

   val extension: SilkGradleExtension
        get() = project.extensions.getByType(SilkGradleExtension::class.java)
}
