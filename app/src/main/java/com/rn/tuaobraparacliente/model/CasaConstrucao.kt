package com.rn.tuaobraparacliente.model

data class CasaConstrucao(
    val id: Long? = null,
    val nome: String,
    val descricao: String,
    val horario: String,
    val urlImagemPerfil: String,
    val frete: String,
    val email: String,
    val contatoWhatsApp: String,
    val formaPagamento: String
)
