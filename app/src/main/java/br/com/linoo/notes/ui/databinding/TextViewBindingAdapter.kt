package br.com.linoo.notes.ui.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import br.com.linoo.notes.extensions.formataDataHora

@BindingAdapter("carregaTextoData")
fun TextView.carregaTextoDataView(date: String?) {
    text = date?.run {
        formataDataHora()
    }
}
