package com.ho8278.core.pref

import android.util.LruCache
import java.lang.reflect.Type

class CachePreference(
    private val delegate: DomainPreference
) : DomainPreference {

    private val valueCache = LruCache<String, Any>(CACHE_SIZE)

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> getValue(domainName: String, key: String, type: Type): T? {
        return valueCache[getCacheKey(domainName, key)] as? T
            ?: delegate.getValue(domainName, key, type)
    }

    override suspend fun <T> putValue(domainName: String, key: String, value: T, type: Type) {
        valueCache.put(getCacheKey(domainName, key), value)
        delegate.putValue(domainName, key, value, type)
    }

    override suspend fun removeValue(domainName: String, key: String) {
        valueCache.remove(getCacheKey(domainName, key))
        delegate.removeValue(domainName, key)
    }

    override suspend fun clear(domainName: String) {
        valueCache.evictAll()
        delegate.clear(domainName)
    }

    private fun getCacheKey(domainName: String, key: String): String {
        return "$domainName:$key"
    }

    companion object {
        private const val CACHE_SIZE = 256
    }
}