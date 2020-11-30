package com.example.task.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.task.*
import com.example.task.model.LocationInfo
import com.example.task.receiver.LocationInfoReceiver
import com.example.task.receiver.LocationStateReceiver
import com.example.task.service.TaskService
import com.example.task.ui.MainActivity
import com.example.task.ui.adapters.LocationAdapter
import com.example.task.ui.viewmodels.PermissionsViewModel
import kotlinx.android.synthetic.main.fragment_service.*


class ServiceFragment : Fragment() {

    private lateinit var permissionsViewModel: PermissionsViewModel
    private val adapter: LocationAdapter by lazy { LocationAdapter() }
    private var playing: Boolean = false
    private var locationUpdateReceiver: LocationInfoReceiver? = null
    private var locationStateReceiver: LocationStateReceiver? = null
    private lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        permissionsViewModel = ViewModelProvider(activity).get(PermissionsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        registerReceivers()
        registerObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeReceivers()
    }


    private fun initView() {
        recLocationInfo.setHasFixedSize(true)
        recLocationInfo.adapter = adapter
        playing = isMyServiceRunning(TaskService::class.java, requireContext())
        if (playing) showServiceRunning()
        else showServiceStopped()
        btnStartStop.setOnClickListener {
            if (playing) stop()
            else start()
        }
    }


    private fun stop() {
        val serviceIntent = Intent(requireActivity(), TaskService::class.java)
        requireActivity().stopService(serviceIntent)
        playing = false
        showServiceStopped()
    }

    private fun start() {
        when {
            !isPermissionGranted(requireActivity()) -> {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_CODE_PERMISSION_LOCATION
                )
            }
            !isLocationEnabled(requireContext()) -> enableLocation(requireContext())

            else -> {
                val serviceIntent = Intent(requireActivity(), TaskService::class.java)
                ContextCompat.startForegroundService(requireActivity(), serviceIntent)
                playing = true
                showServiceRunning()
            }
        }
    }


    private fun showLocation(info: LocationInfo?) {
        progressService.hide()
        adapter.updateLocation(getFieldsToShow(info))
    }

    private fun getFieldsToShow(info: LocationInfo?): List<Pair<String, String>>? {
        return info?.run {
            val result = mutableListOf<Pair<String, String>>()
            address?.let { result.add(Pair(getString(R.string.address), it)) }
            city?.let { result.add(Pair(getString(R.string.city), it)) }
            region?.let { result.add(Pair(getString(R.string.region), it)) }
            country?.let { result.add(Pair(getString(R.string.country), it)) }
            val latAndLong = "$latitude , $longitude"
            result.add(Pair(getString(R.string.lat_long), latAndLong))
            result
        }
    }

    private fun registerReceivers() {
        locationUpdateReceiver = LocationInfoReceiver { showLocation(it) }
        locationUpdateReceiver?.register(requireContext())
        locationStateReceiver = LocationStateReceiver { start() }
        locationStateReceiver?.register(requireContext())
    }

    private fun removeReceivers() {
        locationUpdateReceiver?.unregister(requireContext())
        locationStateReceiver?.unregister(requireContext())
    }

    private fun registerObservers() {
        permissionsViewModel.isLocationPermissionGranted().observe(viewLifecycleOwner, {
            if (isLocationEnabled(requireContext())) start()
            else enableLocation(requireContext())
        })
    }

    private fun showServiceRunning() {
        lottie_play_music.visibility = View.VISIBLE
        recLocationInfo.visibility = View.VISIBLE
        btnStartStop.customize(R.string.stop, R.drawable.ic_stop)
        progressService.show()
        tvStatusService.hide()
    }

    private fun showServiceStopped() {
        lottie_play_music.visibility = View.GONE
        recLocationInfo.visibility = View.GONE
        btnStartStop.customize(R.string.start, R.drawable.ic_start)
        progressService.hide()
        tvStatusService.showStatus(R.string.startToGetLocation)
    }

}