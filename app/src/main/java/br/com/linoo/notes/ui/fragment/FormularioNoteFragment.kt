package br.com.linoo.notes.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.FormularioNoteBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.activity.NOTE_ID_CHAVE
import br.com.linoo.notes.ui.fragment.extensions.mostraErro
import br.com.linoo.notes.ui.viewmodel.FormularioNoteViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TITULO_APPBAR_EDICAO = "Editando Anotação"
private const val TITULO_APPBAR_CRIACAO = "Criando Anotação"
private const val MENSAGEM_ERRO_SALVAR = "Não foi possível salvar a Anotação"

class FormularioNoteFragment : Fragment() {

    var quandoFinalizaFragment: () -> Unit = {}

    private val viewModel: FormularioNoteViewModel by viewModel { parametersOf(noteId) }
    private lateinit var binding: FormularioNoteBinding
    private val noteId: Long by lazy {
        arguments?.getLong(NOTE_ID_CHAVE) ?: 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //libera os menus para o fragment
        preencheFormulario()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FormularioNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        definindoTitulo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.formulario_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.formulario_note_salva -> {
                val titulo = binding.formularioNoteTitulo.text.toString()
                val texto = binding.formularioNoteTexto.text.toString()
                salva(Note(noteId, titulo = titulo, texto = texto))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun definindoTitulo() {
        activity?.title = if (noteId > 0) {
            TITULO_APPBAR_EDICAO
        } else {
            TITULO_APPBAR_CRIACAO
        }
    }

    private fun preencheFormulario() {
        viewModel.buscaPorId(noteId).observe(this, Observer { noteEncontrada ->
            if (noteEncontrada != null) {
                binding.formularioNoteTitulo.setText(noteEncontrada.titulo)
                binding.formularioNoteTexto.setText(noteEncontrada.texto)
            }
        })
    }

    private fun salva(note: Note) {
        binding.formularioNoteProgressbar.visibility = View.VISIBLE
        viewModel.salva(note).observe(this, Observer { resource ->
            if (resource.erro == null) {
                quandoFinalizaFragment()
            } else {
                mostraErro(MENSAGEM_ERRO_SALVAR)
            }

            binding.formularioNoteProgressbar.visibility = View.GONE
        })
    }
}