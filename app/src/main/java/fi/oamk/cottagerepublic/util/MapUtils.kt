package fi.oamk.cottagerepublic.util

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import fi.oamk.cottagerepublic.R

object Constants {
    const val SOURCE_ID = "SOURCE_ID"
    const val ICON_ID = "ICON_ID"
    const val LAYER_ID = "LAYER_ID"
}

object MapUtils {
    @SuppressLint("UseCompatLoadingForDrawables")
    fun initializeMap(
        savedInstanceState: Bundle?,
        resource: Resources,
        mapView: MapView,
        locations: ArrayList<HashMap<String, Double>> = ArrayList<HashMap<String, Double>>(),
        isAllGesturesEnabled: Boolean,
    ) {
        mapView.onCreate(savedInstanceState)

        // list to hold marker locations
        val features = arrayListOf<Feature>()
        if (!locations.isNullOrEmpty()) {
            locations.forEach {
                // for each location, make a marker and add to the list
                features.add(Feature.fromGeometry(Point.fromLngLat(it["long"]!!, it["lat"]!!)))
            }
        }

        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                // Map is set up and the style has loaded.
                // Now you can add data or make other map adjustments
                with(it) {
                    addSource(
                        GeoJsonSource(
                            Constants.SOURCE_ID,
                            FeatureCollection.fromFeatures(features)
                        )
                    )


                    addImageAsync(
                        Constants.ICON_ID,
                        resource.getDrawable(R.drawable.map_marker_cottage_24, null)
                    )

                    addLayer(
                        SymbolLayer(
                            Constants.LAYER_ID, Constants.SOURCE_ID
                        )
                            .withProperties(
                                iconImage(Constants.ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                            )
                    )


                }

                mapboxMap.cameraPosition =
                    CameraPosition.Builder()
                        .target(LatLng(locations[0]["lat"]!!, locations[0]["long"]!!))
                        .zoom(14.0)
                        .build()

                val uiSettings = mapboxMap.uiSettings
                uiSettings.setAllGesturesEnabled(isAllGesturesEnabled)
                uiSettings.isCompassEnabled = false
                uiSettings.isAttributionEnabled = false
                uiSettings.isQuickZoomGesturesEnabled = false
            }
        }
    }
}
