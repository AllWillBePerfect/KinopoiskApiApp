package com.example.kinopoiskapiapp.features.overview.deprecated.v2

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoiskapiapp.core.view.BaseFragment
import com.example.kinopoiskapiapp.core.view.adapter.UniversalRecyclerViewAdapter
import com.example.kinopoiskapiapp.databinding.FragmentOverviewBinding
import com.example.kinopoiskapiapp.features.overview.adapter.MovieItemDelegate
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi
import com.example.kinopoiskapiapp.core.data.utils.Result

@Deprecated("")
class OverviewFragmentV2 : BaseFragment<FragmentOverviewBinding>(FragmentOverviewBinding::inflate) {

    private val viewModel: OverviewViewModelV2 by activityViewModels()
    private lateinit var adapter: UniversalRecyclerViewAdapter<MovieUi>
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewBinding()
        setupLiveData()
        openKeyboard()
    }

    private fun setupViewBinding() {
        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                MovieItemDelegate(viewModel::fetchMovieById)
            ),
            diffUtilCallback = MovieUi.MovieItemDiffUtil()
        )
        binding.recyclerView.adapter = adapter

        binding.textInputEditText.doAfterTextChanged {
            viewModel.setText(it.toString())
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Проверка для прокрутки вниз (конец списка)
                if (viewModel.getLoadingState() != true && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    Log.d("OverviewFragment", "load more data")
                    viewModel.loadMoreData()
                }

            }
        })
    }

    private fun setupLiveData() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            it.eventForCheck?.let { event ->
                when (event) {
                    is Result.Success -> {
                        adapter.items = event.data
                        if (viewModel.currentPage == 0) {
                            binding.recyclerView.scrollToPosition(0)
                            viewModel.currentPage++
                        }
                    }
                    is Result.Loading -> {}
                    is Result.Error -> {
                        Log.d("OverviewFragment onError", "${event.exception}: ${event.exception.message}")
                        Toast.makeText(requireContext(), event.exception.message, Toast.LENGTH_SHORT).show()
                    }
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
}