import android.graphics.Bitmap
import android.net.Uri
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.DadosBancariosRequests
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaFlags

class FormularioCadastro {
    companion object {
        var cadastro: CadastroRequest = CadastroRequest()
        var cadastroRequestAreaAtuacal: CadastroRequestAreaAtuacal = CadastroRequestAreaAtuacal()
        var dadosBancarios: DadosBancariosRequests = DadosBancariosRequests()
        var fotoDocumeto: Uri = Uri.EMPTY
        var base64Galeria = ""
        var base64Assinatura = ""
        var fotoPerfil: Uri = Uri.EMPTY
        var fotoPerfilUrl = ""
        var savedBitmap: Bitmap? = null
        var hash = ""

        private var _featureFlagMeuTime: Boolean = false
        private var _featureFlagMerchan: Boolean = false

        // Getters públicos para as flags
        val featureFlagMeuTime: Boolean
            get() = _featureFlagMeuTime

        val featureFlagMerchan: Boolean
            get() = _featureFlagMerchan

        fun atualizarFlags(flags: RespostaFlags) {
            _featureFlagMeuTime = flags.FeatureFlag_ID == 1 && flags.Status_Cod == 1
            _featureFlagMerchan = flags.FeatureFlag_ID == 2 && flags.Status_Cod == 1
        }

        fun limpaCadastro() {
            cadastro = CadastroRequest()
            dadosBancarios = DadosBancariosRequests()

            cadastroRequestAreaAtuacal = CadastroRequestAreaAtuacal()

            fotoDocumeto = Uri.EMPTY
            base64Galeria = ""
            base64Assinatura = ""
            savedBitmap = null
        }

        private fun resetarFlags() {
            _featureFlagMeuTime = false
            _featureFlagMerchan = false
        }
    }
}
