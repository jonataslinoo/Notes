package br.com.linoo.notes.ui.fragment.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.mostraMensagem(mensagem: String) {
    Toast.makeText(
        context,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}

fun Fragment.transacaoNavController(executa: NavController.() -> Unit) {
    val controller = findNavController()
    executa(controller)
}