package cuisine.de.lapin.choseafaire.repository

import cuisine.de.lapin.choseafaire.model.FlipDataModel
import cuisine.de.lapin.choseafaire.model.MainDataModel
import cuisine.de.lapin.choseafaire.network.NetworkProperties
import java.io.IOException

class MainRepository {
    fun getMainData(): MainDataModel? {
        try {
            val result = NetworkProperties.getService()?.getMainData()?.execute()
            if (result?.isSuccessful == true) {
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
            val result = NetworkProperties.getService()?.getFlipResult(isHead)?.execute()
            if (result?.isSuccessful == true) {
                return result.body()
            } else {
                throw IOException("Wrong Result")
            }
        } catch (exception: Exception) {
            throw exception
        }
    }
}