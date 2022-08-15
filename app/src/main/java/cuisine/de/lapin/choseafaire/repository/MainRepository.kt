package cuisine.de.lapin.choseafaire.repository

import cuisine.de.lapin.choseafaire.model.FlipDataModel
import cuisine.de.lapin.choseafaire.model.MainDataModel
import cuisine.de.lapin.choseafaire.network.MainService
import cuisine.de.lapin.choseafaire.network.NetworkProperties
import cuisine.de.lapin.choseafaire.network.WeatherService
import java.io.IOException
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainService: MainService
) {
    fun getMainData(): MainDataModel? {
        try {
            val result = mainService.getMainData().execute()
            if (result.isSuccessful) {
                return result.body()
            } else {
                throw IOException("Wrong Result")
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    fun getFlipData(isHead: Boolean): FlipDataModel? {
        try {
            val result = mainService.getFlipResult(isHead).execute()
            if (result.isSuccessful) {
                return result.body()
            } else {
                throw IOException("Wrong Result")
            }
        } catch (exception: Exception) {
            throw exception
        }
    }
}