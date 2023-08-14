package dev.ithundxr.silk

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class CheckGitTask @Inject constructor(private val project: SilkProject): DefaultTask() {
    companion object {
        val versionBranchRegex = """\d+\.\d+(?>\.\d+)?/dev""".toRegex()
    }

    init {
        group = "publishing"
        description = "Checks that the git repository is in a state suitable for release"
    }

    @TaskAction
    fun run() {
        val git = project.git ?: throw IllegalStateException("No git repository")
        if (!git.status().isClean) {
            throw IllegalStateException("Git repository not ready for release (${git.status()})")
        }
        val currentBranch = git.currentBranch()
        if (currentBranch == null || (currentBranch != "main" && !currentBranch.matches(versionBranchRegex))) {
            throw IllegalStateException("Need to be on main or a version branch to release (currently on ${currentBranch})")
        }
        git.fetch()
        val status = git.trackingStatus() ?: throw IllegalStateException("No remote tracking branch set for $currentBranch")
        if (status.aheadCount != 0) {
            throw IllegalStateException("Some commits have not been pushed")
        }
        if (status.behindCount != 0) {
            throw IllegalStateException("Some commits have not been pulled")
        }
    }
}
