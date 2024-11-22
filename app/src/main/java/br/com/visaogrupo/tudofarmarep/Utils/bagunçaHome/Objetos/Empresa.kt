package br.com.visaogrupo.tudofarmarep.Objetos

import br.com.visaogrupo.tudofarmarep.Models.CNPJ

data class Empresa(val cnpj: String, val razaoSocial: String, val endereco: String, val uf : String, val cidade:String = "", val numero:String ="", var isCarteira:Boolean = false)
