package com.google.bitfit

import android.app.Application

class CycleApplication: Application() {
    val db by lazy { CycleDatabase.getInstance(this) }
}