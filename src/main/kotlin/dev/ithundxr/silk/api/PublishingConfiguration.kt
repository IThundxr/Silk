package dev.ithundxr.silk.api

interface PublishingConfiguration {
    /**
     * The upload artifact file. This can be any object type that is resolvable by
     * [org.gradle.api.Project.file].
     */
    var mainArtifact: Any

    /**
     * Publishes maven artifacts to the IThundxr maven. Uses the project's publishing configuration.
     *
     * This method uses the `ITHUNDXRMAVENUSERNAME` and `ITHUNDXRMAVENPASSWORD` environment variables to set maven credentials.
     *
     * @param snapshot if true, will publish to the IThundxr `snapshots` repository instead of the default `releases`
     */
    fun withIThundxrMaven(snapshot: Boolean = false)
}
