package cuisine.de.lapin.choseafaire.network

import cuisine.de.lapin.choseafaire.model.MainDataModel
import cuisine.de.lapin.choseafaire.model.FlipDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface MainService {

    @Headers("Content-Type: application/json")
    @GET("mock.json")
    fun getMainData(): Call<MainDataModel>

    @Headers("Content-Type: application/json")
    @GET("mockflip.json")
    fun getFlipResult(): Call<FlipDataModel>
}

