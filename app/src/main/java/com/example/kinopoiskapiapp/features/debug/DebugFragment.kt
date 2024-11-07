package com.example.kinopoiskapiapp.features.debug

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.kinopoiskapiapp.core.database.models.MovieByIdDbo
import com.example.kinopoiskapiapp.core.view.BaseFragment
import com.example.kinopoiskapiapp.core.view.adapter.UniversalRecyclerViewAdapter
import com.example.kinopoiskapiapp.databinding.FragmentDebugBinding
import com.example.kinopoiskapiapp.features.overview.adapter.MovieDebugRecordItemDelegate
import com.example.kinopoiskapiapp.features.overview.adapter.models.MovieUi

class DebugFragment : BaseFragment<FragmentDebugBinding>(FragmentDebugBinding::inflate) {

    private val viewModel: DebugViewModel by activityViewModels()
    private lateinit var adapter: UniversalRecyclerViewAdapter<MovieUi>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                MovieDebugRecordItemDelegate()
            ),
            diffUtilCallback = MovieUi.MovieItemDiffUtil()
        )

        binding.recyclerView.adapter = adapter

        viewModel.dbMoviesLiveData.observe(viewLifecycleOwner) {
            adapter.items = map(it)
        }
    }

    private fun map(list: List<MovieByIdDbo>): List<MovieUi> = list.map {
        MovieUi.MovieItem(
            id = it.id,
            posterUri = Uri.parse(it.poster?.previewUrl ?: ""),
            name = it.name ?: ""
        )
    }
}
