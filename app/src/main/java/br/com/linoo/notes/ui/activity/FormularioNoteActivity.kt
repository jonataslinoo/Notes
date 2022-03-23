package br.com.linoo.notes.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ActivityFormularioNoteBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.activity.extensions.mostraErro
import br.com.linoo.notes.ui.viewmodel.FormularioNoteViewModel
import org.koin.android.viewmodel.ext.android.viewModel

private const val TITULO_APPBAR_EDICAO = "Editando Anotação"
private const val TITULO_APPBAR_CRIACAO = "Criando Anotação"
private const val MENSAGEM_ERRO_SALVAR = "Não foi possível salvar a Anotação"

class FormularioNoteActivity : AppCompatActivity() {

    private val viewModel by viewModel<FormularioNoteViewModel>()
    private lateinit var binding: ActivityFormularioNoteBinding
    private val noteId: Long by lazy {
        intent.getLongExtra(NOTE_ID_CHAVE, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defineContentViewBinding()
        definindoTitulo()
        preencheFormulario()
    }

    private fun defineContentViewBinding() {
        binding = ActivityFormularioNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun definindoTitulo() {
        title = if (noteId > 0) {
            TITULO_APPBAR_EDICAO
        } else {
            TITULO_APPBAR_CRIACAO
        }
    }

    private fun preencheFormulario() {
        viewModel.buscaPorId(noteId).observe(this, Observer { noteEncontrada ->
            if (noteEncontrada != null) {
                binding.activityFormularioNoteTitulo.setText(noteEncontrada.titulo)
                binding.activityFormularioNoteTexto.setText(noteEncontrada.texto)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.formulario_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.formulario_note_salva -> {
                val titulo = binding.activityFormularioNoteTitulo.text.toString()
                val texto = binding.activityFormularioNoteTexto.text.toString()
                salva(Note(noteId, titulo = titulo, texto = texto))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun salva(note: Note) {
        binding.activityFormularioNoteProgressbar.visibility = View.VISIBLE
        viewModel.salva(note).observe(this, Observer { resource ->
            if (resource.erro == null) {
                finish()
            } else {
                mostraErro(MENSAGEM_ERRO_SALVAR)
            }

            binding.activityFormularioNoteProgressbar.visibility = View.GONE
        })
    }
}
