package id.tisnahadiana.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.R
import id.tisnahadiana.storyapp.data.local.room.StoryEntity
import id.tisnahadiana.storyapp.databinding.FragmentHomeBinding
import id.tisnahadiana.storyapp.ui.adapter.LoadingStateAdapter
import id.tisnahadiana.storyapp.ui.adapter.StoryAdapter
import id.tisnahadiana.storyapp.ui.detail.DetailActivity
import id.tisnahadiana.storyapp.ui.detail.DetailActivity.Companion.EXTRA_DETAIL

@AndroidEntryPoint
@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private var token: String = "Token Tidak Ada"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) { token ->
            this.token = token ?: "Token Tidak Ada"
            binding.tvToken.text = this.token
        }

        setRecyclerView()
        getStories()
        swipeRefresh()

        binding.tvToken.text = token
        binding.buttonAdd.setOnClickListener {

        }
    }

    private fun swipeRefresh() {
        binding?.swipe?.setOnRefreshListener { getStories() }
    }
    private fun getStories() {
        viewModel.getStory(token).observe(viewLifecycleOwner) {
            updateAdapter(it)
        }
    }

    private fun updateAdapter(stories: PagingData<StoryEntity>) {
        storyAdapter.submitData(lifecycle, stories)
        recyclerView.smoothScrollToPosition(0)
    }

    private fun setRecyclerView() {
        storyAdapter = StoryAdapter()
        storyAdapter.addLoadStateListener {
            if ((it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && storyAdapter.itemCount < 1) || it.source.refresh is LoadState.Error) showErrorOccurred(true)
            else showErrorOccurred(false)

            binding?.swipe?.isRefreshing = it.source.refresh is LoadState.Loading
        }
        storyAdapter.setOnStartActivityCallback(object : StoryAdapter.OnStartActivityCallback {
            override fun onStartActivityCallback(story: StoryEntity, bundle: Bundle?) {
                Intent(requireContext(), DetailActivity::class.java).also {
                    it.putExtra(EXTRA_DETAIL, story)
                    startActivity(it, bundle)
                }
            }
        })

        try {
            recyclerView = binding?.rvStories!!
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = storyAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        storyAdapter.retry()
                    }
                )
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorOccurred(isError: Boolean) {
        binding.apply {
            tvErrorHome.setVisible(isError)
            rvStories.setVisible(!isError)
        }

        if (isError) showMessage(requireContext(), getString(R.string.error_occurred))
    }

    private fun showMessage(context: Context ,message: String) {
        context.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun View.setVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

}