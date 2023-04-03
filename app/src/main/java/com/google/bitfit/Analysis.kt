package com.google.bitfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Analysis : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val painfulDays: TextView = view.findViewById(R.id.painfulDays)
        lifecycleScope.launch(Dispatchers.IO) {
            val numberOfPainfulDays = (requireActivity().application as CycleApplication).db.cycleDao().getNumberOfPainfulDays()
            withContext(Dispatchers.Main) {
                painfulDays.text = numberOfPainfulDays.toString()
            }
        }
    }
}