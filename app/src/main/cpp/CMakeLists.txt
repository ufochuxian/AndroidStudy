cmake_minimum_required(VERSION 3.4.1)

# 启用 C++17 标准
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# 设置项目名称
project("frosted-glass")

# 添加 C++ 共享库
add_library(frosted-glass SHARED frosted_glass.cpp)

# 查找日志库
find_library(log-lib log)

# 确保 `libc++_shared.so` 被正确链接
add_library(c++_shared SHARED IMPORTED)
set_target_properties(c++_shared PROPERTIES IMPORTED_LOCATION
        "${ANDROID_NDK}/sources/cxx-stl/llvm-libc++/libs/${ANDROID_ABI}/libc++_shared.so")
# 连接目标库
target_link_libraries(frosted-glass
        ${log-lib}         # 连接 Android log 库
        jnigraphics        # 连接 Android 图形库
        c++_shared        # 连接 C++ 共享库，确保 libc++_shared.so
)
