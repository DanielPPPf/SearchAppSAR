package com.sar.buscador.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.sar.buscador.R

class HistoryActivity : AppCompatActivity() {

    private lateinit var btnBack: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initViews()
        setupListeners()
        loadSearchHistory()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadSearchHistory() {
        // TODO: Cargar historial desde base de datos local
        // Por ahora se muestra el layout estático
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
