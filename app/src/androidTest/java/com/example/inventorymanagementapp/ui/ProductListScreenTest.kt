package com.example.inventorymanagementapp.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.inventorymanagementapp.MainActivity
import com.example.inventorymanagementapp.domain.model.Product
import com.example.inventorymanagementapp.fakes.FakeProductRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class ProductListScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeRepository: FakeProductRepository


    @Before
    fun setup() {
        hiltRule.inject()

        fakeRepository.emitProducts(
            listOf(
                Product(
                    id = 1,
                    name = "Milk",
                    barcode = "123",
                    currentStock = 5,
                    minimumStock = 5,
                    description = "",
                    price = 1.2,
                    category = "Dairy",
                    supplierId = 1
                ),
                Product(
                    id = 2,
                    name = "Bread",
                    barcode = "999",
                    currentStock = 3,
                    minimumStock = 2,
                    description = "",
                    price = 0.8,
                    category = "Bakery",
                    supplierId = 2
                )
            )
        )
    }

    @Test
    fun productListDisplaysAllProducts() {
        composeRule.waitUntil(timeoutMillis = 2500) {
            composeRule.onAllNodesWithText("Milk").fetchSemanticsNodes().isNotEmpty()
        }

        // Assert that the UI contains the products
        composeRule.onNodeWithText("Milk").assertExists()
        composeRule.onNodeWithText("Bread").assertExists()
    }
}