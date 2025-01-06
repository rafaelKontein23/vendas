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
        private var _featureFlagAgenda :Boolean = false
        private var _featureFlagProgramaIncentivo :Boolean = false
        private var _featureFlagTreinamentos :Boolean = false
        private var _featureFlagVendaRemota :Boolean = false
        private var _featureFlagClubeCompras:Boolean = false

        // Getters públicos para as flags
        val featureFlagMeuTime: Boolean
            get() = _featureFlagMeuTime

        val featureFlagMerchan: Boolean
            get() = _featureFlagMerchan

        val featureFlagAgenda :Boolean  get() = _featureFlagAgenda
        val featureFlagProgramaIncentivo :Boolean  get() = _featureFlagProgramaIncentivo
        val featureFlagTreinamentos :Boolean  get() = _featureFlagTreinamentos
        val featureFlagVendaRemota :Boolean  get() = _featureFlagVendaRemota
        val featureFlagClubeCompras :Boolean  get() = _featureFlagClubeCompras


        fun atualizarFlags(flags: RespostaFlags) {
            _featureFlagMeuTime = flags.FeatureFlag_ID == 1 && flags.Status_Cod == 1
            _featureFlagMerchan = flags.FeatureFlag_ID == 2 && flags.Status_Cod == 1
            _featureFlagAgenda = flags.FeatureFlag_ID == 3 && flags.Status_Cod == 1
            _featureFlagProgramaIncentivo = flags.FeatureFlag_ID == 4 && flags.Status_Cod == 1
            _featureFlagTreinamentos = flags.FeatureFlag_ID == 5 && flags.Status_Cod == 1
            _featureFlagVendaRemota = flags.FeatureFlag_ID == 6 && flags.Status_Cod == 1
            _featureFlagClubeCompras = flags.FeatureFlag_ID == 7 && flags.Status_Cod == 1
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
            _featureFlagAgenda = false
            _featureFlagProgramaIncentivo = false
            _featureFlagTreinamentos = false
            _featureFlagVendaRemota = false
            _featureFlagClubeCompras = false
        }
    }
}
