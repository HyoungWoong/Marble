package com.ho8278.core.pref

import java.lang.reflect.Type

interface Preference {
    suspend fun <T> getValue(domainName: String, key: String, type: Type): T?
    suspend fun <T> putValue(domainName: String, key: String, value: T, type: Type)
    suspend fun removeValue(domainName: String, key: String)
    suspend fun clear(domainName: String)
}