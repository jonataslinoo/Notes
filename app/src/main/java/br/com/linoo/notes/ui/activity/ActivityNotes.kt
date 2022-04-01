package br.com.linoo.notes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ActivityNotesBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.activity.extensions.transacaoFragment
import br.com.linoo.notes.ui.fragment.FormularioNoteFragment
import br.com.linoo.notes.ui.fragment.ListaNotesFragment
import br.com.linoo.notes.ui.fragment.VisualizaNoteFragment

private const val TAG_FRAGMENT_VISUALIZA_NOTE = "visualizaNote"
private const val TAG_FRAGMENT_FORMULARIO_NOTE = "formularioNote"

class ActivityNotes : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defineContentViewBinding()
        configuraFragmentPeloEstado(savedInstanceState)
    }

    private fun defineContentViewBinding() {
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun configuraFragmentPeloEstado(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            abreListaNotes()
        } else {
            tentaReabrirFragmentVisualizaNote()
        }
    }

    private fun abreListaNotes() {
        transacaoFragment {
            replace(R.id.activity_notes_container_primario, ListaNotesFragment())
        }
    }

    private fun tentaReabrirFragmentVisualizaNote() {
        supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_VISUALIZA_NOTE)?.let { fragment ->
            val argumentos = fragment.arguments
            val novoFragment = VisualizaNoteFragment()
            novoFragment.arguments = argumentos
            removeFragment(fragment)
            transacaoFragment {
                val container = configuraContainerFragmentVisualizaNote()
                replace(container, novoFragment, TAG_FRAGMENT_VISUALIZA_NOTE)
            }
        }
    }

    private fun FragmentTransaction.configuraContainerFragmentVisualizaNote(): Int {
        if (binding.activityNotesContainerSecundario != null) {
            return R.id.activity_notes_container_secundario
        }
        addToBackStack(null)
        return R.id.activity_notes_container_primario
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is ListaNotesFragment -> {
                configuraListaNote(fragment)
            }
            is VisualizaNoteFragment -> {
                configuraVisualizaNote(fragment)
            }
            is FormularioNoteFragment -> {
                configuraFormularioFragment(fragment)
            }
        }
    }

    private fun configuraFormularioFragment(fragment: FormularioNoteFragment) {

    }

    private fun configuraListaNote(fragment: ListaNotesFragment) {
        fragment.quandoNoteSelecionada = this::abreVisualizadorNoticia
//        fragment.quandoFabSalvaNoteClicada = this::abreFormularioModoCriacao
        fragment.quandoFabSalvaNoteClicada = this::abreFormulario
    }

    private fun abreVisualizadorNoticia(note: Note) {
        val fragment = VisualizaNoteFragment()
        val dados = Bundle()
        dados.putLong(NOTE_ID_CHAVE, note.id)
        fragment.arguments = dados
        transacaoFragment {
            val container = configuraContainerFragmentVisualizaNote()
            replace(container, fragment, TAG_FRAGMENT_VISUALIZA_NOTE)
        }
    }

    private fun abreFormulario(note: Note?) {
        val fragment = FormularioNoteFragment()
        val dados = Bundle()
        dados.putLong(NOTE_ID_CHAVE, note?.id ?: 0L)
        fragment.arguments = dados
        transacaoFragment {
            addToBackStack(null)
            replace(
                R.id.activity_notes_container_primario,
                fragment,
                TAG_FRAGMENT_FORMULARIO_NOTE
            )
        }
    }

    private fun configuraVisualizaNote(fragment: VisualizaNoteFragment) {
        fragment.quandoSelecionaMenuEdicao = this::abreFormulario
        fragment.quandoFinalizaActivity = {
            supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_VISUALIZA_NOTE)?.let { fragment ->
                removeFragment(fragment)
            }
        }
    }

    private fun removeFragment(fragment: Fragment) {
        transacaoFragment { remove(fragment) }
        supportFragmentManager.popBackStack()
    }
}