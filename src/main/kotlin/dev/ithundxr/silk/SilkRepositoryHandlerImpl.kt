package dev.ithundxr.silk

import dev.ithundxr.silk.api.SilkRepositoryHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import kotlin.reflect.full.declaredFunctions

class SilkRepositoryHandlerImpl(private val repositories: RepositoryHandler): SilkRepositoryHandler {
    override fun allCommonRepositories() {
        this::class.declaredFunctions.filter { it.name != this::allCommonRepositories.name }.forEach { it.call(this) }
    }

    override fun cotton() {
        repositories.maven { repo ->
            repo.name = "CottonMC"
            repo.setUrl("https://server.bbkr.space/artifactory/libs-release")
            repo.content {
                it.includeGroup("io.github.cottonmc")
            }
        }
    }

    override fun cursemaven() {
        repositories.maven { repo ->
            repo.name = "Cursemaven"
            repo.setUrl("https://cursemaven.com")
            repo.content {
                it.includeGroup("curse.maven")
            }
        }
    }

    override fun jamieswhiteshirt() {
        repositories.maven { repo ->
            repo.setUrl("https://maven.jamieswhiteshirt.com/libs-release/")
            repo.content {
                it.includeGroup("com.jamieswhiteshirt")
            }
        }
    }

    override fun jitpack() {
        repositories.maven { repo ->
            repo.name = "Jitpack"
            repo.setUrl("https://jitpack.io")
            repo.content {
                it.includeGroupByRegex("(io|com)\\.github\\..*")
            }
        }
    }

    override fun ladysnake() {
        repositories.maven { repo ->
            repo.name = "Ladysnake Releases"
            repo.setUrl("https://maven.ladysnake.org/releases")
            repo.content {
                it.includeGroupByRegex("dev\\.emi.*")
                it.includeGroup("io.github.ladysnake")
                it.includeGroup("org.ladysnake")
                it.includeGroupByRegex("(dev|io\\.github)\\.onyxstudios.*")
            }
        }
    }

    override fun lucko() {
        repositories.maven { repo ->
            repo.name = "Lucko"
            repo.setUrl("https://oss.sonatype.org/content/repositories/snapshots")
            repo.content {
                it.includeGroup("me.lucko")
            }
        }
    }

    override fun modrinth() {
        repositories.maven { repo ->
            repo.name = "Modrinth"
            repo.setUrl("https://api.modrinth.com/maven")
            repo.content {
                it.includeGroup("maven.modrinth")
            }
        }
    }

    override fun shedaniel() {
        repositories.maven { repo ->
            repo.setUrl("https://maven.shedaniel.me/")
            repo.content {
                it.includeGroupByRegex("me\\.shedaniel\\..*")
                it.includeGroup("me.sargunvohra.mcmods")
            }
        }
    }

    override fun terraformers() {
        repositories.maven { repo ->
            repo.name = "TerraformersMC"
            repo.setUrl("https://maven.terraformersmc.com/releases")
            repo.content {
                it.includeGroup("com.terraformersmc")
                it.includeGroup("dev.emi")
            }
        }
    }
}
