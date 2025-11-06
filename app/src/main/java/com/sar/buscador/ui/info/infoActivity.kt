package com.sar.buscador.ui.info

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import com.sar.buscador.R
import com.sar.buscador.ui.result.ResultActivity

class InfoActivity : AppCompatActivity() {

    private lateinit var timeOptionsContainer: FlexboxLayout
    private lateinit var mobilityOptionsContainer: LinearLayout
    private lateinit var btnBack: MaterialButton
    private lateinit var btnBackNav: MaterialButton
    private lateinit var btnSkip: MaterialButton
    private lateinit var btnCalculate: MaterialButton

    // Datos recibidos de LocationActivity
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var address: String = ""

    // Datos seleccionados
    private var selectedTimeHours: Int? = null
    private var selectedMobility: String? = null

    // Opciones de tiempo (en horas)
    private val timeOptions = listOf(
        Pair(1, "1 hora"),
        Pair(3, "3 horas"),
        Pair(6, "6 horas"),
        Pair(12, "12 horas"),
        Pair(24, "24 horas"),
        Pair(48, "48 horas"),
        Pair(72, "72 horas"),
        Pair(168, "+3 días")  // 7 días
    )

    // Opciones de movilidad
    private val mobilityOptions = listOf(
        Pair("foot", "🚶 A pie"),
        Pair("bicycle", "🚲 Bicicleta"),
        Pair("car", "🚗 Auto"),
        Pair("public_transport", "🚌 Transporte público"),
        Pair("unknown", "❓ Desconocido")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // Obtener datos de la ubicación
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        address = intent.getStringExtra("address") ?: ""

        initViews()
        setupTimeOptions()
        setupMobilityOptions()
        setupListeners()
    }

    private fun initViews() {
        timeOptionsContainer = findViewById(R.id.timeOptionsContainer)
        mobilityOptionsContainer = findViewById(R.id.mobilityOptionsContainer)
        btnBack = findViewById(R.id.btnBack)
        btnBackNav = findViewById(R.id.btnBackNav)
        btnSkip = findViewById(R.id.btnSkip)
        btnCalculate = findViewById(R.id.btnCalculate)
    }

    private fun setupTimeOptions() {
        timeOptions.forEach { (hours, label) ->
            val button = createTimeButton(hours, label)
            timeOptionsContainer.addView(button)
        }
    }

    private fun createTimeButton(hours: Int, label: String): MaterialButton {
        return MaterialButton(this).apply {
            text = label
            textSize = 14f
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                dpToPx(80)
            ).apply {
                setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))
            }

            // Estilo inicial (no seleccionado)
            setBackgroundColor(ContextCompat.getColor(context, R.color.gray_100))
            setTextColor(ContextCompat.getColor(context, R.color.gray_700))
            cornerRadius = dpToPx(16)
            strokeWidth = dpToPx(2)
            strokeColor = ContextCompat.getColorStateList(context, R.color.gray_300)

            minWidth = dpToPx(100)
            minimumWidth = dpToPx(100)

            setOnClickListener {
                selectTimeOption(hours, this)
            }
        }
    }

    private fun selectTimeOption(hours: Int, selectedButton: MaterialButton) {
        selectedTimeHours = hours

        // Resetear todos los botones
        for (i in 0 until timeOptionsContainer.childCount) {
            val button = timeOptionsContainer.getChildAt(i) as? MaterialButton
            button?.apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.gray_100))
                setTextColor(ContextCompat.getColor(context, R.color.gray_700))
                strokeColor = ContextCompat.getColorStateList(context, R.color.gray_300)
            }
        }

        // Marcar el seleccionado
        selectedButton.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
            setTextColor(Color.WHITE)
            strokeColor = ContextCompat.getColorStateList(context, R.color.primary)
        }
    }

    private fun setupMobilityOptions() {
        mobilityOptions.forEach { (value, label) ->
            val button = createMobilityButton(value, label)
            mobilityOptionsContainer.addView(button)
        }
    }

    private fun createMobilityButton(value: String, label: String): MaterialButton {
        return MaterialButton(this).apply {
            text = label
            textSize = 16f
            gravity = Gravity.START or Gravity.CENTER_VERTICAL

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(60)
            ).apply {
                setMargins(0, 0, 0, dpToPx(12))
            }

            // Estilo inicial
            setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            setTextColor(ContextCompat.getColor(context, R.color.gray_700))
            cornerRadius = dpToPx(12)
            strokeWidth = dpToPx(2)
            strokeColor = ContextCompat.getColorStateList(context, R.color.gray_300)

            setPadding(dpToPx(20), dpToPx(16), dpToPx(20), dpToPx(16))

            setOnClickListener {
                selectMobilityOption(value, this)
            }
        }
    }

    private fun selectMobilityOption(value: String, selectedButton: MaterialButton) {
        selectedMobility = value

        // Resetear todos los botones
        for (i in 0 until mobilityOptionsContainer.childCount) {
            val button = mobilityOptionsContainer.getChildAt(i) as? MaterialButton
            button?.apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                setTextColor(ContextCompat.getColor(context, R.color.gray_700))
                strokeColor = ContextCompat.getColorStateList(context, R.color.gray_300)
            }
        }

        // Marcar el seleccionado
        selectedButton.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.primary_light))
            setTextColor(ContextCompat.getColor(context, R.color.primary))
            strokeColor = ContextCompat.getColorStateList(context, R.color.primary)
        }
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnBackNav.setOnClickListener {
            finish()
        }

        btnSkip.setOnClickListener {
            // Omitir y continuar con valores por defecto
            continueToResult(
                timeHours = 24, // Default: 24 horas
                mobility = "unknown" // Default: desconocido
            )
        }

        btnCalculate.setOnClickListener {
            if (selectedTimeHours == null || selectedMobility == null) {
                Toast.makeText(
                    this,
                    "Por favor selecciona el tiempo y la movilidad",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            continueToResult(selectedTimeHours!!, selectedMobility!!)
        }
    }

    private fun continueToResult(timeHours: Int, mobility: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("latitude", latitude)
            putExtra("longitude", longitude)
            putExtra("address", address)
            putExtra("timeHours", timeHours)
            putExtra("mobility", mobility)
        }
        startActivity(intent)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}