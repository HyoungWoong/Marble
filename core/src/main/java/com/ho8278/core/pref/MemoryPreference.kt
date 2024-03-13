package com.ho8278.core.pref

import java.lang.reflect.Type

class MemoryPreference : Preference {

    private val cache = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> getValue(domainName: String, key: String, type: Type): T? {
        return cache[getCacheKey(domainName, key)] as? T
    }

    override suspend fun <T : Any> putValue(domainName: String, key: String, value: T, type: Type) {
        cache[getCacheKey(domainName, key)] = value
    }

    override suspend fun removeValue(domainName: String, key: String) {
        cache.remove(getCacheKey(domainName, key))
    }

    override suspend fun clear(domainName: String) {
        cache.clear()
    }

    private fun getCacheKey(domainName: String, key: String): String {
        return "$domainName:$key"
    }
}