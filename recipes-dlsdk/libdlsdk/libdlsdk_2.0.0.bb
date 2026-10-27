SUMMARY = "DisplayLink Direct SDK"
DESCRIPTION = "Prebuilt DisplayLink Direct runtime library, headers, and CMake package files"
HOMEPAGE = "https://github.com/DisplayLink/displaylink-direct"
LICENSE = "CLOSED"
COMPATIBLE_HOST = "(aarch64|x86_64).*-linux"

DLSDK_GIT_URI = "git://git@github.com/DisplayLink/displaylink-direct.git;protocol=https;branch=main;destsuffix=git"
# v2.0.0
DLSDK_SRCREV ?= "4e7ed5ffe6a677512be0e33dd892ae5685c40f55"

DLSDK_BIN_URI ?= "https://github.com/DisplayLink/displaylink-direct/releases/download/v2.0.0/binaries.zip;subdir=bin"
DLSDK_BIN_SHA256 ?= "edb0a06be8e3d06cf9e3e3bc2acea068826afdfa3386c5c2f517b188fda8e86a"

SRC_URI = "${DLSDK_GIT_URI} \
           ${DLSDK_BIN_URI};name=dlsdk-bins \
           file://dlsdkConfig.cmake \
"

SRC_URI[dlsdk-bins.sha256sum] = "${DLSDK_BIN_SHA256}"
SRCREV = "${DLSDK_SRCREV}"

# Scarthgap does not have UNPACKDIR
S = "${@d.getVar('UNPACKDIR') or d.getVar('WORKDIR')}"

DEPENDS = "libusb1"
RDEPENDS:${PN} += "libusb1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${S}/bin/linux/${TARGET_ARCH}/libdlsdk.so ${D}${libdir}/libdlsdk.so

    install -d ${D}${includedir}/dlsdk
    install -m 0644 ${S}/git/include/dlsdk/dlsdk.h ${D}${includedir}/dlsdk/dlsdk.h

    install -d ${D}${libdir}/cmake/dlsdk
    install -m 0644 ${S}/dlsdkConfig.cmake ${D}${libdir}/cmake/dlsdk/dlsdkConfig.cmake

    install -m 0644 ${S}/git/dlsdkConfigVersion.cmake ${D}${libdir}/cmake/dlsdk/dlsdkConfigVersion.cmake
}

# Put unversioned .so into main runtime package instead of -dev one
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES:${PN}-dev += "${libdir}/cmake/dlsdk"

INSANE_SKIP:${PN} += "already-stripped ldflags"
