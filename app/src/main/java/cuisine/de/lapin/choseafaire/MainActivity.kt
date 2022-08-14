package cuisine.de.lapin.choseafaire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cuisine.de.lapin.choseafaire.datastore.LogInDataStore
import cuisine.de.lapin.choseafaire.ui.theme.ChoseAFairePourAndroidTheme
import cuisine.de.lapin.choseafaire.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getMainData()
        viewModel.getWeatherData()

        val logInDataStore = LogInDataStore(this)

        setContent {
            ChoseAFairePourAndroidTheme {
                val loginName = logInDataStore.getLoginName.collectAsState(initial = null)

//                if (loginName.value.isNullOrEmpty()) {
//                    Entrance()
//                } else {
//                    ShowMain(viewModel = viewModel)
//                }
                ShowMain(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ShowMain(viewModel: MainViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            Time()
            WeatherInfo(viewModel = viewModel)
            Quote(viewModel = viewModel)
            ToDoList()
            Buinance()
        }
    }
}

@Composable
fun Entrance() {

}

@Composable
fun Time() {

}

@Composable
fun WeatherInfo(viewModel: MainViewModel) {
    val weather by viewModel.weatherData.observeAsState()
    Text("${weather?.name}, ${weather?.main?.temp} °C, ${weather?.weather?.get(0)?.description}")
}

@Composable
fun Quote(viewModel: MainViewModel) {
    val mainData by viewModel.mainData.observeAsState()
    Column {
        Text("${mainData?.quote?.quote}")
        Text("- ${mainData?.quote?.author}")
    }

}

@Composable
fun ToDoList() {

}

@Composable
fun Buinance() {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChoseAFairePourAndroidTheme {

    }
}