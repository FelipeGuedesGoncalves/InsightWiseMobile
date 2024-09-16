package com.example.insightwisemobile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import com.example.insightwisemobile.R

// FRAGMENTO DE TUTORIAL -  Posteriormente apresentarão as informações que o
// usuário deseja visualizar sobre a análise de seus websites, mas atualmente não
// apresentam lógica alguma salvo a definição de seus títulos para exibição na tela.
class TutorialFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)

        val dashboardTextView = view.findViewById<TextView>(R.id.tutorial_text)
        dashboardTextView.text = "Tutorial"

        return view
    }
}
