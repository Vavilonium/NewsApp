package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.ui.theme.NewsAppTheme
import com.kwabenaberko.newsapilib.NewsApiClient
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "newsList") {
                    composable("newsList") {
                        NewsListScreen(navController)
                    }

                    composable(
                        "newsDetailScreen/{url}",
                        arguments = listOf(navArgument("url") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val encodedUrl = backStackEntry.arguments?.getString("url")
                        val url = encodedUrl?.let {
                            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                        }
                        NewsDetailScreen(url = url, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

