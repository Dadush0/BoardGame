package org.wit.boardgame.models
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlacemarkModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var Organizer: String = "",
    var numbP: String = "",
    var spotsLeft: Int = 0,
    var time: String = "",
    var image: Uri = Uri.EMPTY,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var userList: ArrayList<String> = ArrayList()) : Parcelable


@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

