package com.eric.router_compiler;

/**
 *
 *
 *
 *   关于apt 技术的两篇好文章
 *
 *   https://cloud.tencent.com/developer/article/1717461（聊聊AbstractProcessor和Java编译流程）
 *
 *   （https://blog.csdn.net/xx326664162/article/details/68490059 你必须知道的APT、annotationProcessor、android-apt、Provided、自定义注解）
 *
 *   https://juejin.cn/post/6844904136454963207 （apt不在神秘）
 */

import com.eric.router_annotation.Router;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

/**
 * 如果要通过APT编译时技术生成java代码，需要经过以下的步骤
 * 1. 导入三个包
 * 1. auto-service 包
 * 2. 需要处理的自定义注解 router-annotation
 * 3. javapoet代码生成的工具
 * 2. 继承一个叫AbstractProcessor的类
 * 3. 实现process方法
 * 4. 配置自动化注解
 * 5. 注册apt处理的注解，要告诉当前程序需要检索的含有该注解的java类
 * 6. 指定java版本
 * 7. 指定模块
 */

//注册apt
@AutoService(Processor.class)
//注册apt处理的注解
@SupportedAnnotationTypes("com.eric.router_annotation.Router")
//指定apt支持的java版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//key-value的形式接受传递的参数
@SupportedOptions("moduleName")
public class RouterProcessor extends AbstractProcessor {

    private static final String TAG = "RouterProcessor";

    private Filer filer;
    private String moduleName;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        //apk文件由多个dex文件组成，dex文件由许多class文件组成，这个就是拿到了我们的标有注解的处理类
        filer = processingEnv.getFiler();

        Map<String, String> options = processingEnv.getOptions();

        moduleName = options.get("moduleName");

        elements = processingEnv.getElementUtils();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //实现自动生成java文件


        /**
         * package com.eric.routers
         *
         * import android.app.Activity;
         *
         * import com.eric.rxjava.FoodActivity
         *
         * import java.util.Map;
         *
         *
         *
         *
         * public class FoodRegister implements IRouteLoad {
         *
         *  @Overide
         *  public void onLoad(Map<String,Class<? extends Activity> routes)
         *      //找到foodActivity(被自定义annoation注释的),就注册到map集合中
         *      routes.put("food/main",FoodActivity.class)
         * }
         *
         * }
         *
         *
         *
         */
        //有类使用自定义的注解了
        if (!annotations.isEmpty()) {
            Set<? extends Element> routerElements = roundEnv.getElementsAnnotatedWith(Router.class);

            generatedClass(routerElements);
        }


        return false;
    }

    private void generatedClass(Set<? extends Element> routerElements) {


        // 1.导包
        StringBuilder sb = new StringBuilder();
        sb.append("package com.eric.routers.apt;\n");
        sb.append("import android.app.Activity;\n");
        sb.append("import java.util.Map;\n");
        sb.append("import com.eric.routers.IRouteLoad;\n");

        // 动态导入类名字
        // 需要生成类名 需要"动态导包"，这是"类"的元素
        for (Element element : routerElements) {
            TypeElement typeElement = (TypeElement) element;
            sb.append("import ");
            sb.append(typeElement.getQualifiedName());
            sb.append(";\n");
        }

        // 2. 生成类文件的字符串
        sb.append("public class ");
        sb.append(moduleName);
        sb.append("_router implements IRouteLoad {\n");
        sb.append("@Override\n");
        sb.append("public void onLoad(Map<String, Class<? extends Activity>> routes) {\n");

        for (Element routerElement : routerElements) {
            Router route = routerElement.getAnnotation(Router.class);
            sb.append("routes.put(\"");
            sb.append(route.value());
            sb.append("\",");
            sb.append(routerElement.getSimpleName());
            sb.append(".class);\n");
        }

        sb.append("}\n");
        sb.append("}");


        // 3.创建java文件
        try {
            // 生成java文件 com.eric.routers. 这个后面的"."不要忘记加，反正生成的文件目录会不对
            JavaFileObject sourceFile = filer.createSourceFile("com.eric.routers.apt." + moduleName + "_router");

            //输出字符串
            OutputStream outputStream = sourceFile.openOutputStream();
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}