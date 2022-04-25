package com.github.mstavares.cm.fichas

import com.google.android.gms.location.LocationResult

interface OnLocationChangedListener {

    fun onLocationChanged(latitude: Double, longitude: Double)

}
