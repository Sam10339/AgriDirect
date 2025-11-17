package com.example.agridirect20

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.runBlocking

// Define a custom Saver for MapUiSettings
val MapUiSettingsSaver: Saver<MapUiSettings, Any> = Saver(
    save = { settings ->
        // Save each individual setting as a list of simple types (e.g., Boolean)
        listOf(
            settings.compassEnabled,
            settings.indoorLevelPickerEnabled,
            settings.mapToolbarEnabled,
            settings.myLocationButtonEnabled,
            settings.rotationGesturesEnabled,
            settings.scrollGesturesEnabled,
            settings.scrollGesturesEnabledDuringRotateOrZoom,
            settings.tiltGesturesEnabled,
            settings.zoomControlsEnabled,
            settings.zoomGesturesEnabled
        )
    },
    restore = { list ->
        // Restore the settings from the list
        @Suppress("UNCHECKED_CAST")
        list as List<Boolean> // Cast the list back to List<Boolean>
        MapUiSettings(
            compassEnabled = list[0],
            indoorLevelPickerEnabled = list[1],
            mapToolbarEnabled = list[2],
            myLocationButtonEnabled = list[3],
            rotationGesturesEnabled = list[4],
            scrollGesturesEnabled = list[5],
            scrollGesturesEnabledDuringRotateOrZoom = list[6],
            tiltGesturesEnabled = list[7],
            zoomControlsEnabled = list[8],
            zoomGesturesEnabled = list[9]
        )
    }
)

val MapPropertiesSaver: Saver<MapProperties, Any> = Saver(
    save = { settings ->
        // Save each individual setting as a list of simple types (e.g., Boolean)
        listOf(
            settings.isBuildingEnabled,
            settings.isIndoorEnabled,
            settings.isMyLocationEnabled,
            settings.isTrafficEnabled,
            settings.latLngBoundsForCameraTarget,
            settings.mapStyleOptions,
            settings.mapType,
            settings.maxZoomPreference,
            settings.minZoomPreference
        )
    },
    restore = { list ->
        // Restore the settings from the list
        @Suppress("UNCHECKED_CAST")
        list as List<Any?> // Cast the list back to List<Boolean>
        MapProperties(
            isBuildingEnabled = list[0] as Boolean,
            isIndoorEnabled = list[1] as Boolean,
            isMyLocationEnabled = list[2] as Boolean,
            isTrafficEnabled = list[3] as Boolean,
            latLngBoundsForCameraTarget = list[4] as LatLngBounds?,
            mapStyleOptions = list[5] as MapStyleOptions?,
            mapType = list[6] as MapType,
            maxZoomPreference = list[7] as Float,
            minZoomPreference = list[8] as Float,
        )
    }
)

suspend fun retrieveVenues(): List<Venue> {
    return FirebaseVenueRepository.getVenues()
}

@Preview
@Composable
fun MapScreen(){
    val kenoshaCommonMarkets = LatLng(42.5856, -87.8144)
    var venues by remember { mutableStateOf<List<Venue>>(emptyList()) }
    runBlocking {
        venues = retrieveVenues()
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(42.5856, -87.8144), 14f)
    }
    // Use rememberSaveable with the custom saver
    var uiSettings by rememberSaveable(stateSaver = MapUiSettingsSaver) {
        mutableStateOf(MapUiSettings(
            compassEnabled = true,
            indoorLevelPickerEnabled = true,
            mapToolbarEnabled = true,
            myLocationButtonEnabled = true,
            rotationGesturesEnabled = true,
            scrollGesturesEnabled = true,
            scrollGesturesEnabledDuringRotateOrZoom = true,
            tiltGesturesEnabled = true,
            zoomControlsEnabled = true,
            zoomGesturesEnabled = true
        ))
    }
    var properties by rememberSaveable(stateSaver = MapPropertiesSaver) {
        mutableStateOf(MapProperties(
            isBuildingEnabled=false,
            isIndoorEnabled=false,
            isMyLocationEnabled=false,
            isTrafficEnabled=false,
            latLngBoundsForCameraTarget=null,
            mapStyleOptions=null,
            mapType=MapType.NORMAL,
            maxZoomPreference=21.0F,
            minZoomPreference=3.0F
        ))
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(1f), // Fraction of a full screen
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) { for (item in venues) {
        val latitude = item.address.latitude
        val longitude = item.address.longitude
        Marker(
            state = rememberMarkerState(position = (LatLng(latitude, longitude))),
            title = item.name
        )
    }
    }
}