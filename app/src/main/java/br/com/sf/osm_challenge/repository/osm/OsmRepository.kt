package br.com.sf.osm_challenge.repository.osm

import br.com.sf.osm_challenge.repository.BoundingBox
import br.com.sf.osm_challenge.repository.POI
import br.com.sf.osm_challenge.repository.PlaceDetail
import br.com.sf.osm_challenge.repository.Point
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class OsmRepository {

    private val service: Service

    init {
        service = createService()
    }

    private fun createService(): Service {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(Service::class.java);
    }

    fun findPOICloseTo(term: String, box: BoundingBox): Flowable<List<POI>> {
        return service.search(term, "json", 51, box.toViewport())
    }

    fun findReverse(point: Point): Flowable<List<POI>> {
        return service.reverse(
            "json",
            point.lat,
            point.lon,
            1
        )
    }

    fun findDetails(osmid: Int, osmType: String): Single<PlaceDetail> {
        return service
            .findDetails(
                osmid,
                osmType.substring(0, 1).toUpperCase(),
                "json",
                1,
                1,
                1
            )
    }

    private interface Service {

        @GET("/search")
        fun search(
            @Query("q") term: String,
            @Query("format") format: String,
            @Query("limit") limit: Int,
            @Query("viewbox") viewbox: String
        ): Flowable<List<POI>>

        @GET("/reverse")
        fun reverse(
            @Query("format") format: String,
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("addressdetails") addressDetails: Int
        ): Flowable<List<POI>>

        @GET("/details")
        fun findDetails(
            @Query("osmid") osmid: Int,
            @Query("osmtype") osmType: String,
            @Query("format") format: String,
            @Query("addressdetails") addressDetails: Int,
            @Query("linkedplaces") linkedPlaces: Int,
            @Query("hierarchy") hierarchy: Int
        ): Single<PlaceDetail>
    }
}