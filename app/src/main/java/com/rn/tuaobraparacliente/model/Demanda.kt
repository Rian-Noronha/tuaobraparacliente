package com.rn.tuaobraparacliente.model

data class Demanda(
    val id: Long? = null,
    val detalhes: String,
    val trabalhoSerFeito: String,
    val dataPublicacao: String,
    val endereco: Endereco
)