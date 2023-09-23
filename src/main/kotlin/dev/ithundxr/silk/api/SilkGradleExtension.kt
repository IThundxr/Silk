package dev.ithundxr.silk.api

import org.gradle.api.Action
import java.io.File
import java.net.URL

interface SilkGradleExtension {
    val repositories: SilkRepositoryHandler
    fun repositories(action: Action<SilkRepositoryHandler>)
    fun repositories(action: SilkRepositoryHandler.() -> Unit)
    fun configurePublishing(action: Action<PublishingConfiguration>)
    var changelogFile: File
}
