package com.example.weatherforecastapp

import androidx.fragment.app.activityViewModels
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.LocationRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: WeatherViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLastLocationAndFetchWeather()
        } else {
            viewModel.fetchWeatherData(39.925533, 32.866287)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        hourlyAdapter = HourlyAdapter(emptyList())
        dailyAdapter = DailyAdapter(emptyList())

        binding.recyclerHourly.adapter = hourlyAdapter
        binding.recyclerDaily.adapter = dailyAdapter

        viewModel.currentWeather.observe(viewLifecycleOwner) { weatherInfo ->
            binding.tvCity.text = weatherInfo.cityName
            binding.tvTemp.text = "${weatherInfo.temp}°C"
            binding.tvDescription.text = weatherInfo.description
        }

        viewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyList ->
            hourlyAdapter = HourlyAdapter(hourlyList)
            binding.recyclerHourly.adapter = hourlyAdapter
        }

        viewModel.dailyForecast.observe(viewLifecycleOwner) { dailyList ->
            dailyAdapter.updateList(dailyList)
        }

        binding.recyclerHourly.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerDaily.layoutManager = LinearLayoutManager(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocationAndFetchWeather()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLastLocationAndFetchWeather() {
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                    this

                override fun isCancellationRequested(): Boolean = false
            }
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                viewModel.fetchWeatherData(location.latitude, location.longitude)
            } else {
                viewModel.fetchWeatherData(39.925533, 32.866287)
            }
        }
    }


}