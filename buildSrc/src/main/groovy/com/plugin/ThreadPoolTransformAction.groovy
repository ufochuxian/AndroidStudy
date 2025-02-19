import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

import java.nio.file.Files
import java.nio.file.StandardCopyOption

abstract class ThreadPoolTransformAction implements WorkAction<ThreadPoolTransformParameters> {
    @Override
    void execute() {
        File input = parameters.inputFile.get().asFile
        File output = parameters.outputFile.get().asFile

        // 确保输出目录存在
        output.parentFile.mkdirs()

        // ✅ 使用 Java NIO 进行文件拷贝（强制覆盖）
        Files.copy(input.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}


interface ThreadPoolTransformParameters extends WorkParameters {
    RegularFileProperty getInputFile()
    RegularFileProperty getOutputFile()
}
