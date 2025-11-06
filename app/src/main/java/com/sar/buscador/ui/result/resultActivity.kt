package com.sar.buscador.ui.result

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.sar.buscador.R
import com.sar.buscador.ui.finding.AddFindingActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var btnBack: MaterialButton
    private lateinit var btnRefresh: MaterialButton
    private lateinit var btnAddFinding: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var btnShare: MaterialButton
    private lateinit var btnDownload: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initViews()
        setupListeners()
        setupMap()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnAddFinding = findViewById(R.id.btnAddFinding)
        btnSave = findViewById(R.id.btnSave)
        btnShare = findViewById(R.id.btnShare)
        btnDownload = findViewById(R.id.btnDownload)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnRefresh.setOnClickListener {
            Toast.makeText(this, "Actualizando mapa...", Toast.LENGTH_SHORT).show()
            // TODO: Actualizar cálculo del mapa
        }

        btnAddFinding.setOnClickListener {
            val intent = Intent(this, AddFindingActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_FINDING)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnSave.setOnClickListener {
            Toast.makeText(this, "Búsqueda guardada", Toast.LENGTH_SHORT).show()
            // TODO: Guardar en base de datos local
        }

        btnShare.setOnClickListener {
            shareSearchResult()
        }

        btnDownload.setOnClickListener {
            Toast.makeText(this, "Descargando imagen...", Toast.LENGTH_SHORT).show()
            // TODO: Generar y descargar imagen del mapa
        }
    }

    private fun setupMap() {
        // TODO: Configurar Google Maps
        // Por ahora mostramos el placeholder
    }

    private fun shareSearchResult() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Zona de búsqueda SAR - [Coordenadas]")
            putExtra(Intent.EXTRA_SUBJECT, "Búsqueda de persona desaparecida")
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir búsqueda"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_FINDING && resultCode == RESULT_OK) {
            // Hallazgo agregado, recalcular mapa
            Toast.makeText(this, "Recalculando con nuevo hallazgo...", Toast.LENGTH_SHORT).show()
            // TODO: Recalcular zonas de búsqueda
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    companion object {
        private const val REQUEST_ADD_FINDING = 101
    }
}
