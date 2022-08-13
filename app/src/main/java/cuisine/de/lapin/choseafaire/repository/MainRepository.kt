package cuisine.de.lapin.choseafaire.repository

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
}