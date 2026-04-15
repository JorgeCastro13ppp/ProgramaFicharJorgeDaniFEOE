import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programaficharfeoe.data.model.Documento
import com.example.programaficharfeoe.data.repository.DocumentoRepository
import kotlinx.coroutines.launch

class DocumentosViewModel : ViewModel() {

    private val repository = DocumentoRepository()

    var documentos = mutableStateOf<List<Documento>>(emptyList())
    var isLoading = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    fun cargarDocumentos(tipo: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val result = repository.getDocumentos(tipo)
                documentos.value = result ?: emptyList()
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}