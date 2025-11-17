package com.example.agridirect20

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    venues: List<Venue>,
    onVenueClick: (Venue) -> Unit,
    modifier: Modifier = Modifier
) {
    // Center map around Kenosha-ish area (tweak if needed)
    val defaultCenter = LatLng(42.5856, -87.8144)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if (venues.isNotEmpty()) venues.first().address else defaultCenter,
            11f
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true
        )
    ) {
        venues.forEach { venue ->
            Marker(
                state = rememberMarkerState(position = venue.address),
                title = venue.name,
                onClick = {
                    onVenueClick(venue)
                    false // return false to also show default info window
                }
            )
        }
    }
}