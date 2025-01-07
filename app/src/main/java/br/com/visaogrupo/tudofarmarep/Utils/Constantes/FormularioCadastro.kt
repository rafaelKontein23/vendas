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
            when(flags.FeatureFlag_ID){
                 1 -> _featureFlagMeuTime = flags.Status_Cod == 1
                2 -> _featureFlagMerchan = flags.Status_Cod == 1
                3 -> _featureFlagAgenda = flags.Status_Cod == 1
                4 -> _featureFlagProgramaIncentivo = flags.Status_Cod == 1
                5 -> _featureFlagTreinamentos = flags.Status_Cod == 1
                6 -> _featureFlagVendaRemota = flags.Status_Cod == 1
                7 -> _featureFlagClubeCompras = flags.Status_Cod == 1
            }
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


    }
}
