package br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation

import android.os.Parcelable
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.Time
import kotlinx.parcelize.Parcelize

var avaliationslist = mutableListOf<Avaliation>()

@Parcelize
data class Avaliation(
    var time: Time,
    var rate: Double,
    var comment: String,
    var name: String,
    val uid_dentista: String,
) : Parcelable

