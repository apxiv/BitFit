package com.google.bitfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.bitfit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var cycleRecyclerView: RecyclerView
    private val cycleList = mutableListOf<CycleEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ActivityMainBinding.inflate(layoutInflater).root)

        cycleRecyclerView = findViewById(R.id.recyclerView)
        val cycleAdapter = CycleAdapter(cycleList)
        cycleRecyclerView.adapter = cycleAdapter

        lifecycleScope.launch {
            (application as CycleApplication).db.cycleDao().getAll().collect {databaseList ->
                databaseList.map { mappedList ->
                    cycleList.addAll(listOf(mappedList))
                    Log.i("list", "$cycleList")
                    cycleAdapter.notifyDataSetChanged()
                }
            }
        }

        cycleRecyclerView.layoutManager = LinearLayoutManager(this)

        val painfulDays: TextView = findViewById(R.id.painfulDays)
        lifecycleScope.launch(Dispatchers.IO) {
            val numberOfPainfulDays = (application as CycleApplication).db.cycleDao().getNumberOfPainfulDays()
            withContext(Dispatchers.Main) {
                painfulDays.text = numberOfPainfulDays.toString()
            }
        }

        val addFloatingActionButton = findViewById<FloatingActionButton>(R.id.button_add)
        addFloatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }
    }
}