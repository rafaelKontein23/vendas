package br.com.visaogrupo.tudofarmarep.Objetos


data class Empresa(val cnpj: String, val razaoSocial: String, val endereco: String, val uf : String, val cidade:String = "", val numero:String ="", var isCarteira:Boolean = false)
