package com.parth.crawlappsdemo.ui.filter.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterModel(
    @SerializedName("id") var id: FilterId = FilterId.STATUS,
    @SerializedName("type") var type: FilterType = FilterType.CHECK_BOX,
    @SerializedName("name") var name: String = "",
    @SerializedName("filterValue") var filterValue: ArrayList<FilterValue> = ArrayList(),
    @SerializedName("isChecked") var isChecked: Boolean = false,
    @SerializedName("sTime") var sTime: Int = 0,
    @SerializedName("eTime") var eTime: Int = 0
) : Parcelable

@Parcelize
data class FilterValue(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("isChecked") var isChecked: Boolean = false,
    @SerializedName("extraText") var extraText: String = ""
) : Parcelable

enum class FilterId {
    STATUS,
    USERS,
}

enum class FilterType {
    CHECK_BOX,
    RADIO
}