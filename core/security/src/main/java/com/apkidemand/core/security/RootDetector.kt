package com.apkidemand.core.security

import android.content.Context
import com.scottyab.rootbeer.RootBeer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val rootBeer = RootBeer(context)

    fun isDeviceRooted(): Boolean {
        // We log server-side instead of blocking entirely per PRD
        return rootBeer.isRooted
    }
}

