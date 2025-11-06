package com.sar.buscador.ui.location

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.sar.buscador.R
import java.util.Locale

class ManualLocationActivity : AppCompatActivity() {

    private lateinit var etAddress: TextInputEditText
    private lateinit var etLatitude: TextInputEditText
    private lateinit var etLongitude: TextInputEditText
    private lateinit var btnConfirm: MaterialButton
    private lateinit var btnBack: MaterialButton
    private lateinit var btnSwitchMode: MaterialButton
    private lateinit var tvModeTitle: MaterialTextView

    private var isAddressMode = true // true = dirección, false = coordenadas

    companion object {
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        const val EXTRA_ADDRESS = "address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_location)

        initViews()
        setupListeners()
        updateMode()
    }

    private fun initViews() {
        etAddress = findViewById(R.id.etAddress)
        etLatitude = findViewById(R.id.etLatitude)
        etLongitude = findViewById(R.id.etLongitude)
        btnConfirm = findViewById(R.id.btnConfirmManualLocation)
        btnBack = findViewById(R.id.btnBack)
        btnSwitchMode = findViewById(R.id.btnSwitchMode)
        tvModeTitle = findViewById(R.id.tvModeTitle)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSwitchMode.setOnClickListener {
            isAddressMode = !isAddressMode
            updateMode()
        }

        btnConfirm.setOnClickListener {
            if (isAddressMode) {
                processAddress()
            } else {
                processCoordinates()
            }
        }
    }

    private fun updateMode() {
        if (isAddressMode) {
            // Modo dirección
            tvModeTitle.text = "Ingresar dirección"
            findViewById<View>(R.id.layoutAddress).visibility = View.VISIBLE
            findViewById<View>(R.id.layoutCoordinates).visibility = View.GONE
            btnSwitchMode.text = "Usar coordenadas"
        } else {
            // Modo coordenadas
            tvModeTitle.text = "Ingresar coordenadas"
            findViewById<View>(R.id.layoutAddress).visibility = View.GONE
            findViewById<View>(R.id.layoutCoordinates).visibility = View.VISIBLE
            btnSwitchMode.text = "Usar dirección"
        }
    }

    private fun processAddress() {
        val address = etAddress.text.toString().trim()

        if (address.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una dirección", Toast.LENGTH_SHORT).show()
            return
        }

        // Geocoding: Dirección → Coordenadas
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocationName(address, 1)

            if (!addresses.isNullOrEmpty()) {
                val location = addresses[0]
                val latitude = location.latitude
                val longitude = location.longitude

                returnResult(latitude, longitude, address)
            } else {
                Toast.makeText(
                    this,
                    "No se encontró la dirección. Intenta con otra descripción o usa coordenadas.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al buscar la dirección: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun processCoordinates() {
        val latStr = etLatitude.text.toString().trim()
        val lngStr = etLongitude.text.toString().trim()

        if (latStr.isEmpty() || lngStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa ambas coordenadas", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val latitude = latStr.toDouble()
            val longitude = lngStr.toDouble()

            // Validar rangos
            if (latitude < -90 || latitude > 90) {
                Toast.makeText(this, "Latitud debe estar entre -90 y 90", Toast.LENGTH_SHORT).show()
                return
            }

            if (longitude < -180 || longitude > 180) {
                Toast.makeText(this, "Longitud debe estar entre -180 y 180", Toast.LENGTH_SHORT).show()
                return
            }

            // Reverse Geocoding: Coordenadas → Dirección
            var address = "Lat: $latitude, Lng: $longitude"
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    address = addresses[0].getAddressLine(0) ?: address
                }
            } catch (e: Exception) {
                // Si falla el reverse geocoding, usa las coordenadas como address
            }

            returnResult(latitude, longitude, address)

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Formato de coordenadas inválido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun returnResult(latitude: Double, longitude: Double, address: String) {
        val resultIntent = Intent().apply {
            putExtra(EXTRA_LATITUDE, latitude)
            putExtra(EXTRA_LONGITUDE, longitude)
            putExtra(EXTRA_ADDRESS, address)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}