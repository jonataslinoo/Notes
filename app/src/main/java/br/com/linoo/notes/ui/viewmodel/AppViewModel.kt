package br.com.linoo.notes.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {

    private val _selectedMenuId = MutableLiveData<Int>()
    val selectedMenuId: LiveData<Int> = _selectedMenuId

    fun updateSelectedMenuId(menuId: Int) {
        _selectedMenuId.value = menuId
    }

    val componentes: LiveData<ComponentesVisuais> get() = _componentes

    private var _componentes: MutableLiveData<ComponentesVisuais> =
        MutableLiveData<ComponentesVisuais>().also {
            it.value = temComponentes
        }

    var temComponentes: ComponentesVisuais = ComponentesVisuais()
    set(value) {
        field = value
        _componentes.value = value
    }
}

class ComponentesVisuais(
    val bottomNavigation: Boolean = false
)