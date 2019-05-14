package br.com.sf.osm_challenge.repository

import com.google.gson.annotations.SerializedName

data class POI(
    @SerializedName("place_id") val id: String,
    @SerializedName("osm_id") val osmId: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("osm_type") val osmType: String,
    @SerializedName("icon") val icon: String
)

data class Point(val lat: Double, val lon: Double)
data class BoundingBox(val p1: Point, val p2: Point) {

    fun toViewport(): String {
        return "${p1.lat},${p1.lon},${p2.lat},${p2.lon}"
    }
}

data class PlaceDetail(
    @SerializedName("place_id") val id: String,
    @SerializedName("osm_id") val osm_id: String,
    @SerializedName("category") val category: String,
    @SerializedName("type") val type: String,
    @SerializedName("localname") val localName: String,
    @SerializedName("calculated_postcode") val calculated_postcode: String,
    @SerializedName("country_code") val country_code: String,
    @SerializedName("centroid") val center: PlaceCenter,
    @SerializedName("address") val address: List<Address>

)

data class Address(
    @SerializedName("localname") val localName: String
)

data class PlaceCenter(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<Double>
)