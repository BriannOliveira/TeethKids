package br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

var avaliationslist = mutableListOf<Avaliation>()

@Parcelize
data class Avaliation(
    var time: String,
    var rating: Double,
    var comment: String,
    var nameUser: String,
) : Parcelable

