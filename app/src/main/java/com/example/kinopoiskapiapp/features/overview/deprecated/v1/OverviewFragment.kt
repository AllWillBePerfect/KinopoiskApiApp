package com.example.kinopoiskapiapp.features.overview.deprecated.v1

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
import com.example.kinopoiskapiapp.R
import com.example.kinopoiskapiapp.core.view.BaseFragment
import com.example.kinopoiskapiapp.core.view.adapter.UniversalRecyclerViewAdapter
import com.example.kinopoiskapiapp.databinding.FragmentOverviewBinding
import com.example.kinopoiskapiapp.features.moviedetails.MovieDetailsFragment
import com.example.kinopoiskapiapp.features.overview.adapter.MovieItemDelegate
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi
@Deprecated("")
class OverviewFragment : BaseFragment<FragmentOverviewBinding>(FragmentOverviewBinding::inflate) {

    private val viewModel: OverviewViewModel by activityViewModels()
    private lateinit var adapter: UniversalRecyclerViewAdapter<MovieUi>
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                MovieItemDelegate(viewModel::fetchMovieById)
            ),
            diffUtilCallback = MovieUi.MovieItemDiffUtil()
        )
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Проверка для прокрутки вниз (конец списка)
                if (viewModel.getLoadingState() != true && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    viewModel.loadMoreData(isScrollingDown = true)
                }

//                // Проверка для прокрутки вверх (начало списка)
//                if (viewModel.getLoadingState() != true && firstVisibleItemPosition == 0 && viewModel.currentPage > 1) {
//                    viewModel.loadMoreData(isScrollingDown = false)
//                }
            }
        })

        binding.textInputEditText.doAfterTextChanged {
            viewModel.setText(it.toString())
        }

        viewModel.moviesLiveData.observe(viewLifecycleOwner) { movies ->
            adapter.items = movies
            if (viewModel.currentPage == 0) {
                binding.recyclerView.scrollToPosition(0)
                viewModel.currentPage++
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Log.d("OverviewFragment onError", "$error: ${error.message}")
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
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

        handler.postDelayed({
            binding.textInputEditText.requestFocus()

            WindowCompat.getInsetsController(
                requireActivity().window,
                binding.textInputEditText
            ).show(
                WindowInsetsCompat.Type.ime()
            )
        },350)

//        viewModel.fetchMoviesByName(viewModel.query)

    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}