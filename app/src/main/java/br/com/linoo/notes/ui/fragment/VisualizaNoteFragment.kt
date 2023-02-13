package br.com.linoo.notes.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.VisualizaNoteBinding
import br.com.linoo.notes.ui.fragment.extensions.mostraMensagem
import br.com.linoo.notes.ui.fragment.extensions.transacaoNavController
import br.com.linoo.notes.ui.viewmodel.VisualizaNoteViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val NOTE_NAO_ENCONTRADA = "Anotação não encontrada"
private const val MENSAGEM_FALHA_REMOCAO = "Não foi possível remover a anotação"
private const val TITULO_APPBAR = "Anotação"

class VisualizaNoteFragment : Fragment() {

    private val arguments by navArgs<VisualizaNoteFragmentArgs>()
    private val noteId: Long by lazy { arguments.noteId }
    private val viewModel: VisualizaNoteViewModel by viewModel { parametersOf(noteId) }
    private lateinit var viewDataBinding: VisualizaNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //libera os menus para o fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = VisualizaNoteBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        verificaIdDaNote()
        buscaNoteSelecionada()
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
            R.id.visualiza_note_menu_edita -> vaiParaEdicaoFormulario()
            R.id.visualiza_note_menu_remove -> remove()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun vaiParaEdicaoFormulario() {
        viewModel.noteFind.value?.let { noteReceived ->
            transacaoNavController {
                navigate(
                    VisualizaNoteFragmentDirections.acaoVisualizaNoteParaFormularioNote(
                        noteReceived.id
                    )
                )
            }
        }
    }

    private fun remove() {
        viewModel.remove().observe(this, Observer { resource ->
            if (resource.erro == null) {
                endFragment()
            } else {
                mostraMensagem(MENSAGEM_FALHA_REMOCAO)
            }
        })
    }

    private fun buscaNoteSelecionada() {
        viewModel.noteFind.observe(this, Observer { noteFind ->
            noteFind?.let {
                viewDataBinding.note = noteFind
            }
        })
    }

    private fun verificaIdDaNote() {
        if (!temIdValido()) {
            mostraMensagem(NOTE_NAO_ENCONTRADA)
            endFragment()
        }
    }

    private fun temIdValido() = noteId != 0L

    private fun endFragment() {
        transacaoNavController {
            navigate(VisualizaNoteFragmentDirections.acaoVisualizaNoteParaListaNotes())
        }
    }
}