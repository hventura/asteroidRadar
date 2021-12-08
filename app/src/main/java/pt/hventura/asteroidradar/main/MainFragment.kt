package pt.hventura.asteroidradar.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import pt.hventura.asteroidradar.R
import pt.hventura.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainAsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        val activity = requireActivity()
        ViewModelProvider(activity, MainViewModel.Factory(activity.application))[MainViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        adapter = MainAsteroidAdapter(AsteroidListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })

        viewModel.picOfDay.observe(viewLifecycleOwner, {
            it?.let { picOfDay ->
                if (picOfDay.mediaType == "image") {
                    Picasso.get()
                        .load(picOfDay.url)
                        .placeholder(R.drawable.placeholder_picture_of_day)
                        .error(R.drawable.ic_no_image)
                        .into(binding.activityMainImageOfTheDay)
                    binding.activityMainImageOfTheDayTitle.text = picOfDay.title
                    binding.activityMainImageOfTheDayTitle.contentDescription = resources.getString(R.string.nasa_picture_of_day_content_description_format, picOfDay.title)
                } else {
                    binding.activityMainImageOfTheDay.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_no_image, context?.theme))
                    binding.activityMainImageOfTheDay.contentDescription = resources.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
                }
            }
        })

        viewModel.asteroids.observe(viewLifecycleOwner, { asteroidList ->
            if (asteroidList != null && asteroidList.isNotEmpty()) {
                Timber.i("asteroidList was changed")
                adapter.submitList(asteroidList)
                binding.statusLoadingWheel.visibility = View.GONE
                binding.swipeRefreshAsteroidList.visibility = View.VISIBLE
            } else {
                viewModel.refreshAsteroidList()
            }
        })

        binding.asteroidRecycler.adapter = adapter

        binding.swipeRefreshAsteroidList.setOnRefreshListener {
            binding.statusLoadingWheel.visibility = View.VISIBLE
            binding.swipeRefreshAsteroidList.visibility = View.GONE
            viewModel.refreshImageOfDay()
            viewModel.refreshAsteroidList()
            adapter.notifyDataSetChanged()
            binding.swipeRefreshAsteroidList.isRefreshing = false
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_asteroids -> {
                viewModel.getWeekAsteroids()
            }
            R.id.show_today_asteroids -> {
                viewModel.getTodayAsteroids()
            }
            R.id.show_saved_asteroids -> {
                viewModel.getSavedAsteroids()
            }
        }
        return true
    }

}