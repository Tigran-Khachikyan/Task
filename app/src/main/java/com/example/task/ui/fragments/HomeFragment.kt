package com.example.task.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.task.ACTION_OPEN_LOCATION_ON_NOTIFICATION_CLICK
import com.example.task.R
import com.example.task.databinding.FragmentHomeBinding
import com.example.task.ui.MainActivity
import com.example.task.ui.SectionsPagerAdapter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(v)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        if ((requireActivity() as MainActivity).intent.action ==
            ACTION_OPEN_LOCATION_ON_NOTIFICATION_CLICK
        )
            openServiceFragment()
    }

    private fun openServiceFragment() {
        binding.tabLayout.getTabAt(2)?.select()
    }
}