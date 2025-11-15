package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArticleScreen(
    articleId: String
) {
    // For now, just map IDs to some simple content.
    val article = when (articleId) {
        "a1" -> ArticleContent(
            title = "Intro to Local Farming",
            body = """
                Local farming focuses on producing food close to where it is consumed.
                Buying from local farms can reduce transportation emissions, support small
                businesses, and give you fresher produce.

                In this article, you might include sections about:
                - What counts as a "local" farm
                - How local farms fit into your community
                - Why local produce can be higher quality
            """.trimIndent()
        )

        "a2" -> ArticleContent(
            title = "How to Read Produce Labels",
            body = """
                Grocery labels can be confusing: organic, local, non-GMO, fair trade, and more.
                Understanding these labels helps you make informed decisions.

                You could expand on:
                - The difference between "organic" and "local"
                - What "certified organic" actually means
                - How to spot marketing tricks vs real labels
            """.trimIndent()
        )

        "a3" -> ArticleContent(
            title = "Benefits of Seasonal Eating",
            body = """
                Eating seasonally means choosing fruits and vegetables that naturally grow
                in your region at a particular time of year.

                Benefits include:
                - Better flavor and freshness
                - Often lower cost
                - Support for local farmers
            """.trimIndent()
        )

        else -> ArticleContent(
            title = "Article",
            body = "This is a placeholder article."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = article.body,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

data class ArticleContent(
    val title: String,
    val body: String
)