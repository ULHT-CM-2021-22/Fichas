package com.github.mstavares.cm.fichas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mstavares.cm.fichas.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class MapFragment : Fragment(), OnLocationChangedListener {

    private lateinit var binding: FragmentMapBinding
    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        binding = FragmentMapBinding.bind(view)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync {
            map = it
            FusedLocation.registerListener(this)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(12f)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onDestroy() {
        super.onDestroy()
        FusedLocation.unregisterListener(this)
    }

}