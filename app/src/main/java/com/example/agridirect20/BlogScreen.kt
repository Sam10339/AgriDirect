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

data class ArticleUi(
    val id: String,
    val title: String,
    val summary: String
)

@Composable
fun BlogScreen(
    navController: NavController,
    onArticleClick: (String) -> Unit
) {
    val articles = listOf(
        ArticleUi(
            id = "a1",
            title = "The Benefits and Basics of Home Gardening",
            summary = "Home gardening provides a simple and rewarding way for people to grow fresh food, save money, support the environment, and improve their overall well-being."
        ),
        ArticleUi(
            id = "a2",
            title = "The Importance of Water Conservation",
            summary = "Water conservation is essential for protecting limited freshwater resources, reducing energy use, preserving ecosystems, and ensuring a sustainable future for generations to come."
        ),
        ArticleUi(
            id = "a3",
            title = "The Value of Natural Fertilizers ",
            summary = "Natural fertilizers offer a sustainable, safe, and cost-effective way to improve soil health, reduce environmental harm, and grow healthier crops without relying on synthetic chemicals."
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

