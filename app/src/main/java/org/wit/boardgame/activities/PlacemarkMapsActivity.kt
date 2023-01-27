package org.wit.boardgame.activities


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.boardgame.R
import org.wit.boardgame.activities.data.userOnline
import org.wit.boardgame.databinding.ActivityPlacemarkMapsBinding
import org.wit.boardgame.databinding.ContentPlacemarkMapsBinding
import org.wit.boardgame.main.MainApp
import org.wit.boardgame.models.PlacemarkModel


class PlacemarkMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private lateinit var binding: ActivityPlacemarkMapsBinding
    private lateinit var contentBinding: ContentPlacemarkMapsBinding
    lateinit var app: MainApp
    private lateinit var map: GoogleMap
    private lateinit var model: PlacemarkModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlacemarkMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        contentBinding = ContentPlacemarkMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }

        contentBinding.join.setOnClickListener() {
            joinGame()
        }
    }

    private fun joinGame() {
        for (placemarkModel in app.placemarks.findAll()) {
            if(contentBinding.currentTitle.text == placemarkModel.title) {
                if(placemarkModel.userList.contains(userOnline)) {
                    placemarkModel.userList.remove(userOnline)
                    placemarkModel.spotsLeft++
                    contentBinding.join.text = getString(R.string.join)

                }
                else {
                    placemarkModel.userList.add(userOnline)
                    placemarkModel.spotsLeft--
                    contentBinding.join.text = getString(R.string.leave)
                }




            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.placemarks.findAll().forEach {
            model = it
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions()
                .title(model.title)
                .position(loc)
                .snippet(model.description)
                //.icon(BitmapDescriptorFactory.fromBitmap(sizedBitmap))

            map.setOnMarkerClickListener(this)
            val createdMarker = map.addMarker(options)
            createdMarker?.tag= model.id
            map.setInfoWindowAdapter(CustomInfoWindowAdapter(it))
            map.setOnMapClickListener(this)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, model.zoom))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        contentBinding.currentTitle.text = marker.title
        contentBinding.currentDescription.text = marker.snippet
        for (placemarkModel in app.placemarks.findAll()) {
            if(placemarkModel.id == marker.tag) {
                contentBinding.imageView.setImageURI(placemarkModel.image)
                if(placemarkModel.spotsLeft == 0 && !placemarkModel.userList.contains(userOnline)) {
                    contentBinding.join.isEnabled = false
                    contentBinding.join.setBackgroundColor(getColor(R.color.grey))
                }
                else if(placemarkModel.userList.contains(userOnline)){
                    contentBinding.join.isEnabled = true
                    contentBinding.join.text = getString(R.string.leave)
                    contentBinding.join.setBackgroundColor(getColor(R.color.black))
                }
                else {
                    contentBinding.join.isEnabled = true
                    contentBinding.join.setBackgroundColor(getColor(R.color.black))
                }
                break
            }
        }
        contentBinding.join.visibility = View.VISIBLE
        contentBinding.imageView.visibility = View.VISIBLE


        return false
    }

    override fun onMapClick(point: LatLng) {
        contentBinding.join.visibility = View.INVISIBLE
        contentBinding.imageView.visibility = View.INVISIBLE
        contentBinding.currentTitle.text = ""
        contentBinding.currentDescription.text = ""
    }



    internal inner class CustomInfoWindowAdapter(inModel: PlacemarkModel) : InfoWindowAdapter {
        private var popup: View? = null
        var  modelAdapter: PlacemarkModel? = inModel


        override fun getInfoContents(marker: Marker): View? {
            if (popup == null) {
                popup = layoutInflater.inflate(R.layout.infowindow, null)
            }
            val nameView = popup?.findViewById<TextView>(R.id.infoname)
            nameView?.text = marker.title
            val descView = popup?.findViewById<TextView>(R.id.infodesc)
            descView?.text = marker.snippet
            for (placemarkModel in app.placemarks.findAll()) {
                if(placemarkModel.id == marker.tag) {
                    popup?.findViewById<ImageView>(R.id.infoimage)?.setImageURI(placemarkModel.image)
                    break
                }
            }


            return popup
        }

        override fun getInfoWindow(marker: Marker): View? {
            return null
        }

    }
}





