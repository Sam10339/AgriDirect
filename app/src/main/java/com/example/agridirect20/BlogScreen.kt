package com.example.agridirect20

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Simple UI model representing a blog article.
 *
 * @param id Used for navigation to the full article.
 * @param title Displayed in the list.
 * @param summary Short preview text for the article.
 */
data class ArticleUi(
    val id: String,
    val title: String,
    val summary: String
)

/**
 * Screen that displays a list of articles for the app's "Blog & Learning" section.
 *
 * Users can tap an article to open the full article page.
 *
 * USAGE EXAMPLE (AppNav):
 * composable("blog") {
 *     BlogScreen(
 *         navController = navController,
 *         onArticleClick = { id -> navController.navigate("article/$id") }
 *     )
 * }
 */
@Composable
fun BlogScreen(
    navController: NavController,
    onArticleClick: (String) -> Unit
) {
    // Static list of articles used by the Blog section.
    val articles = listOf(
        ArticleUi(
            id = "a1",
            title = "The Benefits and Basics of Home Gardening",
            summary = "Home gardening provides a simple and rewarding way for people to grow fresh food, save money, and support the environment."
        ),
        ArticleUi(
            id = "a2",
            title = "The Importance of Water Conservation",
            summary = "Water conservation protects our environment, saves energy, and supports sustainable living."
        ),
        ArticleUi(
            id = "a3",
            title = "The Value of Natural Fertilizers",
            summary = "Natural fertilizers improve soil health and reduce chemical runoff while growing healthier crops."
        )
    )

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Blog & Learning",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Scrollable list of article cards
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { onArticleClick(article.id) }
                    )
                }
            }
        }
    }
}

/**
 * A card representing a single article in the blog list.
 *
 * Displays the article title and summary, and navigates when clicked.
 *
 * Example:
 * ArticleCard(article) { navController.navigate("article/${article.id}") }
 */
@Composable
fun ArticleCard(
    article: ArticleUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
