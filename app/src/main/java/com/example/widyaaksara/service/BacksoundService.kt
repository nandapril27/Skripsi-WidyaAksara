package com.example.widyaaksara.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.widyaaksara.R

class BacksoundService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.backsound_widyaaksara) // Sesuaikan nama file
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
        return START_STICKY // Agar service tetap berjalan meskipun activity dihancurkan
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Hentikan dan hapus MediaPlayer
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
