package com.eric.androidstudy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eric.androidstudy.databinding.FragmentFirstBinding
import com.eric.animation.CustomAnim
import com.eric.base.ext.ERIC_TAG
import com.eric.base.mgr.PermissionManager
import com.eric.base.setRippleBackground
import com.eric.lifecycle.TestLifeCycleActivity
import com.eric.routers.TgmRouter
import com.eric.task.BroadCastViewModel
import com.eric.task.BroadcastTask
import com.eric.task.GestureTask
import com.eric.task.ITask
import com.eric.task.PasswordTask
import com.eric.task.PermissionTask
import com.eric.task.StoragePermissionTask
import com.eric.task.TasksChainManager
import com.eric.task.copyFile
import kotlinx.coroutines.launch
import java.io.File


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val broadCastViewModel by viewModels<BroadCastViewModel>()

    companion object {
        const val SECOND_ACTIVITY = "secondActivity"
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var permissionMgr : PermissionManager<LifecycleOwner>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        activity?.let {
            permissionMgr = PermissionManager(it)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.threeDView.setOnClickListener {

        }



        val cornerRadius = 12f // 你可以根据需要动态计算这个值
        activity?.let {
//            val rippleColor = R.attr.colorControlHighlight
            val rippleColor = R.color.color_ffbdbdbd
            binding.rippleView.setRippleBackground(it, R.color.colorAccent, rippleColor, cornerRadius)
        }


        binding.jumpBtn.setOnClickListener {
            TgmRouter.getInstance().startActivity("/food/main")
        }

        binding.lifecycleBtn.setOnClickListener {
            var intent = Intent(activity, TestLifeCycleActivity::class.java);
            activity?.let {
                it.startActivity(intent)
            }
        }

        binding.takePhoto.setOnClickListener {
//            activity?.let {
//                val capture = Camera2PhotoCapture(it)
//                capture.startCameraAndTakePhoto(onImageSaved = { imgFile ->
//                    Log.d(TAG, "${imgFile.name} is capture & saved success,path:${imgFile.path}")
//                })
//            }
        }

        binding.copyFileByOkio.setOnClickListener {
//            copyFileByOkio()
        }

        binding.testTask.setOnClickListener {
            lifecycleScope.launch {
                val tasks = listOf(
                    PermissionTask(requireContext(),null, "PermissionTask",permissionMgr),
                    StoragePermissionTask(requireContext(),null, "PermissionTask",permissionMgr),
                    PasswordTask(null, "PasswordTask"),
                    BroadcastTask(broadCastViewModel, "BroadcastTask"),
                    GestureTask(null, "GestureTask")
                )
                TasksChainManager<Boolean>().executeTasks(tasks) //串行执行任务
//                ParallelTasksManager().executeTasks(tasks) //并发执行任务
            }
        }

        val anim = CustomAnim()
        anim.setRotateY(10f)
        binding.threeDView.text = "测试3d效果"
        binding.threeDView.startAnimation(anim)
    }

    private fun initObserver() {
        broadCastViewModel.resultData.observe(viewLifecycleOwner) {
            binding.testTask.text = it?.msg
        }
    }

    private fun copyFileByOkio() {
        // 定义源文件和目标文件
        val sourceFile = File("MainActivity.kt")
        val destinationFile = File("MainActivity_Copy.kt")
        // 复制文件
        context?.let {
            copyFile(sourceFile, it)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(ERIC_TAG,"FirstFragment onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun getThemeColor(context: Context, attr: Int): Int {
    val typedValue = TypedValue()
    val theme = context.theme
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}