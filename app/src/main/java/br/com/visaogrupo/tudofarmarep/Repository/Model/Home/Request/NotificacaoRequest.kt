package br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request

data class NotificacaoRequest (
   val Representante_id:Int,
   val Push_ID:Int = 0,
   val LerTodas:Int = 0
)