package com.sar.buscador.ui.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sar.buscador.R
import java.util.Locale

class MapPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btnConfirm: MaterialButton
    private lateinit var btnBack: MaterialButton
    private lateinit var btnMyLocation: MaterialButton
    private lateinit var tvSelectedAddress: MaterialTextView

    private var selectedLatLng: LatLng? = null
    private var selectedAddress: String = ""

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1001
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        const val EXTRA_ADDRESS = "address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_picker)

        // Inicializar vistas
        btnConfirm = findViewById(R.id.btnConfirmLocation)
        btnBack = findViewById(R.id.btnBack)
        btnMyLocation = findViewById(R.id.btnMyLocation)
        tvSelectedAddress = findViewById(R.id.tvSelectedAddress)

        // Inicializar location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Configurar mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupListeners()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Configurar estilo del mapa
        map.uiSettings.apply {
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = false // Usamos nuestro botón personalizado
            isMapToolbarEnabled = false
        }

        // Listener para cuando el usuario toca el mapa
        map.setOnMapClickListener { latLng ->
            selectLocation(latLng)
        }

        // Verificar permisos y mostrar ubicación actual
        checkLocationPermission()
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnMyLocation.setOnClickListener {
            getCurrentLocation()
        }

        btnConfirm.setOnClickListener {
            confirmLocation()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                selectLocation(currentLatLng)
            } ?: run {
                Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show()
                // Default: Bogotá, Colombia
                val defaultLocation = LatLng(4.7110, -74.0721)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
            }
        }
    }

    private fun selectLocation(latLng: LatLng) {
        selectedLatLng = latLng

        // Limpiar marcadores anteriores
        map.clear()

        // Agregar nuevo marcador
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Ubicación seleccionada")
        )

        // Mover cámara
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng))

        // Obtener dirección
        getAddressFromLatLng(latLng)

        // Habilitar botón de confirmar
        btnConfirm.isEnabled = true
    }

    private fun getAddressFromLatLng(latLng: LatLng) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                selectedAddress = address.getAddressLine(0) ?: "Dirección no disponible"
                tvSelectedAddress.text = selectedAddress
            } else {
                selectedAddress = "Lat: ${String.format("%.6f", latLng.latitude)}, Lng: ${String.format("%.6f", latLng.longitude)}"
                tvSelectedAddress.text = selectedAddress
            }
        } catch (e: Exception) {
            selectedAddress = "Lat: ${String.format("%.6f", latLng.latitude)}, Lng: ${String.format("%.6f", latLng.longitude)}"
            tvSelectedAddress.text = selectedAddress
        }
    }

    private fun confirmLocation() {
        selectedLatLng?.let { latLng ->
            val resultIntent = Intent().apply {
                putExtra(EXTRA_LATITUDE, latLng.latitude)
                putExtra(EXTRA_LONGITUDE, latLng.longitude)
                putExtra(EXTRA_ADDRESS, selectedAddress)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        } ?: run {
            Toast.makeText(this, "Por favor selecciona una ubicación", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
                getCurrentLocation()
            } else {
                Toast.makeText(
                    this,
                    "Permiso de ubicación denegado. Usa el mapa para seleccionar manualmente.",
                    Toast.LENGTH_LONG
                ).show()
                // Default: Bogotá
                val defaultLocation = LatLng(4.7110, -74.0721)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
            }
        }
    }
}