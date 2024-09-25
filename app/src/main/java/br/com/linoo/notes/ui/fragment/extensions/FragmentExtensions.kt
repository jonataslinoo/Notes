package br.com.linoo.notes.ui.fragment.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.mostraMensagem(mensagem: String) {
    Toast.makeText(
        context,
        mensagem,
        Toast.LENGTH_SHORT
    ).show()
}

fun Fragment.transacaoNavController(executa: NavController.() -> Unit) {
    val controller = findNavController()
    executa(controller)
}

fun Fragment.hideKeyboard(view: View) {
    val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, 0)
}
