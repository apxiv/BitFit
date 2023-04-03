package com.google.bitfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class Track : Fragment() {

    private lateinit var cycleRecyclerView: RecyclerView
    private val cycleList = mutableListOf<CycleEntity>()
    private lateinit var cycleAdapter: CycleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_track, container, false)

        cycleRecyclerView = view.findViewById(R.id.recyclerView)
        cycleAdapter = CycleAdapter(cycleList)
        cycleRecyclerView.adapter = cycleAdapter
        cycleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            (requireActivity().application as CycleApplication).db.cycleDao().getAll().collect {databaseList ->
                cycleList.clear()
                databaseList.map { mappedList ->
                    cycleList.addAll(listOf(mappedList))
                    Log.i("list", "$cycleList")
                    cycleAdapter.notifyDataSetChanged()
                }
            }
        }
        cycleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val addFloatingActionButton = view.findViewById<FloatingActionButton>(R.id.button_add)
        addFloatingActionButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddActivity::class.java)
            startActivity(intent)
        }
    }
}