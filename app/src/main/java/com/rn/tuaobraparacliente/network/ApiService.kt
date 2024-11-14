package com.rn.tuaobraparacliente.network

import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.model.Demanda
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/api/demandacliente")
    fun salvarDemandaCliente(@Body demanda: Demanda): Call<Void>

    @POST("/api/cliente")
    fun cadastrarCliente(@Body cliente: Cliente): Call<Void>

    @GET("/api/demandascliente/email/{email}")
    fun listarDemandasCliente(@Path("email") email: String): Call<List<Demanda>>
}