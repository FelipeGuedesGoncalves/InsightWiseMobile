package com.example.insightwisemobile.model

import com.google.gson.annotations.SerializedName

// Classe de dados que representa a resposta de uma consulta CNPJ
data class CnpjResponse(
    // Anotação para mapear o campo "CNPJ" do JSON para a propriedade 'cnpj' da classe
    @SerializedName("CNPJ")
    val cnpj: String?,
    // Anotação para mapear o campo "RAZAO SOCIAL" do JSON para a propriedade 'razaoSocial' da classe
    @SerializedName("RAZAO SOCIAL")
    val razaoSocial: String?,
    // Anotação para mapear o campo "NOME FANTASIA" do JSON para a propriedade 'nomeFantasia' da classe
    @SerializedName("NOME FANTASIA")
    val nomeFantasia: String?,
    // Anotação para mapear o campo "STATUS" do JSON para a propriedade 'status' da classe
    @SerializedName("STATUS")
    val status: String?,
    // Anotação para mapear o campo "error" do JSON para a propriedade 'error' da classe
    @SerializedName("error")
    val error: String?
)
