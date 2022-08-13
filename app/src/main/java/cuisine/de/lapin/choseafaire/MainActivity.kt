package cuisine.de.lapin.choseafaire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import cuisine.de.lapin.choseafaire.ui.theme.ChoseAFairePourAndroidTheme
import cuisine.de.lapin.choseafaire.viewmodel.MainViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        viewModel.mainData.observe(this) {
            Timber.i("dataResult main ${Gson().toJson(it)}")
        }

        viewModel.weatherData.observe(this) {
            Timber.i("dataResult weather ${Gson().toJson(it)}")
        }

        viewModel.init()

        setContent {
            ChoseAFairePourAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Text(text = "Hello Android!", modifier = Modifier.clickable {

                    })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", modifier = Modifier.clickable {

    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChoseAFairePourAndroidTheme {
        Greeting("Android")
    }
}