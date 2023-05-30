package br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

var emergencyList = mutableListOf<Emergency>()

enum class Status{
    NEW,
    DRAFT,
    DONE
}

@Parcelize
data class Emergency(
    @DrawableRes var photo: Int,
    var nameUser: String,
    var phone: String,
    var time: Int,
    val uid: String,
    val status: Status,
    val fcmToken: String,
) : Parcelable