package com.example.insightwisemobile.controller

import com.example.insightwisemobile.model.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton RetrofitInstance - Gerencia a criação do Retrofit e configurações relacionadas para a aplicação.
object RetrofitInstance {
    // Base URL da API pública
    private const val BASE_URL = "https://api-publica.speedio.com.br/"

    // Interceptor de logging para registrar o corpo das solicitações e respostas HTTP
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Definir o nível de log para BODY para registrar o conteúdo das requisições e respostas
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP OkHttp configurado com o interceptor de logging
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Instância Retrofit configurada com a URL base, cliente HTTP e conversor Gson
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // Adicionar cliente HTTP personalizado
            .addConverterFactory(GsonConverterFactory.create()) // Adicionar conversor Gson
            .build()
    }

    // Interface ApiService criada pelo Retrofit para realizar chamadas de rede
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
