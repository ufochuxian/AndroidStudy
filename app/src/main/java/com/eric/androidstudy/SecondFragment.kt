package com.eric.androidstudy

import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.eric.androidstudy.databinding.FragmentSecondBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        shapeByCode()

        setGradientDrawable()


        // 模拟数据
        val items = listOf("选项一", "选项二", "选项三")

        // 使用 SpinnerDelegate 代理类来初始化 Spinner
        SpinnerDelegate(
            context = requireContext(),
            spinner = binding.mySpinner,
            triggerView = binding.spinnerBtn,
            items = items
        ) { selectedItem ->
            // 当用户选择了某个选项时的回调处理
            Toast.makeText(context, "你选择了: $selectedItem", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }

    private fun setGradientDrawable() {
        // 创建一个 GradientDrawable
        val gradientDrawable = GradientDrawable()
        // 设置形状为矩形
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        // 设置背景颜色，例如蓝色
        gradientDrawable.setColor(Color.BLUE)
        // 将 GradientDrawable 设置为某个视图的背景
        binding.gradientDrawableBtn.background = gradientDrawable
    }

    private fun shapeByCode() {
        val roundIn = dp2px(30f).toFloat()
        val externalRounds =
            floatArrayOf(roundIn, roundIn, roundIn, roundIn, roundIn, roundIn, roundIn, roundIn)
        val outlineRound =
            floatArrayOf(roundIn, roundIn, roundIn, roundIn, roundIn, roundIn, roundIn, roundIn)
        val reactFValue = dp2px(2f).toFloat()
        val rectF = RectF(reactFValue, reactFValue, reactFValue, reactFValue)
        val drawable1 = ShapeDrawable(RoundRectShape(externalRounds, rectF, outlineRound))
        drawable1.paint.color = Color.RED
        binding.shapeCode.background = drawable1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.buttonCalculateFragment.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_CalculateFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}