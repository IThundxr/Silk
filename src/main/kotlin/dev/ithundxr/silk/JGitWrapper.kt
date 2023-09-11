package dev.ithundxr.silk

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.Status
import org.eclipse.jgit.lib.BranchTrackingStatus
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.FetchResult

class JGitWrapper(private val jgit: Git) {
    private val repository: Repository by lazy { jgit.repository }

    fun status(): Status = jgit.status().call()

    fun trackingStatus(): BranchTrackingStatus? = currentBranch()?.let { BranchTrackingStatus.of(repository, it) }

    fun currentBranch(): String? {
            val head: Ref? = repository.exactRef(Constants.HEAD)
            if (head != null && head.isSymbolic) {
                return head.target.shortName
            }
            return null
        }

    fun fetch(): FetchResult = jgit.fetch().call()
}

val Ref.shortName: String get() = Repository.shortenRefName(name)
