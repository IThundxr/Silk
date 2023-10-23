package dev.ithundxr.silk.api;

import org.gradle.internal.impldep.com.google.gson.GsonBuilder
import org.gradle.internal.impldep.com.google.gson.JsonElement
import java.io.FilterReader
import java.io.Reader
import java.io.StringReader

private val GSON = GsonBuilder().setPrettyPrinting().create()

class JsonNormalizerReader(reader: Reader) : FilterReader(
    StringReader(
        GSON.toJson(
            GSON.fromJson(reader, JsonElement::class.java)
        )
    )
)

