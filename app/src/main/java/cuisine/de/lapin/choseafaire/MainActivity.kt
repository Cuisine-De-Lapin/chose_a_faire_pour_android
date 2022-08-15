package cuisine.de.lapin.choseafaire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import cuisine.de.lapin.choseafaire.ui.theme.ChoseAFairePourAndroidTheme
import cuisine.de.lapin.choseafaire.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()

        viewModel.getMainData()
        viewModel.getWeatherData()

        setContent {
            ChoseAFairePourAndroidTheme {
                val loginName = viewModel.getLoginName().collectAsState(initial = null)

                if (loginName.value.isNullOrEmpty()) {
                    Entrance(viewModel = viewModel)
                } else {
                    ShowMain(viewModel = viewModel, loginName.value ?: "")
                }
            }
        }
    }
}

@Composable
fun ShowMain(viewModel: MainViewModel, userName: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            TopUI(userName)
            WeatherInfo(viewModel = viewModel)
            Quote(viewModel = viewModel)
            ToDoList(viewModel)
            Buinance(viewModel)
        }
    }
}

@Composable
fun Entrance(viewModel: MainViewModel) {
    var text by remember {mutableStateOf("")}
    Row {
        TextField(
            value = text,
            onValueChange = { value -> text = value},
            label = {Text("Comment vous applez-vous ?")}
        )

        Button(onClick = {
            viewModel.saveLoginName(text)
        }) {
            Text("Log In")
        }
    }
}

@Composable
fun ColumnScope.TopUI(userName: String) {
    Row(modifier = Modifier.weight(1f)) {
       Bienvenue(userName = userName)
       Time()
    }
}

@Composable
fun Bienvenue(userName: String) {
    Text(text = "Bonjour, $userName.")
}

@Composable
fun Time() {
    var date by remember { mutableStateOf(Date()) } // 2
    LaunchedEffect(0) { // 3
        while (true) {
            date = Date()
            delay(1000)
        }
    }
    
    ShowClock(date = date)
}

@Composable
fun ShowClock(date: Date) {
    val timeText = SimpleDateFormat("HH:mm:ss", Locale.US).format(date)
    Text(text = timeText)
}

@Composable
fun ColumnScope.WeatherInfo(viewModel: MainViewModel) {
    val weather by viewModel.weatherData.observeAsState()
    Text(
        text = "${weather?.main?.temp} °C, ${weather?.weather?.get(0)?.description}",
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun ColumnScope.Quote(viewModel: MainViewModel) {
    val mainData by viewModel.mainData.observeAsState()
    Column(modifier = Modifier.weight(2f)) {
        Text("${mainData?.quote?.quote}")
        Text("- ${mainData?.quote?.author}")
    }

}

@Composable
fun ColumnScope.ToDoList(viewModel: MainViewModel) {
    var content by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.weight(6f)) {
        TextField(value = content, onValueChange = { value -> content = value}, label = {Text("Écrivez une chose à faire et appuyez sur Entrée.")})
        Button(onClick = {
            viewModel.insert(content)
        }) {
            Text(text = "Continuer")
        }
        ShowToDos(viewModel)
    }

}

@Composable
fun ShowToDos(viewModel: MainViewModel) {
    val flipData = viewModel.todos.observeAsState()

    flipData.value?.let {
        LazyColumn {
            itemsIndexed(it) { index, item ->
                Row {
                    Text(text = "${Gson().toJson(item)}")
                    Button(onClick = {
                        viewModel.delete(item)
                    }) {
                        Text(text = "X")
                    }
                }

            }
        }
    }
}


@Composable
fun ColumnScope.Buinance(viewModel: MainViewModel) {
    Column(modifier = Modifier.weight(6f)) {
        Row {
            Button(onClick = { viewModel.doFlip(true) }) {
                Text("Tête")
            }
            Button(onClick = { viewModel.doFlip(false) }) {
                Text("Queue")
            }
        }

        BuinanceLog(viewModel)
    }
}

@Composable
fun BuinanceLog(viewModel: MainViewModel) {
    val flipData = viewModel.flipData.observeAsState()

    flipData.value?.let {
        LazyColumn {
            itemsIndexed(it) { index, item ->
                Text(text = "${Gson().toJson(item)}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChoseAFairePourAndroidTheme {

    }
}