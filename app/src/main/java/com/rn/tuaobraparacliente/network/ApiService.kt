package com.rn.tuaobraparacliente.network

import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.model.Demanda
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/demandacliente")
    fun salvarDemandaCliente(@Body demanda: Demanda): Call<Void>

    @POST("/api/cliente")
    fun cadastrarCliente(@Body cliente: Cliente): Call<Void>
}