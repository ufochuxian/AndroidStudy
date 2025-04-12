package com.eric.theme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.FragmentPhotoPickerBinding

class PhotoPickerFragment(override val viewModel: PhotoPickerViewModel) : BaseMviFragment<FragmentPhotoPickerBinding, PhotoPickerIntent, PhotoPickerState, PhotoPickerViewModel>() {
    private val photoPicker = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri -> viewModel.sendIntent(PhotoPickerIntent.PhotoSelected(uri)) }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPhotoPickerBinding.inflate(inflater, container, false)

    override fun setupViews() {
        binding.btnSelectPhoto.setOnClickListener {
            photoPicker.launch("image/*")
        }
    }

    override fun render(state: PhotoPickerState) {
        if (state.navigateToEditor && state.selectedUri != null) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, PhotoEditFragment.newInstance(state.selectedUri.toString()))
                .addToBackStack(null)
                .commit()
        }
    }
}
