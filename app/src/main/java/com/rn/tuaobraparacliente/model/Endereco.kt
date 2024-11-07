package com.rn.tuaobraparacliente.model

data class Endereco(
    val id: Long? = null,
    val cep: String,
    val nomeLugar: String,
    val numero: String
)