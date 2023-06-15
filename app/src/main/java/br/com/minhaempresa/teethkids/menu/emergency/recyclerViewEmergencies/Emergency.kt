package br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.util.concurrent.TimeUnit

var emergencyList = mutableListOf<Emergency>()

enum class Status{
    //tem que ser exatamente igual ao typescript, inclusive as minúsculas e maiúsculas
    new,
    draft,
    done
}
@Parcelize
data class Time(
    val _seconds: Double,
    val _nanoseconds: Double
) : Parcelable {
    fun toMinutesFromNow(): Long {
        val then = this._seconds.toLong() * 1000 + this._nanoseconds.toLong()/1000000
        val now = System.currentTimeMillis()
        val resultmin = TimeUnit.MILLISECONDS.toMinutes(now - then)
        return resultmin
    }
}

@Parcelize
data class Emergency(
    val uid: String,
    val fcmToken: String,
    var phoneNumber: String,
    var name: String,
    var time: Time,
    val status: Status,
) : Parcelable