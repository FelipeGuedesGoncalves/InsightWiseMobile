package com.example.insightwisemobile.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Interface que define os métodos para acessar a API
interface ApiService {
    // Método GET para buscar informações de CNPJ
    @GET("buscarcnpj")
    fun getCnpjInfo(@Query("cnpj") cnpj: String): Call<CnpjResponse>
}
