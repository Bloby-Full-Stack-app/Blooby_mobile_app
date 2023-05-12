package tn.esprit.looby.service

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tn.esprit.looby.models.Record

interface RecordService {

    data class RecordResponse(
        @SerializedName("record")
        val record: Record
    )

    data class RecordsResponse(
        @SerializedName("records")
        val records: List<Record>
    )

    data class NameBody(
        val locationName: String
    )

    data class RecordBody(
        val idUser: String,
        val longitude: Double,
        val lattitude: Double,
        val locationName: String
    )

    @POST("/record/get-by-location")
    fun getByLocation(@Body nameBody:NameBody): Call<RecordsResponse>

    @POST("/record/add-or-update")
    fun addOrUpdate(@Body recordBody: RecordBody): Call<RecordResponse>
}