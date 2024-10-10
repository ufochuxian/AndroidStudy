cmake_minimum_required(VERSION 3.4.1)
add_library(frosted-glass SHARED
        frosted_glass.cpp)
find_library(log-lib
        log)
target_link_libraries(frosted-glass
        ${log-lib}
        jnigraphics)