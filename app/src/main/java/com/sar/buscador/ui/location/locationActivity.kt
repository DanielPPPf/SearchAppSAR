package com.sar.buscador.ui.location

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.sar.buscador.R
import com.sar.buscador.ui.info.InfoActivity

class LocationActivity : AppCompatActivity() {

    private lateinit var btnBack: MaterialButton
    private lateinit var cardSelectOnMap: MaterialCardView
    private lateinit var cardWriteLocation: MaterialCardView
    private lateinit var tvSelectedLocationInfo: MaterialTextView
    private lateinit var layoutSelectedLocation: View
    private lateinit var btnContinue: MaterialButton

    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null
    private var selectedAddress: String? = null

    // Launcher para MapPickerActivity
    private val mapPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                selectedLatitude = data.getDoubleExtra(MapPickerActivity.EXTRA_LATITUDE, 0.0)
                selectedLongitude = data.getDoubleExtra(MapPickerActivity.EXTRA_LONGITUDE, 0.0)
                selectedAddress = data.getStringExtra(MapPickerActivity.EXTRA_ADDRESS)

                showSelectedLocation()
            }
        }
    }

    // Launcher para ManualLocationActivity
    private val manualLocationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                selectedLatitude = data.getDoubleExtra(ManualLocationActivity.EXTRA_LATITUDE, 0.0)
                selectedLongitude = data.getDoubleExtra(ManualLocationActivity.EXTRA_LONGITUDE, 0.0)
                selectedAddress = data.getStringExtra(ManualLocationActivity.EXTRA_ADDRESS)

                showSelectedLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        cardSelectOnMap = findViewById(R.id.cardSelectOnMap)
        cardWriteLocation = findViewById(R.id.cardWriteLocation)
        tvSelectedLocationInfo = findViewById(R.id.tvSelectedLocationInfo)
        layoutSelectedLocation = findViewById(R.id.layoutSelectedLocation)
        btnContinue = findViewById(R.id.btnContinue)

        // Ocultar inicialmente la sección de ubicación seleccionada
        layoutSelectedLocation.visibility = View.GONE
        btnContinue.isEnabled = false
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        // Card: Seleccionar en mapa
        cardSelectOnMap.setOnClickListener {
            val intent = Intent(this, MapPickerActivity::class.java)
            mapPickerLauncher.launch(intent)
        }

        // Card: Escribir ubicación
        cardWriteLocation.setOnClickListener {
            val intent = Intent(this, ManualLocationActivity::class.java)
            manualLocationLauncher.launch(intent)
        }

        // Botón continuar
        btnContinue.setOnClickListener {
            continueToInfo()
        }
    }

    private fun showSelectedLocation() {
        if (selectedLatitude != null && selectedLongitude != null) {
            // Mostrar la información de la ubicación seleccionada
            layoutSelectedLocation.visibility = View.VISIBLE
            btnContinue.isEnabled = true

            val locationText = """
            📍 Ubicación seleccionada:
            
            $selectedAddress
            
            Lat: ${String.format("%.6f", selectedLatitude)}
            Lng: ${String.format("%.6f", selectedLongitude)}
        """.trimIndent()

            tvSelectedLocationInfo.text = locationText

            // Scroll hacia abajo para mostrar la info
            val scrollView = findViewById<androidx.core.widget.NestedScrollView>(R.id.scrollView)
            scrollView?.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }
    }

    private fun continueToInfo() {
        if (selectedLatitude == null || selectedLongitude == null) {
            Toast.makeText(this, "Por favor selecciona una ubicación", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, InfoActivity::class.java).apply {
            putExtra("latitude", selectedLatitude)
            putExtra("longitude", selectedLongitude)
            putExtra("address", selectedAddress)
        }
        startActivity(intent)
    }
}