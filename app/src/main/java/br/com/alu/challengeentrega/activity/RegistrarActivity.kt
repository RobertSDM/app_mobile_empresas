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

class RegistrarActivity : AppCompatActivity(R.layout.registrar_layout) {
    private val usuarioFetcher = UsuarioFetcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginActivity = Intent(this@RegistrarActivity, LoginActivity::class.java)

        val nome = findViewById<EditText>(R.id.nome_registrar)
        val email = findViewById<EditText>(R.id.email_registrar)
        val senha = findViewById<EditText>(R.id.senha_registrar)
        val confirmarSenha = findViewById<EditText>(R.id.confirmar_senha_registrar)
        val linkLoginTela = findViewById<TextView>(R.id.entre_text)

        val btnEntrar = findViewById<Button>(R.id.button_registrar)

        btnEntrar.setOnClickListener {
            if (!Validations.validadeEmail(email.text.toString())) {
                val toast = Toast.makeText(
                    this@RegistrarActivity,
                    "O email é inválido", Toast.LENGTH_LONG
                )
                toast.show()
                return@setOnClickListener
            } else if (!Validations.validateTwoStrings(
                    senha.text.toString(),
                    confirmarSenha.text.toString()
                ) ||
                (senha.text.toString().isEmpty() || confirmarSenha.text.toString().isEmpty())
            ) {

                val toast = Toast.makeText(
                    this@RegistrarActivity,
                    "As senhas não são iguais", Toast.LENGTH_LONG
                )
                toast.show()
                return@setOnClickListener
            }

            val body = """
                {
                    "nome" : "${nome.text}",
                    "email": "${email.text}",
                    "senha": "${senha.text}"
                }
            """.trimIndent()

            try {
                usuarioFetcher.createUsuario(body.toRequestBody("application/json".toMediaTypeOrNull())) { response ->
                    if (response.code == 200) {
                        loginActivity.putExtra("email", email.text.toString())
                        startActivity(loginActivity)
                    } else {
                        Log.v("[ERROR-API]", response.body?.string().toString())
                        runOnUiThread {
                            val toast = Toast.makeText(
                                this@RegistrarActivity,
                                "Erro ao criar o usuario", Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }
                }

            } catch (err: IOException) {

                Log.v("[ERROR-API]", err.message.toString())
                runOnUiThread {
                    val toast = Toast.makeText(
                        this@RegistrarActivity,
                        "Error ao realizar a requisicao", Toast.LENGTH_LONG
                    )
                    toast.show()
                }

            }
        }

        linkLoginTela.setOnClickListener {
            startActivity(loginActivity)
        }

    }

}