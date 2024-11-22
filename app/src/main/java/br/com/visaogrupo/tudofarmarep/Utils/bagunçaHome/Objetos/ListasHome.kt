package br.com.visaogrupo.tudofarmarep.Objetos

data class ListasHome (
    var banners: ArrayList<Banners> = ArrayList(),
    var graficosHome: ArrayList<GraficoMarca> =ArrayList(),
    var pedidosPendentes: ArrayList<CarrinhoAbertos> = ArrayList(),
    var resumoMes: ArrayList<ResumoMes> =ArrayList(),
    var menuLateral: ArrayList<Menulateral> = ArrayList(),
    var listaCotacao: ArrayList<Cotacao> = ArrayList()

)