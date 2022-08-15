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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import cuisine.de.lapin.choseafaire.database.AppDataBase
import cuisine.de.lapin.choseafaire.datastore.LogInDataStore
import cuisine.de.lapin.choseafaire.repository.ToDoRepository
import cuisine.de.lapin.choseafaire.ui.theme.ChoseAFairePourAndroidTheme
import cuisine.de.lapin.choseafaire.viewmodel.MainViewModel
import cuisine.de.lapin.choseafaire.viewmodel.ToDoListViewModel
import cuisine.de.lapin.choseafaire.viewmodel.ToDoListViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()
        val todoViewModel: ToDoListViewModel by viewModels {
            ToDoListViewModelFactory(ToDoRepository(AppDataBase.getDatabase(this).todoListDao()))
        }

        viewModel.getMainData()
        viewModel.getWeatherData()

        val logInDataStore = LogInDataStore(this)

        setContent {
            ChoseAFairePourAndroidTheme {
                val loginName = logInDataStore.getLoginName.collectAsState(initial = null)

                if (loginName.value.isNullOrEmpty()) {
                    Entrance(logInDataStore, lifecycleScope)
                } else {
                    ShowMain(viewModel = viewModel, todoViewModel = todoViewModel, loginName.value ?: "")
                }
            }
        }
    }
}

@Composable
fun ShowMain(viewModel: MainViewModel, todoViewModel: ToDoListViewModel, userName: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            TopUI(userName)
            WeatherInfo(viewModel = viewModel)
            Quote(viewModel = viewModel)
            ToDoList(todoViewModel)
            Buinance(viewModel)
        }
    }
}

@Composable
fun Entrance(logInDataStore: LogInDataStore, coroutineScope: CoroutineScope) {
    var text by remember {mutableStateOf("")}
    Row {
        TextField(
            value = text,
            onValueChange = { value -> text = value},
            label = {Text("Comment vous applez-vous ?")}
        )

        Button(onClick = {
            coroutineScope.launch {
                logInDataStore.saveLoginName(text)
            }
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
fun ColumnScope.ToDoList(viewModel: ToDoListViewModel) {
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
fun ShowToDos(viewModel: ToDoListViewModel) {
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