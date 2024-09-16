package com.example.insightwisemobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insightwisemobile.R
import com.example.insightwisemobile.controller.RetrofitInstance
import com.example.insightwisemobile.model.CnpjResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// CADASTRO DE USUÁRIOS - Esta atividade cuida do cadastro de novos usuários.
// Verifica se todos os campos estão preenchidos e se o CNPJ é válido antes de salvar os dados do usuário.
class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val registerButton = findViewById<Button>(R.id.register_button)
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val cnpjInput = findViewById<EditText>(R.id.cnpj_input)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val phoneInput = findViewById<EditText>(R.id.phone_input)
        val planInput = findViewById<Spinner>(R.id.plan_input)
        val paymentInput = findViewById<Spinner>(R.id.payment_input)

        // Configuração dos adaptadores para os Spinners com layout personalizado
        ArrayAdapter.createFromResource(
            this,
            R.array.plan_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            planInput.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.payment_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            paymentInput.adapter = adapter
        }

        // Configuração do clique no botão de cadastro
        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val cnpj = cnpjInput.text.toString()
            val email = emailInput.text.toString()
            val phone = phoneInput.text.toString()
            val plan = if (planInput.selectedItemPosition != 0) planInput.selectedItem.toString() else ""
            val payment = if (paymentInput.selectedItemPosition != 0) paymentInput.selectedItem.toString() else ""
            val siteName = "" // Valor padrão vazio
            val siteAddress = "" // Valor padrão vazio

            // Verifica se todos os campos estão preenchidos antes de prosseguir com o cadastro
            if (username.isNotEmpty() && password.isNotEmpty() && cnpj.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && plan.isNotEmpty() && payment.isNotEmpty()) {
                verificarCnpj(
                    cnpj,
                    username,
                    password,
                    email,
                    phone,
                    plan,
                    payment,
                    siteName,
                    siteAddress
                )
            } else {
                Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show()
            }
        }

        val backToLoginButton = findViewById<Button>(R.id.back_to_login_button)
        // Configuração do clique no botão para voltar ao login
        backToLoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Verificação do CNPJ usando uma chamada de API
    private fun verificarCnpj(
        cnpj: String,
        username: String,
        password: String,
        email: String,
        phone: String,
        plan: String,
        payment: String,
        siteName: String,
        siteAddress: String
    ) {
        RetrofitInstance.api.getCnpjInfo(cnpj).enqueue(object : Callback<CnpjResponse> {
            override fun onResponse(call: Call<CnpjResponse>, response: Response<CnpjResponse>) {
                if (response.isSuccessful && response.body() != null && response.body()?.error == null) {
                    salvarUsuario(
                        username,
                        password,
                        cnpj,
                        email,
                        phone,
                        plan,
                        payment,
                        siteName,
                        siteAddress
                    )
                } else {
                    Toast.makeText(
                        this@CadastroActivity,
                        "CNPJ não encontrado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CnpjResponse>, t: Throwable) {
                Toast.makeText(
                    this@CadastroActivity,
                    "Erro ao verificar CNPJ. Tente novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // Salvando as informações do usuário no SharedPreferences
    private fun salvarUsuario(
        username: String,
        password: String,
        cnpj: String,
        email: String,
        phone: String,
        plan: String,
        payment: String,
        siteName: String,
        siteAddress: String
    ) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val userKey = "user_$email" // Utilizando o email como chave
        editor.putString("$userKey.username", username)
        editor.putString("$userKey.password", password)
        editor.putString("$userKey.cnpj", cnpj)
        editor.putString("$userKey.email", email)
        editor.putString("$userKey.phone", phone)
        editor.putString("$userKey.plan", plan)
        editor.putString("$userKey.payment", payment)
        editor.putString("$userKey.sitename", siteName)
        editor.putString("$userKey.siteaddress", siteAddress)
        editor.apply()

        Toast.makeText(this, "Cadastro bem-sucedido! Faça login.", Toast.LENGTH_SHORT).show()

        // Inicia a LoginActivity após o cadastro bem-sucedido
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
