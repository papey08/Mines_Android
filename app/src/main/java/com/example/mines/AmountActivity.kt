package com.example.mines

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.isDigitsOnly

class AmountActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amount_activity)

        val bOK = findViewById<Button>(R.id.buttonOK)
        val bMore = findViewById<Button>(R.id.button_upper)
        val bLess = findViewById<Button>(R.id.button_lower)
        val tAmount = findViewById<TextView>(R.id.text_amount)

        var amount = 6

        bMore.setOnClickListener {
            if (amount < 12) {
                amount++
                tAmount.text = amount.toString()
            }
        }

        bLess.setOnClickListener {
            if (amount > 6) {
                amount--
                tAmount.text = amount.toString()
            }
        }

        bOK.setOnClickListener {
            val i = Intent(this,GameActivity::class.java)
            i.putExtra("tagFromAmountToGame", amount.toString())
            startActivity(i)
        }

    }

}
