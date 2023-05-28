package br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize


var emergencyList = mutableListOf<Emergency>()

@Parcelize
data class Emergency(
    @DrawableRes var photo: Int,
    var nameUser: String,
    var phone: String,
    var time: Int,
    val id: Int? = emergencyList.size
) : Parcelable