package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * ArticleScreen displays long-form article content based on an article ID.
 *
 * This screen retrieves article text from a small set of predefined
 * in-app articles ("a1", "a2", "a3"). Each article is represented
 * by a title and a body of text. If the provided ID does not match
 * any known article, a default placeholder article is shown.
 *
 * The content is scrollable and wrapped inside a Scaffold with the shared
 * AgriTopBar at the top.
 *
 * @param navController Navigation controller used for navigating between screens.
 * @param articleId A string identifier determining which article to display.
 *
 * Usage example:
 * navController.navigate("article/a1")
 */
@Composable
fun ArticleScreen(
    navController: NavController,
    articleId: String
) {
    // Selects a title + body based on the article ID
    val (title, body) = when (articleId) {

        "a1" -> "The Benefits and Basics of Home Gardening" to
                "Home gardening has become increasingly popular in recent years, offering individuals a rewarding way to grow their own food, improve well-being, and contribute to environmental sustainability. Whether practiced in a backyard, on a balcony, or even indoors, gardening provides both practical and personal benefits that extend far beyond the harvest.\n" +
                "\n" +
                "One of the greatest advantages of home gardening is access to fresh, nutritious food. Vegetables and herbs grown at home often taste better and are more nutrient-dense than store-bought alternatives because they are harvested at peak ripeness. Growing your own produce also gives you complete control over what goes into the soil and onto the plants, allowing gardeners to avoid synthetic pesticides and embrace organic practices. Even a small garden with tomatoes, lettuce, or herbs can supplement meals and reduce dependence on store produce.\n" +
                "\n" +
                "Gardening also offers economic benefits. Seeds and soil are relatively inexpensive compared to the long-term cost savings of harvesting your own fruits and vegetables. Families can save money while enjoying healthier meals, and excess produce can even be shared with neighbors or preserved for later use. In this way, home gardens contribute to both financial savings and stronger community ties.\n" +
                "\n" +
                "Equally important are the environmental impacts. Home gardens reduce “food miles,” the distance food travels from farm to plate. Growing locally in your backyard cuts down on transportation emissions and packaging waste. Many gardeners also practice composting, turning food scraps into nutrient-rich soil amendments that reduce household waste and improve soil health.\n" +
                "Beyond practical advantages, gardening nurtures mental and physical well-being. Studies have shown that tending to plants reduces stress, improves mood, and even encourages physical activity. Digging, planting, and weeding all contribute to exercise, while spending time outdoors fosters mindfulness and a deeper connection with nature. For children, gardening serves as a hands-on learning experience about biology, nutrition, and responsibility.\n" +
                "\n" +
                "Getting started with home gardening is accessible to nearly everyone. Beginners can start small with container gardens or raised beds, focusing on easy-to-grow crops such as herbs, lettuce, or peppers. Success comes from choosing plants suited to the local climate, preparing nutrient-rich soil, and providing consistent watering. With experience, gardeners can experiment with crop rotation, organic pest control, and seasonal planting schedules.\n" +
                "\n" +
                "Ultimately, home gardening is more than just a hobby; it is a lifestyle that promotes health, sustainability, and community. By cultivating a personal garden space, individuals gain fresh produce, save money, reduce their environmental footprint, and enjoy the many mental and physical benefits of working with the earth. In a world where many are disconnected from the origins of their food, home gardening offers a simple yet powerful way to reconnect with nature and foster a more sustainable future."

        "a2" -> "The Importance of Water Conservation" to
                "Water is one of the most vital resources on Earth, yet it is often taken for granted. With growing populations, climate change, and increasing demand, conserving water has become a global necessity. Water conservation is not only about saving money on utility bills; it is about protecting ecosystems, ensuring clean water for future generations, and maintaining the balance of our natural environment.\n" +
                "\n" +
                "One of the most pressing reasons for conserving water is scarcity. Although Earth is often called the “blue planet,” less than one percent of its water is readily available for human use. Freshwater sources like rivers, lakes, and aquifers are under immense pressure as communities, agriculture, and industries compete for limited supplies. In many regions, overuse has led to depleted reservoirs and groundwater shortages, threatening both people and wildlife.\n" +
                "\n" +
                "Conserving water also reduces the strain on energy systems. Water must be pumped, treated, and transported before it reaches our homes, all of which require electricity. By using less water, we reduce the energy demand, thereby cutting greenhouse gas emissions and contributing to climate change mitigation. For example, simple steps like fixing leaks or running dishwashers only when full can save thousands of gallons of water and significant amounts of energy each year.\n" +
                "\n" +
                "At the community level, water conservation helps maintain healthy ecosystems. Wetlands, rivers, and aquifers rely on balanced water levels to support fish, birds, and plant life. Overuse or pollution disrupts these ecosystems, reducing biodiversity and harming food chains. By practicing responsible water use, we protect not only human needs but also the broader natural environment.\n" +
                "\n" +
                "There are many practical ways individuals can conserve water. Indoors, strategies include installing low-flow faucets and showerheads, repairing leaks promptly, and using water-efficient appliances. Outdoors, homeowners can practice xeriscaping—landscaping with drought-resistant plants—or collect rainwater for irrigation. Small behavior changes, like turning off the tap while brushing teeth or watering gardens in the early morning, also make a significant impact.\n" +
                "\n" +
                "Education plays an important role as well. Teaching children and communities about the value of water encourages long-term cultural shifts toward conservation. When individuals recognize that their actions matter, they are more likely to adopt sustainable practices and advocate for broader water-saving policies.\n" +
                "\n" +
                "In conclusion, water conservation is essential for addressing scarcity, reducing energy use, protecting ecosystems, and ensuring a sustainable future. Every drop saved contributes to the health of our planet and the well-being of future generations. By making simple changes in our daily lives, we can collectively make a profound difference.\n"

        "a3" -> "The Value of Natural Fertilizers" to
                "Fertilizers play a vital role in agriculture and gardening, providing plants with the nutrients they need to grow strong and healthy. While chemical or synthetic fertilizers have become common in modern farming, natural fertilizers are increasingly valued for their sustainability, safety, and long-term benefits to both crops and the environment. Natural fertilizers are derived from organic materials such as compost, manure, bone meal, or plant residues, and they offer a balanced way to enrich soil health while reducing harmful impacts.\n" +
                "\n" +
                "One of the key advantages of natural fertilizers is their ability to improve soil structure. Unlike synthetic fertilizers, which primarily focus on immediate nutrient delivery, organic materials increase the soil’s ability to retain water and promote better aeration. This creates a healthier environment for root systems and supports beneficial organisms like earthworms and microbes. These organisms further break down organic matter, releasing nutrients slowly and naturally over time.\n" +
                "\n" +
                "Natural fertilizers also reduce the risk of chemical buildup and pollution. Synthetic fertilizers often release nutrients too quickly, leading to runoff that contaminates rivers, lakes, and groundwater. This can cause harmful algae blooms and damage aquatic ecosystems. By contrast, natural fertilizers release nutrients gradually, minimizing waste and lowering the environmental footprint of farming and gardening practices.\n" +
                "\n" +
                "Another important benefit is food safety and health. Crops grown with natural fertilizers are less likely to contain chemical residues, making them a healthier choice for consumers. This aligns with the growing demand for organic produce, which is valued for being environmentally friendly and free of synthetic chemicals.\n" +
                "\n" +
                "Economically, natural fertilizers can also be cost-effective, especially when produced locally. Gardeners and farmers can create their own compost from kitchen scraps, yard waste, and animal manure, turning what would otherwise be discarded into a valuable resource. This reduces dependency on purchased inputs and encourages a cycle of reuse that benefits households and communities.\n" +
                "\n" +
                "Getting started with natural fertilizers is simple. Composting food scraps, using grass clippings as mulch, or adding well-aged manure to garden beds are all effective ways to boost soil fertility. Each method provides essential nutrients like nitrogen, phosphorus, and potassium, while also enhancing the overall quality of the soil. For small-scale gardeners, natural fertilizers are both accessible and rewarding.\n" +
                "\n" +
                "In conclusion, natural fertilizers represent a sustainable, safe, and effective alternative to synthetic products. They support soil health, reduce environmental harm, promote nutritious crops, and often cost less in the long run. By choosing natural options, farmers and gardeners alike contribute to a healthier planet and more resilient food systems.\n"

        else -> "Article" to
                "This is where the full article content will appear."
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
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
