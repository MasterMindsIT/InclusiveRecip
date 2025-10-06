package com.nutriweek.inclusiverecip.session

import android.content.Context
import android.content.SharedPreferences

object SessionPrefs {
    private const val FILE = "session_prefs"
    private const val KEY_ACTIVE_ID = "active_user_id"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        }
    }

    fun getActiveUserId(): String? = prefs.getString(KEY_ACTIVE_ID, null)

    fun setActiveUserId(id: String?) {
        prefs.edit().apply {
            if (id.isNullOrBlank()) remove(KEY_ACTIVE_ID) else putString(KEY_ACTIVE_ID, id)
        }.apply()
    }
}
