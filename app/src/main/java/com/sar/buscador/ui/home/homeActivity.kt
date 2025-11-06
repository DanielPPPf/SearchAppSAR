package com.sar.buscador.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.sar.buscador.R
import com.sar.buscador.ui.location.LocationActivity
import com.sar.buscador.ui.history.HistoryActivity
import com.sar.buscador.ui.help.HelpActivity

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.d(TAG, "Setting content view")
            setContentView(R.layout.activity_home)

            Log.d(TAG, "Setting up listeners")
            setupListeners()

            Log.d(TAG, "HomeActivity created successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error creating HomeActivity", e)
        }
    }

    private fun setupListeners() {
        try {
            // Card: Nueva Búsqueda
            findViewById<MaterialCardView>(R.id.cardNewSearch)?.setOnClickListener {
                Log.d(TAG, "Nueva Búsqueda clicked")
                startActivity(Intent(this, LocationActivity::class.java))
            }

            // Card: Mis Búsquedas (Historial)
            findViewById<MaterialCardView>(R.id.cardMySearches)?.setOnClickListener {
                Log.d(TAG, "Mis Búsquedas clicked")
                startActivity(Intent(this, HistoryActivity::class.java))
            }

            // Card: Cómo Usar
            findViewById<MaterialCardView>(R.id.cardHelp)?.setOnClickListener {
                Log.d(TAG, "Cómo Usar card clicked")
                startActivity(Intent(this, HelpActivity::class.java))
            }

            // Botón de ayuda en header (mejorado)
            findViewById<MaterialButton>(R.id.btnHelp)?.setOnClickListener {
                Log.d(TAG, "Help button clicked")
                startActivity(Intent(this, HelpActivity::class.java))
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error setting up listeners", e)
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar el badge de contador cuando volvemos a la pantalla
        updateSearchCounter()
    }

    private fun updateSearchCounter() {
        try {
            // TODO: Obtener el número real de búsquedas guardadas
            // Por ahora usamos un placeholder
            val searchCount = getSearchCount()

            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.tvBadgeCount)?.apply {
                text = " $searchCount"
                visibility = if (searchCount > 0) android.view.View.VISIBLE else android.view.View.GONE
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating search counter", e)
        }
    }

    private fun getSearchCount(): Int {
        // TODO: Implementar con Room o SharedPreferences
        // Por ahora retornamos un valor de prueba
        val prefs = getSharedPreferences("sar_prefs", MODE_PRIVATE)
        return prefs.getInt("search_count", 0)
    }
}