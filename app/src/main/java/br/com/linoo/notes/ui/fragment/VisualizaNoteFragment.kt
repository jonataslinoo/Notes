package br.com.linoo.notes.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.VisualizaNoteBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.activity.NOTE_ID_CHAVE
import br.com.linoo.notes.ui.fragment.extensions.mostraErro
import br.com.linoo.notes.ui.viewmodel.VisualizaNoteViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val NOTE_NAO_ENCONTRADA = "Anotação não encontrada"
private const val MENSAGEM_FALHA_REMOCAO = "Não foi possível remover a anotação"
private const val TITULO_APPBAR = "Anotação"

class VisualizaNoteFragment : Fragment() {

    var quandoSelecionaMenuEdicao: (note: Note) -> Unit = {}
    var quandoFinalizaActivity: () -> Unit = {}

    private val viewModel: VisualizaNoteViewModel by viewModel { parametersOf(noteId) }
    private lateinit var binding: VisualizaNoteBinding
    private val noteId: Long by lazy {
        arguments?.getLong(NOTE_ID_CHAVE)
            ?: throw IllegalArgumentException("Id inválido")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //libera os menus para o fragment
        verificaIdDaNote()
        buscaNoteSelecionada()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = VisualizaNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = TITULO_APPBAR
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.visualiza_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.visualiza_note_menu_edita -> {
                viewModel.noteEncontrada.value?.let(quandoSelecionaMenuEdicao)
            }
            R.id.visualiza_note_menu_remove -> remove()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun remove() {
        viewModel.remove().observe(this, Observer { resource ->
            if (resource.erro == null) {
                quandoFinalizaActivity()
            } else {
                mostraErro(MENSAGEM_FALHA_REMOCAO)
            }
        })
    }

    private fun buscaNoteSelecionada() {
        viewModel.noteEncontrada.observe(this, Observer { noteEncontrada ->
            noteEncontrada?.let {
                preencheCampos(it)
            }
        })
    }

    private fun preencheCampos(note: Note) {
        binding.visualizaNoteTitulo.text = note.titulo
        binding.visualizaNoteTexto.text = note.texto
    }

    private fun verificaIdDaNote() {
        if (noteId == 0L) {
            mostraErro(NOTE_NAO_ENCONTRADA)
            quandoFinalizaActivity()
        }
    }
}