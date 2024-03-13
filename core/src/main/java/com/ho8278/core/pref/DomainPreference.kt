package com.ho8278.core.pref

import java.lang.reflect.Type

open class DomainPreference(
    private val preference: Preference,
    private val domain: String
) {
    suspend fun <T> getValue(key: String, type: Type): T? {
        return preference.getValue(domain, key, type)
    }

    suspend fun <T> putValue(key: String, value: T, type: Type) {
        preference.putValue(domain, key, value, type)
    }

    suspend fun removeValue(key: String) {
        preference.removeValue(domain, key)
    }

    suspend fun clear() {
        preference.clear(domain)
    }
}