package br.com.visaogrupo.tudofarmarep.Carga.ultis

import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Enumeration

class CapturaIP {
    companion object{
        fun pegaIP(): String? {
            try {
                val interfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                while (interfaces.hasMoreElements()) {
                    val networkInterface: NetworkInterface = interfaces.nextElement()
                    val addresses: Enumeration<InetAddress> = networkInterface.inetAddresses
                    while (addresses.hasMoreElements()) {
                        val address: InetAddress = addresses.nextElement()
                        if (!address.isLoopbackAddress) {
                            val ipAddress: String = address.hostAddress
                            if (ipAddress.contains(":")) // É um endereço IPv6, ignorar
                                continue
                            return ipAddress
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }


}