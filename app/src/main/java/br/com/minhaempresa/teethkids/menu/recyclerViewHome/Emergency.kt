package br.com.minhaempresa.teethkids.menu.recyclerViewHome

import androidx.annotation.DrawableRes


var emergencyList = mutableListOf<Emergency>()

data class Emergency(
    @DrawableRes var photo: Int,
    var nameUser: String,
    var phone: String,
    var time: Int,
    val id: Int? = emergencyList.size
)