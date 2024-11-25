package br.com.visaogrupo.tudofarmarep.Objetos

import android.os.Parcel
import android.os.Parcelable

data class GraficoMarca(
    val corMarca: String, // Ajustado para o padrão camelCase
    val marca: String,
    var pedidosRealizados: Int,
    var pedidoRealizadosOriginal: Int = 0
) : Parcelable {
    private constructor(parcel: Parcel) : this(
        parcel.readString() ?: "", // Garantia contra valores nulos
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(corMarca)
        parcel.writeString(marca)
        parcel.writeInt(pedidosRealizados)
        parcel.writeInt(pedidoRealizadosOriginal)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<GraficoMarca> {
        override fun createFromParcel(parcel: Parcel): GraficoMarca = GraficoMarca(parcel)

        override fun newArray(size: Int): Array<GraficoMarca?> = arrayOfNulls(size)
    }
}
