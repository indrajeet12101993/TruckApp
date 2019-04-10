package com.truckintransit.operator.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Build.VERSION_CODES.*
import android.preference.PreferenceManager
import java.lang.ref.WeakReference
import java.util.*

class LocaleManager(context: Context) {
    private val LANGUAGE_ENGLISH = "en"

    private val LANGUAGE_KEY = "language_key"

    private  var prefs: SharedPreferences

    init {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }


    fun setLocale(c: Context): Context {
        return updateResources(c, getLanguage()!!)
    }

    fun setNewLocale(c: Context, language: String): Context {
        persistLanguage(language)
        return updateResources(c, language)
    }

    fun getLanguage(): String? {
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH)
    }

    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(language: String) {
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        prefs.edit().putString(LANGUAGE_KEY, language)
            .apply()
    }



    fun getLocale(res: Resources): Locale {
        val config = res.configuration
        return if (UtiliyMethods.isAtLeastVersion(N)) config.locales.get(0) else config.locale
    }

    fun hexString(res: Resources): String {
        val resImpl = getPrivateField("android.content.res.Resources", "mResourcesImpl", res)
        val o = resImpl ?: res
        return "@" + Integer.toHexString(o.hashCode())
    }

    fun getPrivateField(className: String, fieldName: String, `object`: Any?): Any? {
        try {
            val c = Class.forName(className)
            val f = c.getDeclaredField(fieldName)
            f.isAccessible = true
            return f.get(`object`)
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }

    }

    private fun updateResources(context: Context, language: String): Context {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        if (UtiliyMethods.isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }

  /*  fun getTitleCache(): String {
        // https://developer.android.com/about/versions/pie/restrictions-non-sdk-interfaces
        if (isAtLeastVersion(P)) return "Can't access title cache\nstarting from API 28"
        val o =getPrivateField("android.app.ApplicationPackageManager", "sStringCache", null)
        val cache = o as Map<*, WeakReference<CharSequence>> ?: return ""

        val builder = StringBuilder("Cache:").append("\n")
        for ((_, value) in cache) {
            val title = value.get()
            if (title != null) {
                builder.append(title).append("\n")
            }
        }
        return builder.toString()
    }

    fun getTopLevelResources(a: Activity): Resources {
        try {
            return a.packageManager.getResourcesForApplication(a.applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException(e)
        }

    }*/


}