package cuisine.de.lapin.choseafaire.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LogInDataStore(private val context: Context) {
    private val LOGIN_NAME_KEY = stringPreferencesKey("Login_Name")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "loginInfo")

    val getLoginName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LOGIN_NAME_KEY]
        }

    suspend fun saveLoginName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_NAME_KEY] = name
        }
    }
}