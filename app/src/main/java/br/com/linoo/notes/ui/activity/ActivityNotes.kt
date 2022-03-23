package br.com.linoo.notes.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ActivityNotesBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.activity.extensions.transacaoFragment
import br.com.linoo.notes.ui.fragment.ListaNotesFragment
import br.com.linoo.notes.ui.fragment.VisualizaNoteFragment

private const val TAG_FRAGMENT_VISUALIZA_NOTE = "visualizaNote"

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
            add(R.id.activity_notes_container_primario, ListaNotesFragment())
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
        }
    }

    private fun configuraListaNote(fragment: ListaNotesFragment) {
        fragment.quandoNoteSelecionada = this::abreVisualizadorNoticia
        fragment.quandoFabSalvaNoteClicada = this::abreFormularioModoCriacao
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

    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoteActivity::class.java)
        startActivity(intent)
    }

    private fun configuraVisualizaNote(fragment: VisualizaNoteFragment) {
        fragment.quandoSelecionaMenuEdicao = this::abreFormularioEdicao
        fragment.quandoFinalizaActivity = {
            supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_VISUALIZA_NOTE)?.let { fragment ->
                removeFragment(fragment)
            }
        }
    }

    private fun abreFormularioEdicao(note: Note) {
        val intent = Intent(this, FormularioNoteActivity::class.java)
        intent.putExtra(NOTE_ID_CHAVE, note.id)
        startActivity(intent)
    }

    private fun removeFragment(fragment: Fragment) {
        transacaoFragment { remove(fragment) }
        supportFragmentManager.popBackStack()
    }
}