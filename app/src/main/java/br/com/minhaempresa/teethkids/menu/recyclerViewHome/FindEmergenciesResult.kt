package br.com.minhaempresa.teethkids.menu.recyclerViewHome

import br.com.minhaempresa.teethkids.signUp.model.CustomResponse

data class FindEmergenciesResult(
    val emergencies: List<Emergency>,
    val response: CustomResponse
)
