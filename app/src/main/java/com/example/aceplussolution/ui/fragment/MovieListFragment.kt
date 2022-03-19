package com.example.aceplussolution.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aceplussolution.R
import com.example.aceplussolution.adapter.MovieAdapter
import com.example.aceplussolution.common.IS_FIRST_TIME
import com.example.aceplussolution.databinding.FragmentMovieListBinding
import com.example.aceplussolution.roomdb.entity.MovieEntity
import com.example.aceplussolution.ui.MainActivity
import com.example.aceplussolution.utility.getBooleanPref
import com.example.aceplussolution.utility.kodeinViewModel
import com.example.aceplussolution.utility.setBooleanPref
import com.example.aceplussolution.viewmodel.MovieViewModel
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is MovieListFragment for showing the movies list
 */
class MovieListFragment : Fragment(), DIAware {
    private val binding by lazy { FragmentMovieListBinding.inflate(layoutInflater) }
    override val di by closestDI()
    var alert: AlertDialog? = null
    private val movieViewModel: MovieViewModel by kodeinViewModel()
    private lateinit var movieAdapter: MovieAdapter

    /**
     * In this onCreate method,
     * setting the toolbar first. then
     * set option menu true
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar()
        setHasOptionsMenu(true)
    }

    /**
     * In this onCreateOptionMenu method,
     * just inflate the main menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

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
     * 1. setting the adapter
     * 2. handle Swipe Refresh
     * 3. loadData
     * 4. observeLocalMovies
     * 5. observeMovies
     * 6. setting the isFirstTime value from pref
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        handleSwipe()
        loadData()
        observeLocalMovies()
        observeMovies()
    }

    /**
     * In this setToolbar method,
     * setting the toolbar as supportActionBar
     * setting the toolbar back icon
     */
    private fun setToolbar() {
        (activity as MainActivity).delegate.setSupportActionBar(binding.tlbToolbar)
        (activity as MainActivity).delegate.supportActionBar?.setIcon(R.drawable.ic_baseline_arrow_back_24_white)
    }

    /**
     * In this setAdapter method,
     * setting the movieAdapter
     * setting the movieAdapter to recyclerMovieList
     */
    private fun setAdapter() {
        movieAdapter = MovieAdapter(requireContext(), movieViewModel, this)
        binding.rcvMovieList.adapter = movieAdapter
    }

    /**
     * In this loadData method,
     * fetching the movieDBLiveData
     * fetching the movieListFromAPI
     */
    private fun loadData() {
        movieViewModel.getMovieDBLiveData()
        movieViewModel.getMovieList()
    }

    /**
     * In this showRetryDialog method,
     * when pressing Retry button, load the data again else dimiss the dialog
     */
    private fun showRetryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("No Internet Connection, try again")
            .setCancelable(false)
            .setPositiveButton("Retry") { _, it ->
                loadData()
            }
            .setNegativeButton("Cancel") { _, it ->
                alert?.dismiss()
            }
        alert = builder.create()
        alert!!.show()
    }

    /**
     * In this observeLocalMovies method,
     * observing the local movie data changes, and submit it to the adapter
     */
    private fun observeLocalMovies() {
        movieViewModel.localMovieListLiveData?.observe(viewLifecycleOwner) {
            if (!(it.isNullOrEmpty())) {
                requireContext().setBooleanPref(IS_FIRST_TIME, IS_FIRST_TIME, false)
                movieAdapter.submitList(it) {
                    //scrolling back to the first position
                    binding.rcvMovieList.scrollToPosition(0)
                }
                binding.sflMovieList.stopShimmer()
                binding.sflMovieList.visibility = View.GONE
                binding.rcvMovieList.visibility = View.VISIBLE
            }
        }
    }

    /**
     * In this observeMovies method,
     * observing the movie data changes, and submit it to the adapter
     */
    private fun observeMovies() {
        movieViewModel.movieListLiveData.observe(viewLifecycleOwner) {
            val isFirstTime = requireContext().getBooleanPref(IS_FIRST_TIME, IS_FIRST_TIME, true)
            if (!(it.isNullOrEmpty())) {
                requireContext().setBooleanPref(IS_FIRST_TIME, IS_FIRST_TIME, false)
                movieAdapter.submitList(it) {
                    //scrolling back to the first position
                    binding.rcvMovieList.scrollToPosition(0)
                }
                binding.sflMovieList.stopShimmer()
                binding.sflMovieList.visibility = View.GONE
                binding.rcvMovieList.visibility = View.VISIBLE
            } else {
                if (isFirstTime)
                    showRetryDialog()
            }
        }
    }

    /**
     * In this navigateToDetail method,
     * this method will be call from Movie Adapter.
     * when calling this function, it will add movie entity as safe args and passed it to the detail fragment
     */
    fun navigateToDetail(movieEntity: MovieEntity) {
        val bundle = Bundle()
        bundle.putParcelable("movieEntity", movieEntity)
        findNavController().navigate(R.id.movieDetailFragment, bundle)
    }

    /**
     * In this handleSwipe method,
     * handling the swipe to refresh action
     */
    private fun handleSwipe() {
        binding.srlMovieList.setOnRefreshListener {
            binding.srlMovieList.isRefreshing = true
            loadData()
            binding.srlMovieList.isRefreshing = false
        }
    }

}