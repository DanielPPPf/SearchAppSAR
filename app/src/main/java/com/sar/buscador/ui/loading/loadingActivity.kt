package com.sar.buscador.ui.loading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.sar.buscador.R
import com.sar.buscador.ui.result.ResultActivity

class LoadingActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Simular cálculo durante 3 segundos
        handler.postDelayed({
            navigateToResult()
        }, 3000)
    }

    private fun navigateToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        // Pasar datos recibidos
        intent.putExtras(this.intent.extras ?: Bundle())
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}