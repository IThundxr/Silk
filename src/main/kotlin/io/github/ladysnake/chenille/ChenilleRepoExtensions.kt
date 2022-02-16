/*
 * Chenille
 * Copyright (C) 2022 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
@file:JvmName("ChenilleRepoExtensions")
@file:Suppress("unused")

package io.github.ladysnake.chenille

import org.gradle.api.artifacts.dsl.RepositoryHandler

fun RepositoryHandler.cotton() {
    maven { repo ->
        repo.name = "CottonMC"
        repo.setUrl("https://server.bbkr.space/artifactory/libs-release")
        repo.content {
            it.includeGroup("io.github.cottonmc")
        }
    }
}

fun RepositoryHandler.cursemaven() {
    maven { repo ->
        repo.name = "Cursemaven"
        repo.setUrl("https://cursemaven.com")
        repo.content {
            it.includeGroup("curse.maven")
        }
    }
}

fun RepositoryHandler.jitpack() {
    maven { repo ->
        repo.name = "Jitpack"
        repo.setUrl("https://jitpack.io")
        repo.content {
            it.includeGroupByRegex("(io|com)\\.github\\..*")
        }
    }
}

fun RepositoryHandler.ladysnake() {
    maven { repo ->
        repo.name = "Ladysnake Mods"
        repo.setUrl("https://ladysnake.jfrog.io/artifactory/mods")
        repo.content {
            it.includeGroupByRegex("dev\\.emi.*")
            it.includeGroup("io.github.ladysnake")
            it.includeGroupByRegex("(dev|io\\.github)\\.onyxstudios.*")
        }
    }
}

fun RepositoryHandler.lucko() {
    maven { repo ->
        repo.name = "Lucko"
        repo.setUrl("https://oss.sonatype.org/content/repositories/snapshots")
        repo.content {
            it.includeGroup("me.lucko")
        }
    }
}

fun RepositoryHandler.modrinth() {
    maven { repo ->
        repo.name = "Modrinth"
        repo.setUrl("https://api.modrinth.com/maven")
        repo.content {
            it.includeGroup("maven.modrinth")
        }
    }
}

fun RepositoryHandler.terraformers() {
    maven { repo ->
        repo.name = "TerraformersMC"
        repo.setUrl("https://maven.terraformersmc.com/releases")
        repo.content {
            it.includeGroup("com.terraformersmc")
        }
    }
}
