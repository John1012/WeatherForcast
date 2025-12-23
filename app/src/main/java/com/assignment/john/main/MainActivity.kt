package com.assignment.john.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.assignment.john.R
import com.assignment.john.ui.theme.WeatherForecastTheme
import com.assignment.john.usecase.WeatherForecastUI
import com.assignment.john.usecase.WeatherUI
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecastTheme(darkTheme = true) {
                // Scaffold 會自動使用暗色主題的背景顏色
                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MainScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    when (val state = viewState) {
        is MainState.Loading, MainState.Initial -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        }

        is MainState.Complete -> {
            MainContent(
                state.content,
                onCitySelected = { city -> viewModel.getWeatherByCity(city) }
            )
        }

        is MainState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(state.throwable)
            }
        }
    }
}

@Composable
fun MainContent(
    content: Content,
    onCitySelected: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CityDropdownMenu(
            content.cityList,
            onCitySelected = onCitySelected,
        )
        Spacer(modifier = Modifier.height(16.dp))
        content.weatherForecast?.currentWeather?.let {
            CurrentWeather(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        content.weatherForecast?.forecastList?.let {
            ForecastList(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdownMenu(
    cityList: List<String>,
    onCitySelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember(cityList) { mutableStateOf(cityList.firstOrNull() ?: "") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("請選擇城市") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cityList.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onCitySelected(selectedOptionText)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
fun CurrentWeather(current: WeatherUI) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Current",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.White
        )
        WeatherItem(
            timestamp = current.timestamp,
            weatherType = current.weatherType,
            icon = current.icon,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun ForecastList(
    forecastList: List<WeatherUI>,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Forecast",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.White
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = forecastList,
                key = { it.timestamp }
            ) { item ->
                WeatherItem(
                    timestamp = item.timestamp,
                    weatherType = item.weatherType,
                    icon = item.icon,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun WeatherItem(
    timestamp: Long,
    weatherType: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    val randomColor by remember {
        mutableStateOf(
            Color(
                red = Random.nextFloat(),
                green = Random.nextFloat(),
                blue = Random.nextFloat(),
                alpha = 1f
            )
        )
    }

    val gradientBrush = Brush.linearGradient(
        colors = listOf(randomColor.copy(alpha = 0.4f), randomColor),
        start = androidx.compose.ui.geometry.Offset.Zero,
        end = androidx.compose.ui.geometry.Offset.Infinite
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 2.dp,
                color = randomColor,
                shape = MaterialTheme.shapes.medium
            )
            .background(gradientBrush)
            .padding(16.dp)
    ) {
        AsyncImage(
            model = "https://openweathermap.org/img/wn/$icon@2x.png",
            contentDescription = "天氣圖標",
            placeholder = painterResource(R.drawable.placeholder),
            modifier = Modifier.size(64.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = weatherType,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = timestamp.formatTimestamp(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ErrorScreen(throwable: Throwable?) {
    Text(
        text = "Error: ${throwable?.message ?: "Something Wrong!!!"}",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    // 在預覽中也使用暗色主題
    WeatherForecastTheme(darkTheme = true) {
        val content = Content(
            cityList = listOf("London", "Paris", "New York"),
            weatherForecast = WeatherForecastUI(
                city = "London",
                currentWeather = WeatherUI(
                    timestamp = 1766731902,
                    weatherType = "Clouds",
                    icon = "10d"
                ),
                forecastList = listOf(
                    WeatherUI(1766741902, "Rain", "09d"),
                    WeatherUI(1766751902, "Clear", "01d")
                ),
            )
        )
        // 為了讓預覽背景是暗色，我們將 MainContent 包在 Scaffold 中
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                MainContent(
                    content = content,
                    onCitySelected = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    // 在預覽中也使用暗色主題
    WeatherForecastTheme(darkTheme = true) {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                ErrorScreen(null)
            }
        }
    }
}

fun Long.formatTimestamp(): String {
    val instant = Instant.ofEpochSecond(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}
