package com.eric.androidstudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eric.androidstudy.viewmodel.CalculateViewModel
import kotlinx.coroutines.launch

/**

 * @Author: chen

 * @datetime: 2024/7/28

 * @desc:

 */
class CalculateFragment : Fragment() {

    val vm by viewModels<CalculateViewModel>()

    private var rootView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =
            LayoutInflater.from(context).inflate(R.layout.layout_calculate_fragment, null, false)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tryCollect()
        initAction()
    }

    private fun tryCollect() {

        lifecycleScope.launch {
            vm.state.collect { value ->
                rootView?.findViewById<TextView>(R.id.tvCount)?.text = value.count.toString()
            }
        }
    }

    private fun initAction() {
        rootView?.findViewById<Button>(R.id.btnIncrement)?.setOnClickListener {
            vm.increase()
        }

        rootView?.findViewById<Button>(R.id.btnDecrement)?.setOnClickListener {
            vm.decrease()
        }
    }
}