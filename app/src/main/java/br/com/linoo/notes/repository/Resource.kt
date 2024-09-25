package br.com.linoo.notes.repository

class Resource<T>(
    val dado: T,
    val erro: String? = null
)

fun <T> criaResourceDeFalha(
    resourceAtual: Resource<T?>?,
    erro: String?
): Resource<T?> {
    if (resourceAtual != null) {
        return Resource(dado = resourceAtual.dado, erro = erro)
    }
    return Resource(dado = null, erro = erro)
}