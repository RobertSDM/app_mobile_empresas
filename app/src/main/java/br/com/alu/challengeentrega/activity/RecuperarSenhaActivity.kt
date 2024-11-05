package br.com.alu.challengeentrega.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.alu.challengeentrega.R
import br.com.alu.challengeentrega.fetcher.usuario.UsuarioFetcher
import br.com.alu.challengeentrega.util.Validations
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

class RecuperarSenhaActivity : AppCompatActivity(R.layout.recuperar_senha_layout) {
    private val usuarioFetcher = UsuarioFetcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginActivity = Intent(this@RecuperarSenhaActivity, LoginActivity::class.java)

        val email = findViewById<EditText>(R.id.email)
        val novaSenha = findViewById<EditText>(R.id.nova_senha)
        val confirmarNovaSenha = findViewById<EditText>(R.id.confirmar_nova_senha)

        val linkLoginActivity = findViewById<TextView>(R.id.textView7)

        val btnTrocarSenha = findViewById<Button>(R.id.trocar_senha_btn)

        btnTrocarSenha.setOnClickListener {
            if (!Validations.validadeEmail(email.text.toString())) {
                val toast = Toast.makeText(
                    this@RecuperarSenhaActivity,
                    "O email é inválido", Toast.LENGTH_LONG
                )
                toast.show()
                return@setOnClickListener
            } else if (!Validations.validateTwoStrings(
                    novaSenha.text.toString(),
                    confirmarNovaSenha.text.toString()
                ) &&
                (novaSenha.text.toString().isEmpty() || confirmarNovaSenha.text.toString()
                    .isEmpty())
            ) {

                val toast = Toast.makeText(
                    this@RecuperarSenhaActivity,
                    "As senhas não são iguais", Toast.LENGTH_LONG
                )
                toast.show()
                return@setOnClickListener
            }

            val body = """
                {
                    "novaSenha" : "${novaSenha.text}",
                    "email": "${email.text}"
                }
            """.trimIndent()

            try {
                usuarioFetcher.updateSenha(body.toRequestBody("application/json".toMediaTypeOrNull())) { response ->
                    if (response.code == 200) {
                        loginActivity.putExtra("email", email.text.toString())
                        startActivity(loginActivity)
                    } else {
                        Log.v("[ERROR-API]", response.body?.string().toString())
                        runOnUiThread {
                            val toast = Toast.makeText(
                                this@RecuperarSenhaActivity,
                                "Erro ao atualizar a senha", Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }
                }
            } catch (err: IOException) {
                Log.v("[ERROR-API]", err.message.toString())
                runOnUiThread {
                    val toast = Toast.makeText(
                        this@RecuperarSenhaActivity,
                        "Error ao realizar a requisicao", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }

        }

        linkLoginActivity.setOnClickListener {
            startActivity(loginActivity)
        }

    }

}