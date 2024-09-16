package com.example.insightwisemobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insightwisemobile.R

// LOGIN DE USUÁRIOS - Esta atividade cuida do login de usuários cadastrados.
// Verifica se as credenciais correspondem às armazenadas no CadastroActivity.
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_button)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Verifica se os campos de email e senha não estão vazios.
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nome de usuário e senha não podem estar vazios!", Toast.LENGTH_SHORT).show()
            } else {
                // Obtém as preferências compartilhadas armazenadas no dispositivo.
                val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val userKey = "user_$email"
                val storedUsername = sharedPreferences.getString("$userKey.username", null)
                val storedPassword = sharedPreferences.getString("$userKey.password", null)
                val storedCnpj = sharedPreferences.getString("$userKey.cnpj", null)
                val storedEmail = sharedPreferences.getString("$userKey.email", null)
                val storedPhone = sharedPreferences.getString("$userKey.phone", null)
                val storedPlan = sharedPreferences.getString("$userKey.plan", null)
                val storedPayment = sharedPreferences.getString("$userKey.payment", null)
                val storedSiteName = sharedPreferences.getString("$userKey.sitename", null)
                val storedSiteAddress = sharedPreferences.getString("$userKey.siteaddress", null)

                // Verifica se o email e a senha inseridos correspondem aos armazenados.
                if (storedEmail != null && storedPassword != null && email == storedEmail && password == storedPassword) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("username", storedUsername)
                    intent.putExtra("cnpj", storedCnpj)
                    intent.putExtra("email", email)
                    intent.putExtra("password", password)
                    intent.putExtra("phone", storedPhone)
                    intent.putExtra("plan", storedPlan)
                    intent.putExtra("payment", storedPayment)
                    intent.putExtra("sitename", storedSiteName)
                    intent.putExtra("siteaddress", storedSiteAddress)
                    // Inicia a MainActivity após a autenticação bem-sucedida.
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val registerButton = findViewById<Button>(R.id.register_button)
        // Inicia a atividade de cadastro ao clicar no botão de registro.
        registerButton.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}
