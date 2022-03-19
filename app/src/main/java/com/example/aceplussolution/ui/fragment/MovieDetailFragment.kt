package com.example.aceplussolution.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.aceplussolution.R
import com.example.aceplussolution.databinding.FragmentMovieDetailBinding
import com.example.aceplussolution.utility.kodeinViewModel
import com.example.aceplussolution.viewmodel.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is MovieDetailFragment for showing detail of movie
 */
class MovieDetailFragment : Fragment(), DIAware {
    override val di by closestDI()

    private val binding by lazy { FragmentMovieDetailBinding.inflate(layoutInflater) }
    val args by navArgs<MovieDetailFragmentArgs>()
    private val movieViewModel: MovieViewModel by kodeinViewModel()

    /**
     * In this onCreateView method,
     * return the layout root
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    /**
     * In this onViewCreated method,
     * 1. set the data of Movie detail
     * 2. handle back arrow action
     * 3. handle Favorite icon action
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            setData()
            delay(1000L)
            binding.sflDetail.stopShimmer()
            binding.sflDetail.visibility = View.GONE
            binding.cslDetail.visibility = View.VISIBLE
        }

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.fabFavorite.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                handleFabClick()
            }
        }
    }

    /**
     * In this setData method,
     * 1. apply the detail from the args
     * 2. if the fav status is 0, set the brown color on Favorite icon else set the red color
     */
    private suspend fun setData() = with(binding) {
        args.movieEntity.apply {
            if (movieViewModel.getFav(this.id) != null) {
                if (movieViewModel.getFav(this.id) == 0) {
                    binding.fabFavorite.setColorFilter(requireContext().resources.getColor(R.color.brown));
                } else if (movieViewModel.getFav(this.id) == 1) {
                    binding.fabFavorite.setColorFilter(requireContext().resources.getColor(R.color.red));

                }
            }
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/original/${this.backdropPath}")
                .placeholder(R.drawable.placeholder)
                .into(imgPhoto)
            txtName.text = this.title
            txtReleaseDate.text = "Release Date : ${this.releaseDate}"
            txtVotes.text = "${this.voteAverage}/10"
            txtSummary.text = this.overview
        }
    }

    /**
     * In this setData method,
     * 1. get the movie favorite status by it's id and update it accordingly if fav icon is clicked.
     * 2. if the fav status is 0, change to 1. else change to 0
     * 3. and set the color accordingly.
     */
    private suspend fun handleFabClick() {
        args.movieEntity.apply {
            if (movieViewModel.getFav(this.id) == 0) {
                movieViewModel.updateFav(1, this.id)
                binding.fabFavorite.setColorFilter(requireContext().resources.getColor(R.color.red));
            } else {
                movieViewModel.updateFav(0, this.id)
                binding.fabFavorite.setColorFilter(requireContext().resources.getColor(R.color.brown));

            }
        }
    }
}