package com.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GenerateModulePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.tasks.register("generateModule") {
            doLast {
                // 读取 moduleName 并转换多级目录为合法 package
                def moduleName = project.hasProperty("moduleName")
                        ? project.property("moduleName").toString()
                        : "DefaultModule"

                println("moduleName: ${moduleName}")

                // 获取 namespace（如果项目有 Android 插件，则自动读取）
                def namespace = project.hasProperty("namespace")
                        ? project.property("namespace").toString()
                        : project.android.namespace ?: "com.example"
                println("namespace: ${namespace}")

                // 处理 moduleName 支持多级目录（转换 / 为 .）
                // 处理 moduleName 生成 packagePath（支持多级目录结构）
                def packageSuffix = moduleName.split('/').collect { it.toLowerCase() }.join('.')
                println("packageSuffix: ${packageSuffix}")
                def packagePath = "${namespace}.${packageSuffix}"
                println("packagePath: ${packagePath}")

                // 获取 moduleName 最后一级名称（用于 ViewBinding、类名）
                def moduleSimpleName = moduleName.split('/').last()

                println("moduleSimpleName: ${moduleSimpleName}")

                // 获取输出路径
                def outputPath = project.hasProperty("outputPath")
                        ? project.property("outputPath").toString()
                        : "src/main/java"
                println("outputPath: ${outputPath}")

                // 处理 ViewBinding 类名
                def activityBindingClass = "Activity" + moduleSimpleName.capitalize() + "Binding"
                def fragmentBindingClass = "Fragment" + moduleSimpleName.capitalize() + "Binding"

                // 生成 ViewModel 类名
                def activityViewModelClass = moduleSimpleName.capitalize() + "ActivityViewModel"
                def fragmentViewModelClass = moduleSimpleName.capitalize() + "FragmentViewModel"

                // 生成文件路径（转换 package 为目录结构）
                def packageDir = packagePath.replace('.', '/')
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
                def activityFile = new File(baseDir, "${moduleSimpleName}Activity.kt")
                activityFile.text = """
            package ${packagePath}

            import android.os.Bundle
            import ${namespace}.R
            import ${namespace}.databinding.${activityBindingClass}
            import com.transsion.architecturemodule.base.activity.BaseVMActivity

            class ${moduleSimpleName}Activity : BaseVMActivity<${activityBindingClass}, ${activityViewModelClass}>() {
                override fun initData() {
                    // 初始化数据
                }

                override fun initView(savedInstanceState: Bundle?) {
                    // 显示 Fragment
                    showPage(R.id.container, ${moduleSimpleName}Fragment.newInstance(), ${moduleSimpleName}Fragment.TAG, true)
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
                def fragmentFile = new File(baseDir, "${moduleSimpleName}Fragment.kt")
                fragmentFile.text = """
            package ${packagePath}

            import android.os.Bundle
            import android.view.LayoutInflater
            import android.view.View
            import android.view.ViewGroup
            import ${namespace}.databinding.${fragmentBindingClass}
            import com.transsion.architecturemodule.base.fragment.BaseVMFragment

            class ${moduleSimpleName}Fragment : BaseVMFragment<${fragmentBindingClass}, ${fragmentViewModelClass}>() {

                companion object {
                    const val TAG = "${moduleSimpleName}Fragment"
                    fun newInstance(args: Bundle? = null): ${moduleSimpleName}Fragment {
                        return ${moduleSimpleName}Fragment().apply {
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

                // 生成 Activity 的 XML 布局文件
                def layoutFile = new File(resDir, "activity_${moduleSimpleName.toLowerCase()}.xml")
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

                // 生成 Fragment 的 XML 布局文件
                def fragmentLayoutFile = new File(resDir, "fragment_${moduleSimpleName.toLowerCase()}.xml")
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