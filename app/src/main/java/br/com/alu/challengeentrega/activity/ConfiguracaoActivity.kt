package br.com.alu.challengeentrega.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.alu.challengeentrega.R
import br.com.alu.challengeentrega.fetcher.usuario.UsuarioFetcher
import okio.IOException

class ConfiguracaoActivity : AppCompatActivity(R.layout.login_layout) {
    private val empresaFetcher = UsuarioFetcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuracao_layout)

        val excluirConta = findViewById<TextView>(R.id.text_excluir_conta_option)
        val loginActivity = Intent(this@ConfiguracaoActivity, LoginActivity::class.java)

        excluirConta.setOnClickListener {
            try {
                    empresaFetcher.deleteByEmail(intent.getStringExtra("email")!!) { response ->
                        if (response.code == 200) {
                            startActivity(loginActivity)
                        } else {
                            Log.v("[ERROR-API]", response.body?.string().toString())
                            runOnUiThread {
                                val toast = Toast.makeText(
                                    this@ConfiguracaoActivity,
                                    "Erro ao excluir conta", Toast.LENGTH_LONG
                                )
                                toast.show()
                            }
                        }
                    }
            } catch (err: IOException) {
                Log.v("[ERROR-API]", err.message.toString())

                runOnUiThread {
                    val toast = Toast.makeText(
                        this@ConfiguracaoActivity,
                        "Erro ao excluir conta", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }

        }

    }
}
