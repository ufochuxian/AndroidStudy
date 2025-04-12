package com.eric.theme

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.os.bundleOf
import com.eric.androidstudy.databinding.FragmentThemeCustomizeBinding

class ThemeCustomizeFragment(override val viewModel: ThemeCustomizeViewModel) : BaseMviFragment<FragmentThemeCustomizeBinding, ThemeCustomizeIntent, ThemeCustomizeState, ThemeCustomizeViewModel>() {

    companion object {
        fun newInstance(uri: String): ThemeCustomizeFragment {
            return ThemeCustomizeFragment().apply {
                arguments = bundleOf("uri" to uri)
            }
        }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentThemeCustomizeBinding.inflate(inflater, container, false)

    override fun setupViews() {
        val uri = Uri.parse(requireArguments().getString("uri"))
        binding.keyboardPreview.setImageURI(uri)

        binding.seekBrightness.setOnSeekBarChangeListener(seekListener { value ->
            viewModel.sendIntent(ThemeCustomizeIntent.UpdateBrightness(value / 100f))
        })

        binding.seekOpacity.setOnSeekBarChangeListener(seekListener { value ->
            viewModel.sendIntent(ThemeCustomizeIntent.UpdateOpacity(value / 100f))
        })

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.sendIntent(ThemeCustomizeIntent.SwitchTheme(isChecked))
        }

        binding.btnApply.setOnClickListener {
            viewModel.sendIntent(ThemeCustomizeIntent.Apply)
        }
    }

    override fun render(state: ThemeCustomizeState) {
        binding.keyboardPreview.alpha = state.brightness
        // 可扩展：绘制边框透明度 + 深浅模式样式等
    }

    private fun seekListener(onChanged: (Int) -> Unit) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) = onChanged(progress)
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        }
}
