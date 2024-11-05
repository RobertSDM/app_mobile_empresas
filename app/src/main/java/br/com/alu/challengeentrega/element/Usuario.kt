package br.com.alu.challengeentrega.element

data class Usuario(
    val id: String,
    val email: String,
    val senha: String,
    val nome: String,
    val empresa: Empresa?
)

