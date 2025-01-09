package com.rn.tuaobraparacliente.network

import com.rn.tuaobraparacliente.model.CasaConstrucao
import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.model.Demanda
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("/api/casasconstrucao")
    fun listarCasas(): Call<List<CasaConstrucao>>

    @POST("/api/demandacliente")
    fun salvarDemandaCliente(@Body demanda: Demanda): Call<Void>

    @POST("/api/cliente")
    fun cadastrarCliente(@Body cliente: Cliente): Call<Void>

    @GET("/api/demandascliente/email/{email}")
    fun listarDemandasCliente(@Path("email") email: String): Call<List<Demanda>>

    @PUT("/api/demanda/{id}/{email}")
    fun atualizarDemanda(@Path("id") demandaId: Long, @Path("email") email: String, @Body demanda: Demanda): Call<Demanda>
}