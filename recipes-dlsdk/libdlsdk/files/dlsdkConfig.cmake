if(NOT TARGET dlsdk::dlsdk)
    find_library(_dlsdk_lib NAMES dlsdk REQUIRED)
    find_path(_dlsdk_inc dlsdk.h PATH_SUFFIXES dlsdk REQUIRED)
    add_library(dlsdk::dlsdk SHARED IMPORTED)

    set_target_properties(dlsdk::dlsdk PROPERTIES
        IMPORTED_LOCATION "${_dlsdk_lib}"
        INTERFACE_INCLUDE_DIRECTORIES "${_dlsdk_inc}"
    )

    unset(_dlsdk_lib CACHE)
    unset(_dlsdk_inc CACHE)
endif()
set(dlsdk_FOUND TRUE)
