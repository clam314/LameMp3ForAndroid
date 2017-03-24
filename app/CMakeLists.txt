
cmake_minimum_required(VERSION 3.4.1)


set(SRC_DIR src/main/cpp/lamemp3)

add_library(
             native-lib
             SHARED
             src/main/cpp/native-lib.cpp )

include_directories(src/main/cpp/lamemp3)

aux_source_directory(src/main/cpp/lamemp3 SRC_LIST)
add_library(lamemp3 SHARED ${SRC_LIST})
#add_library(lame-mp3
#            SHARED
#            ${SRC_DIR}/bitstream.c
#            ${SRC_DIR}/encoder.c
#            ${SRC_DIR}/fft.c
#            ${SRC_DIR}/gain_analysis.c
#            ${SRC_DIR}/id3tag.c
#            ${SRC_DIR}/lame.c
#            ${SRC_DIR}/mpglib_interface.c
#            ${SRC_DIR}/newmdct.c
#            ${SRC_DIR}/presets.c
#            ${SRC_DIR}/psymodel.c
#            ${SRC_DIR}/quantize.c
#            ${SRC_DIR}/quantize_pvt.c
#            ${SRC_DIR}/reservoir.c
#            ${SRC_DIR}/set_get.c
#            ${SRC_DIR}/tables.c
#            ${SRC_DIR}/takehiro.c
#            ${SRC_DIR}/util.c
#            ${SRC_DIR}/vbrquantize.c
#            ${SRC_DIR}/VbrTag.c
#            ${SRC_DIR}/version.c
#            )

find_library(
              log-lib
              log )

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
