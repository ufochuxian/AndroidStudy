package com.eric.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eric.androidstudy.databinding.FragmentSampleBinding

class SampleFragment : Fragment() {

    private var _binding: FragmentSampleBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(title: String): SampleFragment {
            val fragment = SampleFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = arguments?.getString("title") ?: "No Title"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
