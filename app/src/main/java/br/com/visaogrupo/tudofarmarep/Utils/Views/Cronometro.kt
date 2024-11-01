package br.com.visaogrupo.tudofarmarep.Utils.Views
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Cronometro(private val tempoInicialSegundos: Int = 0) {

    private val _tempo = MutableStateFlow(formatarTempo(tempoInicialSegundos))
    val tempo: StateFlow<String> = _tempo.asStateFlow()

    private var segundosAtuais = tempoInicialSegundos
    private var job: Job? = null

    fun iniciar() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive && segundosAtuais >= 0) {
                _tempo.value = formatarTempo(segundosAtuais)
                delay(1000L)
                segundosAtuais--
            }
            // Cancela a coroutine quando o tempo chega a zero
            if (segundosAtuais < 0) {
                _tempo.value = formatarTempo(0)
            }
        }
    }

    fun pausar() {
        job?.cancel()
    }

    fun resetar() {
        pausar()
        segundosAtuais = tempoInicialSegundos
        _tempo.value = formatarTempo(segundosAtuais)
    }

    private fun formatarTempo(segundos: Int): String {
        val minutos = segundos / 60
        val seg = segundos % 60
        return String.format("%02d:%02d", minutos, seg)
    }
}