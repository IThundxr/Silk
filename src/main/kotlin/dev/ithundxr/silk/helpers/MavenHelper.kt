package dev.ithundxr.silk.helpers

import dev.ithundxr.silk.SilkProject
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import java.net.URI

internal object MavenHelper {
    fun configureDefaults(project: SilkProject, ithundxrMaven: IThundxrMaven?) {
        project.pluginManager.apply("maven-publish")

        project.extensions.configure(PublishingExtension::class.java) { ext ->
            ext.publications { pubs ->
                if (pubs.findByName("mavenJava") == null) {
                    pubs.create("mavenJava", MavenPublication::class.java) { pub ->
                        pub.from(project.components.getByName("java"))
                    }
                }
            }
            ext.repositories { repos ->
                if (ithundxrMaven != null) {
                    val ithundxrMavenUsername = System.getenv("ITHUNDXRMAVENUSERNAME")
                    val ithundxrMavenPassword = System.getenv("ITHUNDXRMAVENPASSWORD")
                    if (ithundxrMavenUsername is String && ithundxrMavenPassword is String) {
                        repos.maven { repo ->
                            repo.name = ithundxrMaven.mavenName
                            repo.url = URI("https://maven.ithundxr.dev/${ithundxrMaven.path}/")
                            repo.credentials {
                                it.username = ithundxrMavenUsername
                                it.password = ithundxrMavenPassword
                            }
                        }
                    } else {
                        println("Cannot configure IThundxr Maven publication; please define `ITHUNDXRMAVENUSERNAME` and `ITHUNDXRMAVENPASSWORD` environment variables before running publish")
                    }
                }
            }
        }
    }

    enum class IThundxrMaven(val mavenName: String, val path: String) {
        RELEASES("IThundxrReleases", "releases"), SNAPSHOTS("IThundxrSnapshots", "snapshots")
    }
}
