package com.example.agridirect20

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Preview
@Composable
fun GoogleMap(){
    val kenoshaCommonMarkets = LatLng(42.5856, -87.8144)
    val kenoshaHarborMarket = LatLng(42.5800, -87.8221)
    val KenoshaPublicMarket = LatLng(42.5758, -87.8163)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(kenoshaCommonMarkets, 10f)
    }
}