package com.example.kinopoiskapiapp.features.moviedetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.kinopoiskapiapp.R
import com.example.kinopoiskapiapp.core.view.BaseFragment
import com.example.kinopoiskapiapp.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate) {

    private val viewModel: MovieDetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movieLiveData.observe(viewLifecycleOwner) {
            it.event?.let { movie ->
                Glide.with(requireContext())
                    .load(movie.posterDtoPart?.url)
                    .centerCrop()
                    .error(R.drawable.error_load)
                    .into(binding.poster)
            }
        }

    }
}