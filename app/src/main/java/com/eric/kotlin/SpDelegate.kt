package com.eric.kotlin

import android.content.Context
import android.content.SharedPreferences
import com.eric.AndroidStudyApplication
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class SpDelegate<T>(
    private val propName: String,
    private val defaultValue: T
) : ReadWriteProperty<Any, T> {

    private val sharedPreferences: SharedPreferences by lazy {
        AndroidStudyApplication.context.getSharedPreferences("Android_Study", Context.MODE_PRIVATE)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        value?.let { putSPValue(propName, value) }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getSPValue(propName, defaultValue) ?: defaultValue
    }

    private fun <T> getSPValue(name: String, defaultValue: T): T? = with(sharedPreferences) {
        val result = when (defaultValue) {
            is String -> getString(name, defaultValue)
            is Int -> getInt(name, defaultValue)
            is Long -> getLong(name, defaultValue)
            is Float -> getFloat(name, defaultValue)
            is Boolean -> getBoolean(name, defaultValue)
            else -> null
        }
        result as T
    }

    private fun <T> putSPValue(name: String, value: T) = with(sharedPreferences.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> null
        }
    }?.apply()
}

object SpKey {
    const val IS_FIRST = "is_first"
}

object SPMgr {
    var isFirst by SpDelegate(SpKey.IS_FIRST,"yes")
}
