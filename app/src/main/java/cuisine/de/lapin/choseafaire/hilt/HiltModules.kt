package cuisine.de.lapin.choseafaire.hilt

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import cuisine.de.lapin.choseafaire.database.AppDataBase
import cuisine.de.lapin.choseafaire.datastore.LogInDataStore
import cuisine.de.lapin.choseafaire.network.MainService
import cuisine.de.lapin.choseafaire.network.NetworkProperties
import cuisine.de.lapin.choseafaire.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object HiltServiceModule {
    @Provides
    fun provideMainService(): MainService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NetworkProperties.BASE_URL)
            .build()
            .create(MainService::class.java)
    }

    @Provides
    fun provideWeatherService(): WeatherService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NetworkProperties.WEATHER_BASE_URL)
            .build()
            .create(WeatherService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDataBase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDataBase::class.java,
            "app_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideLoginDataStore(@ApplicationContext appContext: Context):LogInDataStore {
        return LogInDataStore(appContext)
    }
}