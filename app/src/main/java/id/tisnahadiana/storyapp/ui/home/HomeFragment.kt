package id.tisnahadiana.storyapp.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import id.tisnahadiana.storyapp.ui.camera.CameraActivity
import id.tisnahadiana.storyapp.ui.login.LoginActivity

@AndroidEntryPoint
@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private var token: String = "Token Tidak Ada"
    private var lastVisiblePosition: Int = 0

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

        homeViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) { token ->
            this.token = token ?: "Token Tidak Ada"
            binding.tvToken.text = this.token
        }

        setRecyclerView()
        swipeRefresh()
        getStories()

        binding.tvToken.text = token
        binding.buttonAdd.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkIfSessionValid()
        if (::recyclerView.isInitialized && lastVisiblePosition != RecyclerView.NO_POSITION) {
            recyclerView.scrollToPosition(lastVisiblePosition)
        }
    }

    override fun onPause() {
        super.onPause()
        if (::recyclerView.isInitialized) {
            lastVisiblePosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }
    }

    private fun checkIfSessionValid() {
        homeViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) { token ->
            if (token.isNullOrEmpty()) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun swipeRefresh() {
        binding?.swipe?.setOnRefreshListener { getStories() }
    }

    private fun getStories() {
        homeViewModel.getStory(token).observe(viewLifecycleOwner) {
            updateAdapter(it)
        }
    }

    private fun updateAdapter(stories: PagingData<StoryEntity>) {
        storyAdapter.submitData(lifecycle, stories)
    }

    private fun setRecyclerView() {
        storyAdapter = StoryAdapter()
        storyAdapter.addLoadStateListener {
            if ((it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && storyAdapter.itemCount < 1) || it.source.refresh is LoadState.Error) showErrorOccurred(
                true
            )
            else showErrorOccurred(false)

            binding?.swipe?.isRefreshing = it.source.refresh is LoadState.Loading
        }

        try {
            recyclerView = binding?.rvStories!!
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = storyAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        storyAdapter.retry()
                    }
                )
                addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                    if (bottom < oldBottom) {
                        lastVisiblePosition =
                            (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    }
                }
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

    private fun showMessage(context: Context, message: String) {
        context.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun View.setVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_needed),
                    Toast.LENGTH_SHORT
                )
                    .show()
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}
