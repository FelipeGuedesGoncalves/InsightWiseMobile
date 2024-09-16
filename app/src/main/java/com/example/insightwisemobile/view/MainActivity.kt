package com.example.insightwisemobile.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.insightwisemobile.R
import com.example.insightwisemobile.databinding.ActivityMainBinding

// ATIVIDADE PRINCIPAL - Esta atividade cuida da navegação entre os diferentes fragmentos da aplicação.
// Inicializa e configura a navegação, além de salvar o email do usuário atual nas preferências compartilhadas.
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados do usuário passados pela Intent
        val username = intent.getStringExtra("username")
        val cnpj = intent.getStringExtra("cnpj")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val phone = intent.getStringExtra("phone")
        val plan = intent.getStringExtra("plan")
        val payment = intent.getStringExtra("payment")
        val sitename = intent.getStringExtra("sitename")
        val siteaddress = intent.getStringExtra("siteaddress")

        // Salva o email atual nas preferências compartilhadas
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("current_email", email)
        editor.apply()

        // Configura o NavController para usar o fragmento de navegação
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Define a navegação inicial para DashboardFragment, passando todos os dados como argumentos
        val bundle = Bundle().apply {
            putString("username", username)
            putString("cnpj", cnpj)
            putString("email", email)
            putString("password", password)
            putString("phone", phone)
            putString("plan", plan)
            putString("payment", payment)
            putString("sitename", sitename)
            putString("siteaddress", siteaddress)
        }
        navController.setGraph(R.navigation.nav_graph, bundle)

        // Configura botões para alternar entre DashboardFragment e ProfileFragment
        binding.dashboardButton.setOnClickListener {
            navController.navigate(R.id.dashboardFragment, bundle) // Navega para DashboardFragment
        }

        binding.profileButton.setOnClickListener {
            navController.navigate(R.id.profileFragment, bundle) // Navega para ProfileFragment
        }

        binding.relatorioButton.setOnClickListener {
            navController.navigate(R.id.relatorioFragment, bundle) // Navega para RelatorioFragment
        }

        binding.heatmapButton.setOnClickListener {
            navController.navigate(R.id.analiseindividualFragment, bundle) // Navega para AnaliseIndividualFragment
        }

        binding.tutorialButton.setOnClickListener {
            navController.navigate(R.id.tutorialFragment, bundle) // Navega para TutorialFragment
        }
    }
}
