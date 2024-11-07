package com.rn.tuaobraparacliente.model

data class Pedreiro(
    val id: Long? = null,
    val urlImagemPerfil: String? = null,
    val descricao: String,
    val nome: String,
    val contatoWhatsApp: String,
    val email: String,
    val endereco: Endereco,
    val especialidade: List<Especialidade>? = null,
    val demandas: List<Demanda>? = null

)
