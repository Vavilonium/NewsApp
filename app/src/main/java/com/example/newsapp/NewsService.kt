package com.example.newsapp

import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.NewsApiClient.ArticlesResponseCallback
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import java.time.Instant
import java.time.ZoneId


class NewsService {
    private var client : NewsApiClient = NewsApiClient(BuildConfig.NEWS_API_KEY)

    fun GetNews() {
        client.getTopHeadlines(
            TopHeadlinesRequest.Builder()
                .q("bitcoin")
                .language("en")
                .build(),
            object : ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse) {
                    HandleNewsResult(response)
                    return
                }

                override fun onFailure(throwable: Throwable) {
                    return
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
                source = piece.source.name,
                author = piece.author,
                title = piece.title,
                description = piece.description,
                date = Instant.parse(piece.publishedAt).atZone(ZoneId.of("UTC")),
                imageUrl = piece.urlToImage,
                url = piece.url
            )
            listOfArticles.add(article)
        }
        return listOfArticles
    }

}