package cuisine.de.lapin.choseafaire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import cuisine.de.lapin.choseafaire.model.FlipDataModel
import cuisine.de.lapin.choseafaire.network.NetworkProperties
import cuisine.de.lapin.choseafaire.ui.theme.ChoseAFairePourAndroidTheme
import cuisine.de.lapin.choseafaire.ui.theme.Purple500
import cuisine.de.lapin.choseafaire.ui.theme.Purple700
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
    viewModel.getMainData()
    viewModel.getWeatherData()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        backgroundView(viewModel = viewModel)
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.horizontal_padding),
                )
                .padding(top = dimensionResource(id = R.dimen.top_padding)),
        ) {
            TopUI(userName)
            WeatherInfo(viewModel = viewModel)
            Quote(viewModel = viewModel)
            ToDoList(viewModel)
            Buinance(viewModel)
        }
    }
}

@Composable
fun backgroundView(viewModel: MainViewModel) {
    val mainData by viewModel.mainData.observeAsState()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("${NetworkProperties.BASE_URL}${mainData?.background ?: ""}")
            .size(Size.ORIGINAL) // Set the target size to load the image at.
            .build()
    )

    Image(
        painter = painter,
        contentDescription = stringResource(id = R.string.background_description),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )
}

@Composable
fun Entrance(viewModel: MainViewModel) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_padding))
        ) {
            TextField(
                value = text,
                colors = TextFieldDefaults.textFieldColors(textColor = Purple700),
                onValueChange = { value -> text = value },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_login),
                        color = Purple500
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.saveLoginName(text)
                        focusManager.clearFocus()
                    }),
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.login_height))
                    .weight(2f)
            )

            Button(
                onClick = {
                    viewModel.saveLoginName(text)
                },
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.login_height))
                    .weight(1f)
            ) {
                Text(stringResource(id = R.string.button_login))
            }
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
fun RowScope.Bienvenue(userName: String) {
    Text(
        text = stringResource(
            id = R.string.welcome, userName
        ),
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun Time() {
    var date by remember { mutableStateOf(Date()) }
    LaunchedEffect(0) {
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
    Row(
        modifier = Modifier
            .weight(1.5f)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = weather?.name ?: "")
            Text(text = weather?.weather?.get(0)?.description ?: "")
        }

        Text(
            text = stringResource(id = R.string.temperature, weather?.main?.temp ?: 0f),
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun ColumnScope.Quote(viewModel: MainViewModel) {
    val mainData by viewModel.mainData.observeAsState()
    Column(
        modifier = Modifier
            .weight(2f)
    ) {
        Text(text = mainData?.quote?.quote ?: "")
        Text(
            text = stringResource(id = R.string.author_quote, mainData?.quote?.author ?: ""),
            modifier = Modifier.align(Alignment.End)
        )
    }

}

@Composable
fun ColumnScope.ToDoList(viewModel: MainViewModel) {
    var content by rememberSaveable {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.weight(8f)) {
        Row {
            TextField(
                value = content,
                onValueChange = { value -> content = value },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.label_todo),
                        fontSize = 10.sp
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.insert(content)
                    focusManager.clearFocus()
                    content = ""

                }),
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(id = R.dimen.todo_height))
            )
            Button(
                onClick = {
                    viewModel.insert(content)
                },
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.button_padding))
                    .height(dimensionResource(id = R.dimen.todo_height))
            ) {
                Text(text = stringResource(id = R.string.button_todo))
            }
        }

        ShowToDos(viewModel)
    }

}

@Composable
fun ShowToDos(viewModel: MainViewModel) {
    val todoData by viewModel.todos.observeAsState()
    val listState = rememberLazyListState()

    todoData?.let {
        LazyColumn(state = listState) {
            itemsIndexed(it) { _, item ->
                Row {
                    Text(
                        text = item.content,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    Button(
                        onClick = {
                            viewModel.delete(item)
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_todo_delete),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

            }
        }
    }

    LaunchedEffect(todoData) {
        listState.scrollToItem(todoData?.size ?: 0)
    }
}


@Composable
fun ColumnScope.Buinance(viewModel: MainViewModel) {
    Column(modifier = Modifier.weight(4f)) {
        Row {
            Button(
                onClick = { viewModel.doFlip(true) },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.buinance_head))
            }
            Box(modifier = Modifier.width(dimensionResource(id = R.dimen.button_padding)))
            Button(
                onClick = { viewModel.doFlip(false) },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.buinance_tail))
            }
        }

        BuinanceLog(viewModel)
    }
}

@Composable
fun BuinanceLog(viewModel: MainViewModel) {
    val listState = rememberLazyListState()
    val flipData by viewModel.flipData.observeAsState()

    flipData?.let {
        LazyColumn(state = listState) {
            itemsIndexed(it) { _, item ->
                BuinanceResult(item = item)
            }
        }
    }

    LaunchedEffect(flipData) {
        listState.scrollToItem(flipData?.size ?: 0)
    }
}

@Composable
fun LazyItemScope.BuinanceResult(item: FlipDataModel) {
    Text(
        text = stringResource(
            id = R.string.buinance_result,
            getIsHead(isHead = item.isYourHead),
            getIsHead(isHead = item.isAIHead),
            stringResource(
                id = if (item.isSuccess)
                    R.string.buinance_success
                else
                    R.string.buinance_fail
            )
        )
    )
}

@Composable
fun getIsHead(isHead: Boolean): String {
    return stringResource(
        id = if (isHead)
            R.string.buinance_head
        else
            R.string.buinance_tail
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChoseAFairePourAndroidTheme {

    }
}