package com.eric.theme

class PhotoEditFragment : BaseMviFragment<FragmentPhotoEditBinding, PhotoEditIntent, PhotoEditState, PhotoEditViewModel>() {

    companion object {
        fun newInstance(uri: String): PhotoEditFragment {
            return PhotoEditFragment().apply {
                arguments = bundleOf("uri" to uri)
            }
        }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPhotoEditBinding.inflate(inflater, container, false)

    override fun setupViews() {
        val uri = Uri.parse(requireArguments().getString("uri"))
        binding.photoView.setImageURI(uri)

        binding.btnNext.setOnClickListener {
            viewModel.sendIntent(PhotoEditIntent.OnNextClicked(uri))
        }
    }

    override fun render(state: PhotoEditState) {
        if (state.navigateToCustomize && state.editedUri != null) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ThemeCustomizeFragment.newInstance(state.editedUri.toString()))
                .addToBackStack(null)
                .commit()
        }
    }
}
