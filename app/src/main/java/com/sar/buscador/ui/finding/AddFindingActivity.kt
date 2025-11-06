package com.sar.buscador.ui.finding

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.sar.buscador.R

class AddFindingActivity : AppCompatActivity() {

    private lateinit var optionWitness: MaterialCardView
    private lateinit var optionObject: MaterialCardView
    private lateinit var optionTrace: MaterialCardView
    private lateinit var btnBack: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnAddAndRecalculate: MaterialButton
    private lateinit var btnUseCurrentLocation: MaterialButton
    private lateinit var btnUseMap: MaterialButton

    private var selectedFindingType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_finding)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        optionWitness = findViewById(R.id.optionWitness)
        optionObject = findViewById(R.id.optionObject)
        optionTrace = findViewById(R.id.optionTrace)
        btnBack = findViewById(R.id.btnBack)
        btnCancel = findViewById(R.id.btnCancel)
        btnAddAndRecalculate = findViewById(R.id.btnAddAndRecalculate)
        btnUseCurrentLocation = findViewById(R.id.btnUseCurrentLocation)
        btnUseMap = findViewById(R.id.btnUseMap)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { onBackPressed() }
        btnCancel.setOnClickListener { onBackPressed() }

        // Finding type selection
        optionWitness.setOnClickListener {
            selectFindingType(optionWitness, "witness")
        }

        optionObject.setOnClickListener {
            selectFindingType(optionObject, "object")
        }

        optionTrace.setOnClickListener {
            selectFindingType(optionTrace, "trace")
        }

        // Location buttons
        btnUseCurrentLocation.setOnClickListener {
            Toast.makeText(this, "Obteniendo ubicación actual...", Toast.LENGTH_SHORT).show()
            // TODO: Obtener ubicación GPS
        }

        btnUseMap.setOnClickListener {
            Toast.makeText(this, "Abrir selector de mapa", Toast.LENGTH_SHORT).show()
            // TODO: Abrir mapa para seleccionar ubicación
        }

        // Add and recalculate
        btnAddAndRecalculate.setOnClickListener {
            if (selectedFindingType != null) {
                // TODO: Guardar hallazgo y retornar resultado
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Por favor selecciona el tipo de hallazgo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun selectFindingType(selectedCard: MaterialCardView, findingType: String) {
        // Deselect all
        listOf(optionWitness, optionObject, optionTrace).forEach { card ->
            card.strokeColor = ContextCompat.getColor(this, R.color.gray_200)
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        // Select chosen option
        selectedCard.strokeColor = ContextCompat.getColor(this, R.color.blue_primary)
        selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray_50))

        selectedFindingType = findingType
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}