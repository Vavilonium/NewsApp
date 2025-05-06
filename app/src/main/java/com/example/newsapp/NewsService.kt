package com.example.newsapp

import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.NewsApiClient.ArticlesResponseCallback
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import java.time.Instant
import java.time.ZoneId


class NewsService {
    private var client : NewsApiClient = NewsApiClient(BuildConfig.NEWS_API_KEY)

    fun GetNews(onResult: (List<NewsArticle>) -> Unit) {
        client.getTopHeadlines(
            TopHeadlinesRequest.Builder()
                .country("us")
                .build(),
            object : ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse) {
                    val articles = HandleNewsResult(response)
                    onResult(articles)
                }

                override fun onFailure(throwable: Throwable) {
                    println("Error: ${throwable.message}")
                }
            }
        )
    }

    private fun HandleNewsResult(response: ArticleResponse): List<NewsArticle>
    {
        val listOfArticles = mutableListOf<NewsArticle>()

        for (piece in response.articles)
        {
            var article = NewsArticle(
                source = piece.source?.name ?: "Unknown Source",
                author = piece.author  ?: "Unknown Author",
                title = piece.title ?: "No Title",
                description = piece.description ?: "No Description",
                date = Instant.parse(piece.publishedAt).atZone(ZoneId.of("UTC")),
                imageUrl = piece.urlToImage ?: "",
                url = piece.url ?: ""
            )
            listOfArticles.add(article)
        }
        return listOfArticles
    }

}