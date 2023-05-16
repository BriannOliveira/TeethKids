package br.com.minhaempresa.teethkids.ui.recyclerViewHome

var emergencyList = mutableListOf<Emergency>()


class Emergency(
    var photo: Int,
    var nameUser: String,
    var phone: String,
    val id: Int? = emergencyList.size
)