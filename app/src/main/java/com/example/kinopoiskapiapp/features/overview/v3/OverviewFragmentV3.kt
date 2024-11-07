package com.example.kinopoiskapiapp.features.overview.v3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoiskapiapp.R
import com.example.kinopoiskapiapp.core.data.utils.Result
import com.example.kinopoiskapiapp.core.view.BaseFragment
import com.example.kinopoiskapiapp.core.view.adapter.UniversalRecyclerViewAdapter
import com.example.kinopoiskapiapp.databinding.FragmentOverviewBinding
import com.example.kinopoiskapiapp.features.debug.DebugFragment
import com.example.kinopoiskapiapp.features.moviedetails.MovieDetailsFragment
import com.example.kinopoiskapiapp.features.overview.adapter.MovieItemDelegate
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi

class OverviewFragmentV3 : BaseFragment<FragmentOverviewBinding>(FragmentOverviewBinding::inflate) {

    private val viewModel: OverviewViewModelV3 by activityViewModels()
    private lateinit var adapter: UniversalRecyclerViewAdapter<MovieUi>
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupEditTextListener()
        setupBurgerButton()
        setupScrollListener()
        setupViewModel()
        openKeyboard()
    }

    private fun setupAdapter() {
        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                MovieItemDelegate(viewModel::fetchMovieById)
            ),
            diffUtilCallback = MovieUi.MovieItemDiffUtil()
        )
        binding.recyclerView.adapter = adapter
    }

    private fun setupEditTextListener() {
        binding.textInputEditText.doAfterTextChanged {
            viewModel.firstFetchMovies(it.toString())
        }
    }

    private fun setupScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Проверка для прокрутки вниз (конец списка)
                if (!viewModel.isLoading() && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    Log.d("OverviewFragmentV3", "load more data")
                    viewModel.nextPageFetchMovies()
                }

            }
        })
    }

    fun setupBurgerButton() {
        binding.burgerIcon.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_container, DebugFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            it.let { event ->
                when (event) {
                    is Result.Success -> {
                        Log.d("OverviewFragmentV3 onLoading", event.data.toString())
                        adapter.items = event.data
                    }

                    is Result.Loading -> {
                        Log.d("OverviewFragmentV3 onLoading", "Loading...")
                    }

                    is Result.Error -> {
                        Log.d(
                            "OverviewFragmentV3 onError",
                            "${event.exception}: ${event.exception.message}"
                        )
                        Toast.makeText(
                            requireContext(),
                            event.exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewModel.navigateViewModel.observe(viewLifecycleOwner) {
            it.event?.let { event ->
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_container, MovieDetailsFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }


    private fun openKeyboard() {
        handler.postDelayed({
            binding.textInputEditText.requestFocus()

            WindowCompat.getInsetsController(
                requireActivity().window,
                binding.textInputEditText
            ).show(
                WindowInsetsCompat.Type.ime()
            )
        },350)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

}