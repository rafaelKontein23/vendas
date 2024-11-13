package br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi

import android.util.Base64
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.CriptografiaChavesSenha
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


fun String.incriptar(): String {
    val iv = IvParameterSpec(CriptografiaChavesSenha.SECRET_IV.toByteArray())
    val keySpec = SecretKeySpec(CriptografiaChavesSenha.SECRET_KEY.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
    val encrypted = cipher.doFinal(this.toByteArray())
    return Base64.encodeToString(encrypted, Base64.DEFAULT)
}

fun String.descritar(): String {
    val decoded = Base64.decode(this, Base64.DEFAULT)
    val iv = IvParameterSpec(CriptografiaChavesSenha.SECRET_IV.toByteArray())
    val keySpec = SecretKeySpec(CriptografiaChavesSenha.SECRET_KEY.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
    return String(cipher.doFinal(decoded))
}