package com.example.insightwisemobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.insightwisemobile.R

// FRAGMENTO DE PERFIL - Este fragmento exibe e permite a edição do perfil do usuário,
// incluindo funcionalidades para logout e exclusão de perfil.
class ProfileFragment : Fragment() {

    // Variáveis para armazenar dados do perfil
    private var username: String? = null
    private var cnpj: String? = null
    private var email: String? = null
    private var password: String? = null
    private var phone: String? = null
    private var plan: String? = null
    private var payment: String? = null
    private var siteName: String? = null
    private var siteAddress: String? = null
    private var isEditing = false

    // Referências aos elementos da interface
    private lateinit var usernameInput: EditText
    private lateinit var cnpjInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var planInput: Spinner
    private lateinit var paymentInput: Spinner
    private lateinit var sitenameInput: EditText
    private lateinit var siteaddressInput: EditText
    private lateinit var editProfileButton: Button
    private lateinit var logoutButton: Button
    private lateinit var deleteProfileButton: Button
    private lateinit var showPasswordButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inicializando os campos de texto e botões
        usernameInput = view.findViewById(R.id.username_input)
        cnpjInput = view.findViewById(R.id.cnpj_input)
        emailInput = view.findViewById(R.id.email_input)
        passwordInput = view.findViewById(R.id.senha_input)
        phoneInput = view.findViewById(R.id.phone_input)
        planInput = view.findViewById(R.id.plan_input)
        paymentInput = view.findViewById(R.id.payment_input)
        sitenameInput = view.findViewById(R.id.sitename_input)
        siteaddressInput = view.findViewById(R.id.siteaddress_input)
        editProfileButton = view.findViewById(R.id.edit_profile_button)
        logoutButton = view.findViewById(R.id.logoutButton)
        deleteProfileButton = view.findViewById(R.id.delete_profile_button)
        showPasswordButton = view.findViewById(R.id.showPasswordButton)

