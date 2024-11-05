package br.com.alu.challengeentrega.activity

import androidx.appcompat.app.AppCompatActivity
import br.com.alu.challengeentrega.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog

class ProdutosVendidosActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var companyEmail: String
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos_vendidos)
        companyEmail = intent.getStringExtra("email")!!

        sharedPreferences = getSharedPreferences("produtos_vendidos", Context.MODE_PRIVATE)

        val produtosLayout: LinearLayout = findViewById(R.id.produtos_layout)
        val nomeProdutoEditText: EditText = findViewById(R.id.nome_produto_edittext)
        val adicionarProdutoButton: Button = findViewById(R.id.adicionar_produto_button)

        atualizarListaProdutos(produtosLayout)

        adicionarProdutoButton.setOnClickListener {
            val nomeProduto = nomeProdutoEditText.text.toString()
            if (nomeProduto.isNotBlank()) {
                adicionarProduto(nomeProduto)
                atualizarListaProdutos(produtosLayout)
                nomeProdutoEditText.text.clear()
            } else {
                Toast.makeText(this, "Por favor, insira o nome do produto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun adicionarProduto(nomeProduto: String) {
        val produtos = obterProdutos().apply {
            add(nomeProduto)
        }
        salvarProdutos(produtos)
        Toast.makeText(this, "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show()
    }

    private fun removerProduto(nomeProduto: String, produtosLayout: LinearLayout) {
        val produtos = obterProdutos()

        if (produtos.contains(nomeProduto)) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmação de Exclusão")
            builder.setMessage("Tem certeza que deseja excluir o produto '$nomeProduto'?")

            builder.setPositiveButton("Sim") { _, _ ->
                produtos.remove(nomeProduto)
                salvarProdutos(produtos)
                atualizarListaProdutos(produtosLayout)
                Toast.makeText(this, "Produto removido com sucesso", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss() // Fecha o diálogo sem fazer nada
            }

            builder.create().show()
        }
    }

    private fun obterProdutos(): MutableList<String> {
        val json = sharedPreferences.getString("produtos $companyEmail", "[]") ?: "[]"
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    private fun salvarProdutos(produtos: MutableList<String>) {
        val json = gson.toJson(produtos)
        sharedPreferences.edit().putString("produtos $companyEmail", json).apply()
    }

    private fun atualizarListaProdutos(produtosLayout: LinearLayout) {
        produtosLayout.removeAllViews()
        val produtos = obterProdutos()

        for (produto in produtos) {
            val textView = TextView(this)
            textView.text = produto

            val removeButton = Button(this)
            removeButton.text = "Remover"
            removeButton.setOnClickListener {
                removerProduto(produto, produtosLayout)
            }

            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            horizontalLayout.addView(textView)
            horizontalLayout.addView(removeButton)

            produtosLayout.addView(horizontalLayout)
        }
    }
}
