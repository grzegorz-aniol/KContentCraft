package com.appga.contentcraft.store

import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
class ContentCache {
    private  val cache = ConcurrentHashMap<String, Boolean>()

    fun getAll(): List<String> {
        return cache.keys.toList()
    }

    fun exists(item: String): Boolean {
        return cache.containsKey(item)
    }

    fun setAll(items: List<String>) {
        cache.clear()
        items.forEach { cache[it] = true }
    }
}