        // Configurar adaptadores para os Spinners
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.plan_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            planInput.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.payment_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            paymentInput.adapter = adapter
        }

        // Carregar os dados do SharedPreferences
        loadProfileData()

        // Configurar o campo de senha para iniciar como oculto
        passwordInput.inputType =
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        showPasswordButton.setImageResource(R.drawable.passworddisabled) // Defina o ícone apropriado para senha oculta

        // Desabilitar a edição inicial
        setEditingEnabled(false)

        // Lógica para o botão Editar Perfil
        editProfileButton.setOnClickListener {
            if (isEditing) {
                saveProfileChanges()
            } else {
                setEditingEnabled(true)
            }
        }

        // Lógica para o botão de logout
        logoutButton.setOnClickListener {
            logout()
        }

        // Lógica para o botão Excluir Perfil
        deleteProfileButton.setOnClickListener {
            deleteProfile()
        }

        // Bloquear edição do email
        emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && isEditing) {
                showEmailEditWarning()
            }
        }

        // Lógica para mostrar/ocultar senha
        showPasswordButton.setOnClickListener {
            togglePasswordVisibility()
        }

        return view
    }

    // Carregar dados do perfil do SharedPreferences e preencher os campos
    private fun loadProfileData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userKey = "user_${sharedPreferences.getString("current_email", "")}"

        username = sharedPreferences.getString("$userKey.username", "")
        cnpj = sharedPreferences.getString("$userKey.cnpj", "")
        email = sharedPreferences.getString("$userKey.email", "")
        password = sharedPreferences.getString("$userKey.password", "")
        phone = sharedPreferences.getString("$userKey.phone", "")
        plan = sharedPreferences.getString("$userKey.plan", "")
        payment = sharedPreferences.getString("$userKey.payment", "")
        siteName = sharedPreferences.getString("$userKey.sitename", "")
        siteAddress = sharedPreferences.getString("$userKey.siteaddress", "")

        // Atualizando os campos de texto com os dados carregados
        usernameInput.setText(username)
        cnpjInput.setText(cnpj)
        emailInput.setText(email)
        passwordInput.setText(password)
        phoneInput.setText(phone)
        sitenameInput.setText(siteName)
        siteaddressInput.setText(siteAddress)

        // Selecionar o plano e a forma de pagamento corretos nos Spinners
        plan?.let { selectSpinnerItem(planInput, it) }
        payment?.let { selectSpinnerItem(paymentInput, it) }
    }

    // Selecionar o item correto em um Spinner
    private fun selectSpinnerItem(spinner: Spinner, itemValue: String) {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == itemValue) {
                spinner.setSelection(i)
                return
            }
        }
    }

    // Alternar visibilidade da senha
    private fun togglePasswordVisibility() {
        if (passwordInput.inputType == (android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordInput.inputType =
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            showPasswordButton.setImageResource(R.drawable.passwordenabled) // Defina o ícone apropriado para senha visível
        } else {
            passwordInput.inputType =
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            showPasswordButton.setImageResource(R.drawable.passworddisabled) // Defina o ícone apropriado para senha oculta
        }
        // Movendo o cursor para o final do texto
        passwordInput.setSelection(passwordInput.text.length)
    }

    // Habilitar ou desabilitar a edição dos campos
    private fun setEditingEnabled(enabled: Boolean) {
        isEditing = enabled
        usernameInput.isEnabled = enabled
        cnpjInput.isEnabled = enabled
        emailInput.isEnabled = enabled
        passwordInput.isEnabled = enabled
        phoneInput.isEnabled = enabled
        planInput.isEnabled = enabled
        paymentInput.isEnabled = enabled
        sitenameInput.isEnabled = enabled
        siteaddressInput.isEnabled = enabled

        if (enabled) {
            editProfileButton.text = "Confirmar Alterações"
        } else {
            editProfileButton.text = "Editar Perfil"
        }
    }

    // Mostrar aviso sobre a edição do email
    private fun showEmailEditWarning() {
        val dialogMessage =
            "Por motivos de segurança, seu e-mail só pode ser alterado pela equipe InsightWise, caso deseje prosseguir, contate-nos via email - insightwisesuporte@gmail.com"
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(dialogMessage)
            .setCancelable(true)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
        emailInput.isEnabled = false
    }

    // Salvar as alterações do perfil no SharedPreferences
    private fun saveProfileChanges() {
        val newUsername = usernameInput.text.toString()
        val newCnpj = cnpjInput.text.toString()
        val newEmail = emailInput.text.toString()
        val newPassword = passwordInput.text.toString()
        val newPhone = phoneInput.text.toString()
        val newPlan = planInput.selectedItem.toString()
        val newPayment = paymentInput.selectedItem.toString()
        val newSitename = sitenameInput.text.toString()
        val newSiteaddress = siteaddressInput.text.toString()

        // Verifica se todos os campos obrigatórios estão preenchidos
        if (newUsername.isNotEmpty() && newCnpj.isNotEmpty() && newEmail.isNotEmpty() && newPhone.isNotEmpty() && newPlan.isNotEmpty() && newPayment.isNotEmpty()) {
            val sharedPreferences =
                requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val userKey = "user_$email" // Utilizando o email como chave

            editor.putString("$userKey.username", newUsername)
            editor.putString("$userKey.cnpj", newCnpj)
            editor.putString("$userKey.email", newEmail)
            editor.putString("$userKey.password", newPassword)
            editor.putString("$userKey.phone", newPhone)
            editor.putString("$userKey.plan", newPlan)
            editor.putString("$userKey.payment", newPayment)
            editor.putString("$userKey.sitename", newSitename)
            editor.putString("$userKey.siteaddress", newSiteaddress)
            editor.apply()

            // Atualizar as variáveis locais
            username = newUsername
            cnpj = newCnpj
            email = newEmail
            password = newPassword
            phone = newPhone
            plan = newPlan
            payment = newPayment
            siteName = newSitename
            siteAddress = newSiteaddress

            // Desabilitar a edição
            setEditingEnabled(false)

            Toast.makeText(requireContext(), "Alterações salvas com sucesso!", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                requireContext(),
                "Todos os campos são obrigatórios!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Excluir perfil do usuário
    private fun deleteProfile() {
        // Inflar o layout do diálogo
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_profile, null)
        val emailInput = dialogView.findViewById<EditText>(R.id.email_confirm_input)
        val passwordInput = dialogView.findViewById<EditText>(R.id.password_confirm_input)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
        val confirmButton = dialogView.findViewById<Button>(R.id.confirm_button)

        // Construir o diálogo
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
            .setCancelable(false)

        val alertDialog = builder.create()

        // Configurar o botão Cancelar
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Configurar o botão Confirmar
        confirmButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Verificar se o email e senha estão corretos
            if (email == this.email && password == this.password) {
                // Excluir o perfil
                val sharedPreferences =
                    requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val userKey = "user_$email" // Utilizando o email como chave

                editor.remove("$userKey.username")
                editor.remove("$userKey.cnpj")
                editor.remove("$userKey.email")
                editor.remove("$userKey.password")
                editor.remove("$userKey.phone")
                editor.remove("$userKey.plan")
                editor.remove("$userKey.payment")
                editor.remove("$userKey.sitename")
                editor.remove("$userKey.siteaddress")
                editor.apply()

                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

                Toast.makeText(requireContext(), "Perfil excluído com sucesso!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Email ou senha incorretos", Toast.LENGTH_SHORT)
                    .show()
            }
            alertDialog.dismiss()
        }

        // Mostrar o diálogo
        alertDialog.show()
    }

    // Realizar logout do usuário
    private fun logout() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("current_email")
        editor.apply()

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
