import android.os.Parcel
import android.os.Parcelable

data class ResumoMes(
    val ComissaoDisponivel: Double,
    val PedidosRealizados: Int,
    val QtdeCNPJ: Int,
    val TaxaFaturamento: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(ComissaoDisponivel)
        dest.writeInt(PedidosRealizados)
        dest.writeInt(QtdeCNPJ)
        dest.writeDouble(TaxaFaturamento)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResumoMes> {
        override fun createFromParcel(parcel: Parcel): ResumoMes {
            return ResumoMes(parcel)
        }

        override fun newArray(size: Int): Array<ResumoMes?> {
            return arrayOfNulls(size)
        }
    }
}
