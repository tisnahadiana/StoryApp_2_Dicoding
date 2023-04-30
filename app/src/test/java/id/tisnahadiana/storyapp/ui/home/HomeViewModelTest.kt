package id.tisnahadiana.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import id.tisnahadiana.storyapp.data.local.room.StoryEntity
import id.tisnahadiana.storyapp.data.repository.StoryRepository
import id.tisnahadiana.storyapp.data.repository.UserRepository
import id.tisnahadiana.storyapp.ui.adapter.StoryAdapter
import id.tisnahadiana.storyapp.utils.CoroutineTestUtil
import id.tisnahadiana.storyapp.utils.DataDummy
import id.tisnahadiana.storyapp.utils.PagingDataSourceTest
import id.tisnahadiana.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestUtil = CoroutineTestUtil()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(storyRepository, userRepository)
    }

    private val dummyToken = DataDummy.DUMMY_TOKEN
    private val dummyStories = DataDummy.generateDummyStories()
    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    @Test
    fun `Get all stories, success`() = runTest {
        val data = PagingDataSourceTest.snapshot(dummyStories)
        val stories = MutableLiveData<PagingData<StoryEntity>>()
        stories.value = data
        `when`(storyRepository.getStory(dummyToken)).thenReturn(stories)

        val actualStories = homeViewModel.getStory(dummyToken).getOrAwaitValue()
        Mockito.verify(storyRepository).getStory(dummyToken)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = coroutineTestUtil.testDispatcher,
            workerDispatcher = coroutineTestUtil.testDispatcher
        )
        differ.submitData(actualStories)
        advanceUntilIdle()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories.first().id, differ.snapshot().first()?.id)
        //saya asumsikan data pertama yang dikembalikan ini adalah id pada DataDummy
    }

    @Test
    fun `Get all stories, no data is returned`() = runTest {
        val emptyList = emptyList<StoryEntity>()
        val emptyData = PagingDataSourceTest.snapshot(emptyList)

        val stories = MutableLiveData<PagingData<StoryEntity>>()
        stories.value = emptyData
        `when`(storyRepository.getStory(dummyToken)).thenReturn(stories)

        val actualStories = homeViewModel.getStory(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStory(dummyToken)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = coroutineTestUtil.testDispatcher,
            workerDispatcher = coroutineTestUtil.testDispatcher
        )
        differ.submitData(actualStories)
        advanceUntilIdle()

        Assert.assertEquals(0, differ.snapshot().size)
    }


}