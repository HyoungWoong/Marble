package com.ho8278.core.pref

import java.lang.reflect.Type

interface DomainPreference {
    suspend fun <T> getValue(domainName: String, key: String, type: Type): T?
    suspend fun <T> putValue(domainName: String, key: String, value: T, type: Type)
}