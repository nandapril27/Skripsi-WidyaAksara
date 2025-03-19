package com.example.widyaaksara

import android.app.Application
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.widyaaksara.service.BacksoundService

class MainApplication : Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // Aplikasi masuk ke background, hentikan backsound
        stopService(Intent(this, BacksoundService::class.java))
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Aplikasi kembali ke foreground, mulai backsound lagi
        startService(Intent(this, BacksoundService::class.java))
    }
}
