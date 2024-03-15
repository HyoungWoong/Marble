package com.ho8278.marble.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ho8278.core.error.stable
import com.ho8278.core.flowbinding.textChanges
import com.ho8278.marble.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()

    private val adapter by lazy {
        MarbleCharacterAdapter { viewModel.onSelectCard(it.characterId) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initProgressBar()
        initEditText()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.itemList
                .stable()
                .collect { adapter.submitList(it) }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (recyclerView.layoutManager !is GridLayoutManager) return

                    val lastVisiblePosition =
                        (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                    val isLast = (recyclerView.adapter?.itemCount?.minus(1)) == lastVisiblePosition

                    if (isLast) {
                        viewModel.loadMore()
                    }
                }
            }
        })
    }

    private fun initProgressBar() {
        lifecycleScope.launch {
            viewModel.isLoading
                .stable()
                .collect {
                    binding.progress.visibility = if (it) View.VISIBLE else View.GONE
                }
        }
    }

    private fun initEditText() {
        lifecycleScope.launch {
            binding.editText.textChanges()
                .debounce(300L)
                .stable()
                .collect { viewModel.onTextChanges(it) }
        }
    }

    companion object {
        const val TAG = "search"

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}