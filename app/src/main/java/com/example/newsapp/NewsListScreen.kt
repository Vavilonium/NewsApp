package com.example.newsapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class NewsArticle(
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val date: String,
    val imageUrl: String,
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(navController: NavController) {
    val sampleArticles = listOf(
        NewsArticle(
            "TechCrunch", "John Doe", "AI Revolution 2025",
            "AI is transforming industries faster than ever...",
            "05/05/2025",
            "https://media.geeksforgeeks.org/wp-content/uploads/geeksforgeeks-13.png",
            "https://techcrunch.com/sample-article-1"
        ),
        NewsArticle(
            "BBC News", "Jane Smith", "Global Warming Alert",
            "New reports show alarming trends in global climate...",
            "05/04/2025",
            "https://media.geeksforgeeks.org/wp-content/uploads/geeksforgeeks-13.png",
            "https://bbc.com/news/sample-article-2"
        ),
        NewsArticle(
            "Reuters", "Alex Green", "Markets Rally in Spring",
            "Markets show recovery signs after a tough winter...",
            "05/03/2025",
            "https://media.geeksforgeeks.org/wp-content/uploads/geeksforgeeks-13.png",
            "https://reuters.com/markets/sample-article-3"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("News") })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(sampleArticles) { article ->
                NewsItem(article = article, onClick = {
                    val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                    navController.navigate("newsDetailScreen/$encodedUrl")
                })
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
            Text(text = article.date, style = MaterialTheme.typography.bodySmall)

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