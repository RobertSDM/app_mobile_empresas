package br.com.alu.challengeentrega.element

data class AnaliseMercado(
    val id: Int,
    val empresa_id: String,
    val media_navegacao: Int,
    val taxa_redirecionamento: Int,
    val empresa: Empresa,
    val cliques: List<Cliques>
)