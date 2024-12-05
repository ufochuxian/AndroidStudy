package com.eric.androidstudy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.eric.task.AppLockPermissionTask
import com.eric.task.BroadCastViewModel
import com.eric.task.BroadcastTask
import com.eric.task.CameraPermissionTask
import com.eric.task.GestureTask
import com.eric.task.PatternPasswordTask
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

    private var permissionMgr: PermissionManager<LifecycleOwner>? = null

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
            binding.rippleView.setRippleBackground(
                it,
                R.color.colorAccent,
                rippleColor,
                cornerRadius
            )
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
            permissionMgr?.requestLocationPermissionOnBackground(object : PermissionManager.PermissionCallback {
                override fun onPermissionGranted(result: ActivityResult?) {
                    Log.d(ERIC_TAG,"后台定位权限已经获取成功")
                }

                override fun onPermissionDenied(result: ActivityResult?) {
                    Log.d(ERIC_TAG,"后台定位权限已经获取失败")
                }
            })

        }


        binding.testTask.setOnClickListener {
            lifecycleScope.launch {
                val tasks = listOf(
                    AppLockPermissionTask(requireContext(),null,permissionMgr),
                    CameraPermissionTask(requireContext(), null, permissionMgr),
                    //一组上锁逻辑
                    StoragePermissionTask(requireContext(), null, permissionMgr),
                    PatternPasswordTask(context, null),
                    gestureTask,
                    BroadcastTask(broadCastViewModel),
                )
                TasksChainManager<String?>().executeTasksSequentially(tasks) //串行执行任务
//                ParallelTasksManager().executeTasks(tasks) //并发执行任务
            }
        }


        val anim = CustomAnim()
        anim.setRotateY(10f)
        binding.threeDView.text = "测试3d效果"
        binding.threeDView.startAnimation(anim)
    }

    private val gestureTask: GestureTask by lazy {
        GestureTask(context, gestureLauncher)
    }

    private val gestureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        gestureTask.onPatternResult(result.resultCode)
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
        Log.d(ERIC_TAG, "FirstFragment onResume")
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