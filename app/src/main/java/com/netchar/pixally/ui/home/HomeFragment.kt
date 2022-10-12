package com.netchar.pixally.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.netchar.pixally.R
import com.netchar.pixally.databinding.FragmentHomeBinding
import com.netchar.pixally.domain.usecase.PhotosRequest
import com.netchar.pixally.ui.home.adapter.ImageRecyclerItem
import com.netchar.pixally.ui.home.adapter.UiImageItem
import com.netchar.pixally.ui.abstractions.adapter.GenericRecyclerAdapter
import com.netchar.pixally.ui.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val adapter by lazy {
        GenericRecyclerAdapter.create(ImageRecyclerItem(::onImageItemClicked))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupViews()
        observe()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val imageType = when (menuItem.itemId) {
                    R.id.menu_item_home_all -> PhotosRequest.ImageType.ALL
                    R.id.menu_item_home_photo -> PhotosRequest.ImageType.PHOTO
                    R.id.menu_item_home_illustration -> PhotosRequest.ImageType.ILLUSTRATION
                    R.id.menu_item_home_vector -> PhotosRequest.ImageType.VECTOR
                    else -> PhotosRequest.ImageType.ALL
                }

                viewModel.filterPhotosBy(imageType)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupViews() {
        binding.homeRecycler.adapter = adapter
        binding.homeLayoutRefresh.setOnRefreshListener {
            viewModel.refreshImages()
        }
    }

    private fun observe() {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::updateUi)
            .launchIn(lifecycleScope)
    }

    private fun updateUi(state: HomeState) {
        adapter.submitList(state.photos)
        binding.homeLayoutRefresh.isRefreshing = state.isLoading
        state.errorMessage?.let { errorMessage ->
            if (errorMessage is HomeState.ErrorMessage.Toast) {
                context.showToast(errorMessage.message)
            }
        }
    }

    private fun onImageItemClicked(uiImageItem: UiImageItem) {
        context.showToast(uiImageItem.imageUrl)
    }
}