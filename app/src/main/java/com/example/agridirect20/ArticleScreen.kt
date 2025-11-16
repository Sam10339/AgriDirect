package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ArticleScreen(
    navController: NavController,
    articleId: String
) {
    // Simple placeholder content based on articleId
    val (title, body) = when (articleId) {
        "a1" -> "Intro to Local Farming" to
                "Local farms play a key role in providing fresh, seasonal produce " +
                "to nearby communities. This article explains why buying local matters."
        "a2" -> "How to Read Produce Labels" to
                "Produce labels can be confusing. This article explains common " +
                "terms like organic, local, fair trade, and more."
        "a3" -> "Benefits of Seasonal Eating" to
                "Eating seasonally can improve freshness, reduce costs, and support " +
                "local farmers. Learn how to plan meals around the seasons."
        else -> "Article" to
                "This is where the full article content will appear."
    }

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
