package com.rn.tuaobraparacliente.model

data class Cliente(
    val id: Long? = null,
    val nome: String? = null,
    val email: String,
    val contatoWhatsApp: String? = null,
    val endereco: Endereco? = null,
    val firebaseUid: String? = null
)
