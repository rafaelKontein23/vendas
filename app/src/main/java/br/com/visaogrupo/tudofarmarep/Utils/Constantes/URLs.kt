package br.com.visaogrupo.tudofarmarep.Utils.Constantes

class URLs {
    companion object {
        private var isInitialized = false

        var urlWsBase: String = "https://www.visaogrupo.com.br/ws/"
        var urlWs: String = "https://www.visaogrupo.com.br/"
        var urlImagensLoja: String = "https://stage.loiu.com.br/Cargas/images/lojas/icones"
        var urlImagensCnpjs: String = "https://stage.loiu.com.br/Cargas/images/icones/"
        var urlImagensOpls: String = "https://stage.loiu.com.br/img/logos/distribuidoras/"
        var urlMarcas: String = "https://stage.loiu.com.br/"
        var urlVendaRometa: String = "https://vendaremota.loiu.com.br/meu-hash/"
        var Url_pdf: String = "https://www.loiu.com.br/Docs/Extract/Usuarios/"
        var URL_Webviewmob: String = "https://stage.loiu.com.br/Autenticacao/LoginMobile?login="
        var URL_convite: String = "https://www.loiu.com.br/"

        // Função para trocar o ambiente apenas se não tiver sido configurado
        fun trocaAmbiente(ambiente: String) {
                if (ambiente == "stage") {
                    urlWsBase = "https://www.visaogrupo.com.br/ws/"
                    urlWs = "https://www.visaogrupo.com.br/"
                    urlVendaRometa = "https://vendaremota.loiu.com.br/meu-hash/"

                } else {
                    urlWsBase = "https://${ambiente}.visaogrupo.com.br/ws/"
                    urlWs = "https://${ambiente}.visaogrupo.com.br/"
                    urlVendaRometa = "https://${ambiente}.vendaremota.loiu.com.br/meu-hash/"

                }

                urlImagensLoja = "https://${ambiente}.loiu.com.br/Cargas/images/lojas/icones"
                urlImagensCnpjs = "https://${ambiente}.loiu.com.br/Cargas/images/icones/"
                urlImagensOpls = "https://${ambiente}.loiu.com.br/img/logos/distribuidoras/"
                urlMarcas = "https://${ambiente}.loiu.com.br/"
                Url_pdf = "https://${ambiente}.loiu.com.br/Docs/Extract/Usuarios/"
                URL_Webviewmob = "https://${ambiente}.loiu.com.br/Autenticacao/LoginMobile?login="
                URL_convite = "https://${ambiente}.loiu.com.br/"


        }

        fun isConfigured(): Boolean {
            return isInitialized
        }
    }
}