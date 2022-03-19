package com.example.aceplussolution.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aceplussolution.R
import com.example.aceplussolution.databinding.ListItemMovieListBinding
import com.example.aceplussolution.roomdb.entity.MovieEntity
import com.example.aceplussolution.ui.fragment.MovieListFragment
import com.example.aceplussolution.viewmodel.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the adapter class with diff util to show the movie items.
 */
class MovieAdapter(
    private val context: Context,
    private val movieViewModel: MovieViewModel,
    private val movieListFragment: MovieListFragment
) :
    ListAdapter<MovieEntity, MovieAdapter.MViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemMovieListBinding.inflate(inflater, parent, false)
        return MViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class MViewHolder(val binding: ListItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieEntity) {
            CoroutineScope(Dispatchers.Main).launch {
                setData(item, this@MViewHolder)
            }
        }
    }

    /**
     * This is the function of setting the data.
     * call the navigateToDetail function from the movieList Fragment when item is clicked.
     * if the fav status is 0, show the un favorite icon. else show the favorite icon
     * get the movie favorite status by it's id and update it accordingly if fav icon is clicked.
     * if the fav status is 0, change to 1. else change to 0
     */
    private suspend fun setData(movie: MovieEntity, holder: MViewHolder) {
        holder.itemView.setOnClickListener {
            movieListFragment.navigateToDetail(movie)
        }
        with(holder)
        {
            if (movieViewModel.getFav(movie.id) != null) {
                if (movieViewModel.getFav(movie.id) == 0) {
                    binding.imgFav.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_un_favorite_24))
                } else if (movieViewModel.getFav(movie.id) == 1) {
                    binding.imgFav.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_24))
                }
            }
            binding.imgFav.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    if (movieViewModel.getFav(movie.id) == 0) {
                        movieViewModel.updateFav(1, movie.id)
                    } else {
                        movieViewModel.updateFav(0, movie.id)
                    }
                }
            }
            binding.txtName.text = movie.title
            Glide.with(context).load("https://image.tmdb.org/t/p/original/${movie.posterPath}")
                .placeholder(
                    R.drawable.placeholder
                ).into(binding.imgImage)
        }
    }
}
