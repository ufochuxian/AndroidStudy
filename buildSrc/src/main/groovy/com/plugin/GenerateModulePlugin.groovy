package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GenerateModulePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.tasks.register("generateModule") {
            doLast {
                // 读取参数
                def moduleName = project.hasProperty("moduleName") ? project.property("moduleName").toString() : "DefaultModule"
                def namespace = project.hasProperty("namespace")
                        ? project.property("namespace").toString()
                        : project.android.namespace ?: "com.example"
                println("namespace:${namespace}")
                def packagePath = project.hasProperty("packagePath")
                        ? project.property("packagePath").toString()
                        : "${namespace}.${moduleName.toLowerCase()}"
                def outputPath = project.hasProperty("outputPath") ? project.property("outputPath").toString() : "src/main/java"
                println("outputPath:${outputPath}")

                // 处理 ViewBinding 类名，符合 Android 生成规则
                def activityBindingClass = "Activity" + moduleName.toLowerCase().split('_').collect { it.capitalize() }.join('') + "Binding"
                def fragmentBindingClass = "Fragment" + moduleName.toLowerCase().split('_').collect { it.capitalize() }.join('') + "Binding"

                // 生成各自的 ViewModel 类名
                def activityViewModelClass = moduleName + "ActivityViewModel"
                def fragmentViewModelClass = moduleName + "FragmentViewModel"

                // 生成文件路径
                def packageDir = packagePath.replace('.', '/')
                def namespaceDir = namespace.replace('.', '/')
//                def baseDir = project.file("${project.projectDir}/${outputPath}/${packageDir}")
//                def resDir = project.file("${project.projectDir}/src/main/res/layout")
                def baseDir = project.file("${outputPath}/${packageDir}")
                def resDir = project.file("src/main/res/layout")

                // 确保目录存在
                baseDir.mkdirs()
                resDir.mkdirs()

                // 生成 Activity ViewModel 文件
                def activityViewModelFile = new File(baseDir, "${activityViewModelClass}.kt")
                activityViewModelFile.text = """
            package ${packagePath}

            import com.transsion.architecturemodule.base.viewmodel.BaseViewModel

            class ${activityViewModelClass} : BaseViewModel() {
              
            }
        """.stripIndent()

                // 生成 Fragment ViewModel 文件
                def fragmentViewModelFile = new File(baseDir, "${fragmentViewModelClass}.kt")
                fragmentViewModelFile.text = """
            package ${packagePath}

            import com.transsion.architecturemodule.base.viewmodel.BaseViewModel

            class ${fragmentViewModelClass} : BaseViewModel() {

            }
        """.stripIndent()

                // 生成 Activity 类文件
                def activityFile = new File(baseDir, "${moduleName}Activity.kt")
                activityFile.text = """
            package ${packagePath}

            import android.os.Bundle
            import ${namespace}.R
            import ${namespace}.databinding.${activityBindingClass}
            import com.transsion.architecturemodule.base.activity.BaseVMActivity

            class ${moduleName}Activity : BaseVMActivity<${activityBindingClass}, ${activityViewModelClass}>() {
                override fun initData() {
                    // 初始化数据
                }

                override fun initView(savedInstanceState: Bundle?) {
                    // 显示 Fragment
                    showPage(R.id.container, ${moduleName}Fragment.newInstance(), ${moduleName}Fragment.TAG, true)
                }

                override fun initObserve() {
                    // 观察 LiveData 变化
                }

                override fun initAction() {
                    // 初始化点击事件等
                }

                override fun viewModelClass(): Class<${activityViewModelClass}> = ${activityViewModelClass}::class.java

                override fun getViewBinding(): ${activityBindingClass} = ${activityBindingClass}.inflate(layoutInflater)
            }
        """.stripIndent()

                // 生成 Fragment 类文件
                def fragmentFile = new File(baseDir, "${moduleName}Fragment.kt")
                fragmentFile.text = """
            package ${packagePath}

            import android.os.Bundle
            import android.view.LayoutInflater
            import android.view.View
            import android.view.ViewGroup
            import ${namespace}.databinding.${fragmentBindingClass}
            import com.transsion.architecturemodule.base.fragment.BaseVMFragment

            class ${moduleName}Fragment : BaseVMFragment<${fragmentBindingClass}, ${fragmentViewModelClass}>() {

                companion object {
                    const val TAG = "${moduleName}Fragment"
                    fun newInstance(args: Bundle? = null): ${moduleName}Fragment {
                        return ${moduleName}Fragment().apply {
                            arguments = args
                        }
                    }
                }

                override fun initData() {
                    // 初始化数据
                }

                override fun initView(view: View, savedInstanceState: Bundle?) {
                    // 初始化 UI
                }
                
                override fun initAction() {
                    // 处理点击事件等
                }

                override fun initObserve() {
                    // 观察 LiveData 变化
                }

                override fun viewModelClass(): Class<${fragmentViewModelClass}> = ${fragmentViewModelClass}::class.java

                override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): ${fragmentBindingClass} {
                    return ${fragmentBindingClass}.inflate(layoutInflater, mParentContainer, false)
                }
            }
        """.stripIndent()

                // 生成 Activity 的 XML 布局文件，去除多余空白
                def layoutFile = new File(resDir, "activity_${moduleName.toLowerCase()}.xml")
                if (!layoutFile.exists()) {
                    layoutFile.text = """<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
    """.stripIndent().trim()
                }

// 生成 Fragment 的 XML 布局文件，去除多余空白
                def fragmentLayoutFile = new File(resDir, "fragment_${moduleName.toLowerCase()}.xml")
                if (!fragmentLayoutFile.exists()) {
                    fragmentLayoutFile.text = """<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
    """.stripIndent().trim()
                }


                println """
        ========================================
        Module ${moduleName} generated successfully!
        Generated ViewBinding Classes:
        - Activity Binding: ${activityBindingClass}
        - Fragment Binding: ${fragmentBindingClass}
        
        Generated ViewModel Classes:
        - Activity ViewModel: ${activityViewModelClass}
        - Fragment ViewModel: ${fragmentViewModelClass}

        Files Created:
        - Activity: ${activityFile.absolutePath}
        - Fragment: ${fragmentFile.absolutePath}
        - ViewModels:
            - ${activityViewModelFile.absolutePath}
            - ${fragmentViewModelFile.absolutePath}
        - Layout Files:
            - ${layoutFile.absolutePath}
            - ${fragmentLayoutFile.absolutePath}
        ========================================
        """
            }
        }
    }
}
