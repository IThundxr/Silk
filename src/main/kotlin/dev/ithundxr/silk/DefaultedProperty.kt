package dev.ithundxr.silk

import kotlin.reflect.KProperty

fun <T> defaulted(initializer: () -> T) = DefaultedProperty(initializer)

class DefaultedProperty<T>(val initializer: () -> T) {
    private var listener: (T) -> Unit = {}
    private var initialized: Boolean = false
    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!initialized) this.setValue(thisRef, property, initializer())
        @Suppress("UNCHECKED_CAST") // cannot be null at this point unless the caller wants it to be
        return this.value as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        initialized = true
        this.value = value
        this.listener(value)
    }
}
