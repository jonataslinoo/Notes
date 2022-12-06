package br.com.linoo.notes.ui.activity.extensions

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

fun Activity.mostraMensagem(mensagem: String) {
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}

fun AppCompatActivity.transacaoFragment(executa: FragmentTransaction.() -> Unit) {
    val transaction = supportFragmentManager.beginTransaction()
    executa(transaction)
    transaction.commit()
}