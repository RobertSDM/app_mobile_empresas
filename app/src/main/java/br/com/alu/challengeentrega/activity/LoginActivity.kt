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

class LoginActivity : AppCompatActivity(R.layout.login_layout) {

    private val usuarioFetcher = UsuarioFetcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val emailIntent = intent.getStringExtra("email") != null
        val recuperarSenhaActivity = Intent(this@LoginActivity, RecuperarSenhaActivity::class.java)
        val analiseMercadoActivity = Intent(this@LoginActivity, AnaliseMercadoActivity::class.java)

        val linkRegistrarTela = findViewById<TextView>(R.id.entre_text)
        val linkRecuperarSenha = findViewById<TextView>(R.id.esqueci_senha_text)

        val email = findViewById<EditText>(R.id.email_input)
        val senha = findViewById<EditText>(R.id.senha_input)

        val btnEntrar = findViewById<Button>(R.id.button_entrar)

        if (emailIntent) {
            email.setText(intent.getStringExtra("email"))
        }

        btnEntrar.setOnClickListener {
            if (!Validations.validadeEmail(email.text.toString())) {
                runOnUiThread {

                    val toast = Toast.makeText(
                        this@LoginActivity,
                        "O email é inválido", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
                return@setOnClickListener
            } else if (senha.text.toString().isEmpty()) {

                runOnUiThread {
                    val toast = Toast.makeText(
                        this@LoginActivity,
                        "A senha não pode ser nula", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
                return@setOnClickListener
            }

            val body = """
                {
                    "email": "${email.text}",
                    "senha": "${senha.text}"
                }
            """.trimIndent()

            try {
                usuarioFetcher.loginUsuario(body.toRequestBody("application/json".toMediaTypeOrNull())) { response ->
                    if (response.code == 200) {
                        analiseMercadoActivity.putExtra("email", email.text.toString())
                        startActivity(analiseMercadoActivity)
                    } else {
                        Log.v("[ERROR-API]", response.body?.string().toString())

                        runOnUiThread {
                            val toast = Toast.makeText(
                                this@LoginActivity,
                                "Senha ou email são inválidos", Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }
                }
            } catch (err: IOException) {
                Log.v("[ERROR-API]", err.message.toString())

                runOnUiThread {
                    val toast = Toast.makeText(
                        this@LoginActivity,
                        "Error ao realizar a requisicao", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }

        }

        linkRecuperarSenha.setOnClickListener {
            startActivity(recuperarSenhaActivity)
        }

        linkRegistrarTela.setOnClickListener {
            val registrarActivity = Intent(this, RegistrarActivity::class.java)
            startActivity(registrarActivity)
        }
    }
}