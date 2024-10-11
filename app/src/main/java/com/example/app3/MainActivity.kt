package com.example.app3

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var selectedIngredientsLayout: LinearLayout
    private lateinit var textViewTotal: TextView
    private var totalPrice = 6.00 // Starting price

    private val ingredientImages = mapOf(
        R.id.checkbox1 to R.drawable.cheese,
        R.id.checkbox2 to R.drawable.tomato,
        R.id.checkbox3 to R.drawable.lettuce,
        R.id.checkbox4 to R.drawable.pickles,
        R.id.checkbox5 to R.drawable.onion,
        R.id.checkbox6 to R.drawable.ketchup
    )

    private val checkboxes = mutableListOf<CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectedIngredientsLayout = findViewById(R.id.selectedIngredientsLayout)
        textViewTotal = findViewById(R.id.textViewTotal)

        updateTotalPriceDisplay()

        // Set up click listeners for all checkboxes
        for (checkboxId in ingredientImages.keys) {
            val checkbox = findViewById<CheckBox>(checkboxId)
            checkboxes.add(checkbox)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                updateSelectedIngredients(checkboxId, isChecked)
                updateTotalPrice(checkboxId, isChecked)
            }
        }

        // Set up Order button
        findViewById<Button>(R.id.button).setOnClickListener {
            showOrderConfirmation()
        }

        // Set up Clear Order button
        findViewById<Button>(R.id.button2).setOnClickListener {
            clearOrder()
        }
    }

    private fun updateSelectedIngredients(checkboxId: Int, isChecked: Boolean) {
        val imageResId = ingredientImages[checkboxId] ?: return

        if (isChecked) {
            // Add image
            val imageView = ImageView(this).apply {
                setImageResource(imageResId)
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.ingredient_image_size),
                    resources.getDimensionPixelSize(R.dimen.ingredient_image_size)
                ).apply {
                    setMargins(0, 0, 8, 0)
                }
                scaleType = ImageView.ScaleType.FIT_CENTER
                adjustViewBounds = true
                tag = checkboxId
            }
            selectedIngredientsLayout.addView(imageView)
        } else {
            // Remove image
            val viewToRemove = selectedIngredientsLayout.findViewWithTag<ImageView>(checkboxId)
            selectedIngredientsLayout.removeView(viewToRemove)
        }
    }

    private fun updateTotalPrice(checkboxId: Int, isChecked: Boolean) {
        val priceChange = when (checkboxId) {
            R.id.checkbox1 -> 1.5 // Cheese
            R.id.checkbox2 -> 1.0 // Tomatoes
            R.id.checkbox3 -> 1.0 // Lettuce
            R.id.checkbox4 -> 1.0 // Pickles
            R.id.checkbox5 -> 1.0 // Onion
            R.id.checkbox6 -> 0.5 // Ketchup
            else -> 0.0
        }

        if (isChecked) {
            totalPrice += priceChange
        } else {
            totalPrice -= priceChange
        }

        updateTotalPriceDisplay()
    }

    private fun updateTotalPriceDisplay() {
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale.UK).format(totalPrice)
        textViewTotal.text = formattedPrice
    }

    private fun showOrderConfirmation() {
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale.UK).format(totalPrice)
        AlertDialog.Builder(this)
            .setTitle("Order Confirmation")
            .setMessage("Your total order amount is $formattedPrice")
            .setPositiveButton("Pay Now") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    private fun clearOrder() {
        // Uncheck all checkboxes
        for (checkbox in checkboxes) {
            checkbox.isChecked = false
        }

        // Clear selected ingredients
        selectedIngredientsLayout.removeAllViews()

        // Reset total price
        totalPrice = 6.00
        updateTotalPriceDisplay()
    }
}