package dev.ithundxr.silk

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.gradle.api.Project

class SilkProject(private val project: Project): Project by project {
   val git: JGitWrapper? by lazy {
        try { JGitWrapper(Git.open(rootDir)) } catch (e: RepositoryNotFoundException) { null }
   }
}
