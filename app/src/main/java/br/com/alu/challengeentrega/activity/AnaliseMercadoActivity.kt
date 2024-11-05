package br.com.alu.challengeentrega.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.alu.challengeentrega.R
import br.com.alu.challengeentrega.fetcher.usuario.UsuarioFetcher
import br.com.alu.challengeentrega.element.Usuario
import com.google.gson.Gson
import okio.IOException

class AnaliseMercadoActivity : AppCompatActivity(R.layout.analise_mercado_layout) {
    private val usuarioFetcher = UsuarioFetcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cliques = findViewById<TextView>(R.id.cliques_view)
        val tempoMedioNav = findViewById<TextView>(R.id.textView3)
        val btnConfig = findViewById<Button>(R.id.analise_mercado_config_btn)
        val btnProdutos = findViewById<Button>(R.id.adicionar_produtos)

        val taxaRedirecionamento = findViewById<TextView>(R.id.taxa_redirecionamento)
        val progress = findViewById<ProgressBar>(R.id.progressBar)

        val configuracaoActivity =
            Intent(this@AnaliseMercadoActivity, ConfiguracaoActivity::class.java)

        val produtosVendidosActivity =
            Intent(this@AnaliseMercadoActivity, ProdutosVendidosActivity::class.java)

        btnProdutos.setOnClickListener {
            produtosVendidosActivity.putExtra("email", intent.getStringExtra("email")!!)
            startActivity(produtosVendidosActivity)
        }

        val gson = Gson()

        try {
            usuarioFetcher.findByEmail(intent.getStringExtra("email")!!) { response ->
                if (response.code == 200) {
                    val jsonContent = response.body?.string()
                    val usuario: Usuario =
                        gson.fromJson(jsonContent, Usuario::class.java)



                    runOnUiThread {
                        cliques.text =
                            usuario.empresa?.analise_mercado!!.cliques[0].cliques.toString()
                        tempoMedioNav.text =
                            usuario.empresa.analise_mercado.media_navegacao.toString() + "min"
                        taxaRedirecionamento.text =
                            usuario.empresa.analise_mercado.taxa_redirecionamento.toString() + "%"
                        progress.progress =
                            usuario.empresa.analise_mercado.taxa_redirecionamento.toInt()
                    }
                } else {
                    runOnUiThread {
                        val toast = Toast.makeText(
                            this@AnaliseMercadoActivity,
                            "Erro ao realizar a requisição para o servidor",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
            }
        } catch (err: IOException) {
            Log.v("[API-ERROR]", err.message.toString())

            runOnUiThread {
                val toast = Toast.makeText(
                    this@AnaliseMercadoActivity,
                    "Erro ao realizar a requisição para o servidor",
                    Toast.LENGTH_LONG
                )
                toast.show()
            }
        }


        btnConfig.setOnClickListener {
            configuracaoActivity.putExtra("email", intent.extras?.getString("email"))
            startActivity(configuracaoActivity)
        }
    }
}