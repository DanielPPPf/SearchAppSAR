package com.sar.buscador.ui.help

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.sar.buscador.R

class HelpActivity : AppCompatActivity() {

    private val TAG = "HelpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.d(TAG, "Setting content view")
            setContentView(R.layout.activity_help)

            Log.d(TAG, "Initializing views")
            initViews()

            Log.d(TAG, "Setting up listeners")
            setupListeners()

            Log.d(TAG, "HelpActivity created successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error creating HelpActivity", e)
            e.printStackTrace()
        }
    }

    private fun initViews() {
        try {
            val btnBack = findViewById<MaterialButton>(R.id.btnBack)
            val btnUnderstood = findViewById<MaterialButton>(R.id.btnUnderstood)

            Log.d(TAG, "btnBack: ${if (btnBack != null) "found" else "NULL"}")
            Log.d(TAG, "btnUnderstood: ${if (btnUnderstood != null) "found" else "NULL"}")

        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views", e)
        }
    }

    private fun setupListeners() {
        try {
            findViewById<MaterialButton>(R.id.btnBack)?.setOnClickListener {
                Log.d(TAG, "Back button clicked")
                finish()
            }

            findViewById<MaterialButton>(R.id.btnUnderstood)?.setOnClickListener {
                Log.d(TAG, "Understood button clicked")
                finish()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error setting up listeners", e)
        }
    }
}