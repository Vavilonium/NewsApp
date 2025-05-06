package com.example.newsapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class NewsArticle(
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val date: ZonedDateTime,
    val imageUrl: String,
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(navController: NavController) {
    // Состояние для списка новостей
    var newsArticles by remember { mutableStateOf<List<NewsArticle>>(emptyList()) }

    // Инициализация сервиса для получения новостей
    val newsService = NewsService()

    // Загрузка новостей, когда экран компонуется
    LaunchedEffect(true) {
        newsService.GetNews { articles ->
            newsArticles = articles // Обновляем состояние новостей
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("News") })
        }
    ) { innerPadding ->
        // Проверяем, есть ли новости для отображения
        if (newsArticles.isEmpty()) {
            // Если нет новостей, показываем загрузку
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(newsArticles) { article ->
                    NewsItem(article = article, onClick = {
                        val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                        navController.navigate("newsDetailScreen/$encodedUrl")
                    })
                }
            }
        }
    }
}

@Composable
fun NewsItem(article: NewsArticle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = article.source, style = MaterialTheme.typography.labelSmall)
            Text(text = article.author, style = MaterialTheme.typography.labelSmall)
            Text(text = article.title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = article.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = FormatDate(article.date), style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberAsyncImagePainter(article.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun FormatDate(dateTime: ZonedDateTime): String {
    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val month = dateTime.monthValue.toString().padStart(2, '0')
    val year = dateTime.year
    return "$month/$day/$year"
}