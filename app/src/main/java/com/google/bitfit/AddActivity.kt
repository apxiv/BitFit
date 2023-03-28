package com.google.bitfit

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("MMM d, yyyy", Locale.US)
    private var isStartDateSelected = false
    private var feeling: Int = 0
    private lateinit var switch: Switch
    private var isPainful = false
    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Track your period"

        findViewById<Button>(R.id.startDateValue).setOnClickListener {
            print(calendar.time)
            isStartDateSelected = true
            showDatePicker()
        }

        findViewById<Button>(R.id.endDateValue).setOnClickListener {
            isStartDateSelected = false
            print(calendar.time)
            showDatePicker()
        }

        findViewById<ImageView>(R.id.sadEmoji).setOnClickListener {
            feeling = 0
            Toast.makeText(this, "Sad \uD83D\uDE2D", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.neutralEmoji).setOnClickListener {
            feeling = 1
            Toast.makeText(this, "OK \uD83D\uDE10", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.happyEmoji).setOnClickListener {
            feeling = 2
            Toast.makeText(this, "Happy \uD83D\uDE04", Toast.LENGTH_SHORT).show()
        }

        switch = findViewById(R.id.isPainfulValue)
        switch.setOnCheckedChangeListener { _, isChecked ->
            isPainful = isChecked
        }


        findViewById<FloatingActionButton>(R.id.add_item).setOnClickListener {
            if (startDate != null && endDate != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    (application as CycleApplication).db.cycleDao().insert(
                        CycleEntity(startDate!!, endDate!!, isPainful, feeling)
                    )
                }
            }

            val intent = Intent(this@AddActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // Disable the FloatingActionButton initially
        findViewById<FloatingActionButton>(R.id.add_item).isEnabled = false

        // Update the enabled state of the FloatingActionButton based on start and end date selection
        findViewById<Button>(R.id.startDateValue).doOnTextChanged { _, _, _, _ ->
            startDate = if (findViewById<Button>(R.id.startDateValue).text.isBlank()) null else calendar.time
            findViewById<FloatingActionButton>(R.id.add_item).isEnabled = startDate != null && endDate != null
        }

        findViewById<Button>(R.id.endDateValue).doOnTextChanged { _, _, _, _ ->
            endDate = if (findViewById<Button>(R.id.endDateValue).text.isBlank()) null else calendar.time
            findViewById<FloatingActionButton>(R.id.add_item).isEnabled = startDate != null && endDate != null
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.e("Calendar", "$year -- ${month + 1} -- $dayOfMonth")
        calendar.set(year, month, dayOfMonth)
        if (isStartDateSelected) {
            findViewById<Button>(R.id.startDateValue).text = formatter.format(calendar.timeInMillis)
            startDate = calendar.time
        } else {
            findViewById<Button>(R.id.endDateValue).text = formatter.format(calendar.timeInMillis)
            endDate = calendar.time
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}