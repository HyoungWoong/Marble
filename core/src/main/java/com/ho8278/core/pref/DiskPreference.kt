package com.ho8278.core.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.ho8278.core.serialize.Serializer
import java.lang.reflect.Type

class DiskPreference(
    private val context: Context,
    private val serializer: Serializer,
) : DomainPreference {

    private val preferenceCache = mutableMapOf<String, SharedPreferences>()

    private fun getPreference(domainName: String): SharedPreferences {
        return preferenceCache[domainName] ?: kotlin.run {
            val sharedPreference = context.getSharedPreferences(domainName, Context.MODE_PRIVATE)
            preferenceCache[domainName] = sharedPreference
            sharedPreference
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    override suspend fun <T> getValue(domainName: String, key: String, type: Type): T? {
        val sharedPreference = getPreference(domainName)

        return when (type) {
            Int::class.java -> sharedPreference.getInt(key, 0)
            String::class.java -> sharedPreference.getString(key, null)
            Boolean::class.java -> sharedPreference.getBoolean(key, false)
            Float::class.java -> sharedPreference.getFloat(key, 0.0f)
            else -> {
                val valueString = sharedPreference.getString(key, null)
                valueString?.let { serializer.deserialize<T>(valueString, type) }
            }
        } as T?
    }

    override suspend fun <T> putValue(domainName: String, key: String, value: T, type: Type) {
        val sharedPreference = getPreference(domainName)
        when (type) {
            Int::class.java -> sharedPreference.edit { putInt(key, value as Int) }
            String::class.java -> sharedPreference.edit { putString(key, value as String) }
            Boolean::class.java -> sharedPreference.edit { putBoolean(key, value as Boolean) }
            Float::class.java -> sharedPreference.edit { putFloat(key, value as Float) }
            else -> {
                val serializedString = serializer.serialize<T>(value, type)
                sharedPreference.edit { putString(key, serializedString) }
            }
        }
    }
}