package fi.oamk.cottagerepublic.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MapUtils(
    val savedInstanceState: Bundle?,
    val context: Context,
    mapView: MapView,
    private val isAllGesturesEnabled: Boolean = false,
    private val mapListener: MapboxMap.OnMapClickListener = MapboxMap.OnMapClickListener { _ -> true },
) {

    var mapboxMap = MutableLiveData<MapboxMap>()

    companion object {
        const val SOURCE_ID = "SOURCE_ID"
        const val ICON_ID = "ICON_ID"
        const val LAYER_ID = "LAYER_ID"
    }

    init {
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            it.setStyle(Style.MAPBOX_STREETS) { style ->
                // Map is set up and the style has loaded.
                // Now you can add data or make other map adjustments
                with(style) {
                    addSource(GeoJsonSource(SOURCE_ID))

                    addImageAsync(
                        ICON_ID,
                        com.mapbox.mapboxsdk.utils.BitmapUtils.getDrawableFromRes(
                            context,
                            fi.oamk.cottagerepublic.R.drawable.map_marker_cottage_24
                        )!!
                    )

                    addLayer(
                        SymbolLayer(LAYER_ID, SOURCE_ID)
                            .withProperties(
                                com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage(ICON_ID),
                                com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap(true),
                                com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement(true)
                            )
                    )
                }

                val uiSettings = it.uiSettings
                uiSettings.setAllGesturesEnabled(isAllGesturesEnabled)
                uiSettings.isCompassEnabled = false
                uiSettings.isAttributionEnabled = false
                uiSettings.isLogoEnabled = false
                uiSettings.isQuickZoomGesturesEnabled = false

                // add click listener to the map
                it.addOnMapClickListener(mapListener)
            }
            mapboxMap.value = it
        }
    }

    fun initCameraPosition(point: HashMap<String, Double>) {
        mapboxMap.value?.cameraPosition =
            CameraPosition.Builder()
                .target(LatLng(point["lat"]!!, point["long"]!!))
                .zoom(4.0)
                .build()
    }

    fun updateMapStyle(point: HashMap<String, Double>) {
        mapboxMap.value?.getStyle {
            val geoJsonSource = it.getSourceAs<GeoJsonSource>(SOURCE_ID)
            geoJsonSource?.setGeoJson(Feature.fromGeometry(Point.fromLngLat(point["long"]!!, point["lat"]!!)))
        }

        mapboxMap.value?.cameraPosition =
            CameraPosition.Builder()
                .target(LatLng(point["lat"]!!, point["long"]!!))
                .zoom(15.0)
                .build()
    }

    fun getPointAddress(point: HashMap<String, Double>): Address {
        return Geocoder(context).getFromLocation(point["lat"]!!, point["long"]!!, 1)[0]
    }
}