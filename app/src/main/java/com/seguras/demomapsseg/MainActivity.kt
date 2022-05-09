package com.seguras.demomapsseg

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.seguras.demomapsseg.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFragment()
    }

    private fun createFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun enalbeLocation(){
        if(!::map.isInitialized)return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{
            requestLocationPermission()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //Este codigo se ejecutara cuando el Fragment termine de cargarse
        map = googleMap
        //Tratar de acceder a la ubucacion del GPS
        enalbeLocation()
    }

    private fun createMarker(title: String ,latitude: Double, longitude: Double) {
        map.clear()
        val coordinates = LatLng(latitude, longitude)
        val marker = MarkerOptions().position(coordinates).title(title)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude),18f)
        )
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            //Mostrar la ventana de permiso
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this, "Para activar permiso, ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    fun setLocation(view: View) {
        var restaurantName: String = ""
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        with (binding) {
            when(view.id) {
                belliniButton.id -> {
                    restaurantName = "Bellini"
                    latitude = 19.394794267115692
                    longitude = -99.17410198666796
                }
                sonoraGrillButton.id -> {
                    restaurantName = "Sonora Grill"
                    latitude = 19.38868257433863
                    longitude = -99.17574952899598
                }
                chillisButton.id -> {
                    restaurantName = "Chilli's"
                    latitude = 19.437077181263913
                    longitude = -99.2069726813531
                }
                cambalacheButton.id -> {
                    restaurantName = "Cambalache"
                    latitude = 19.432200458233414
                    longitude = -99.19595161672936
                }

            }
        }
        createMarker(restaurantName,latitude, longitude)
    }
}