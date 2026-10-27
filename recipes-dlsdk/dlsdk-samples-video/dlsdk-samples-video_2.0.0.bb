SUMMARY = "DisplayLink Direct video samples"
DESCRIPTION = "Sample video player programs for the DisplayLink Direct SDK"
HOMEPAGE = "https://github.com/DisplayLink/displaylink-direct"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=69f83c02392b3d2162b055caf09bef86"
COMPATIBLE_HOST = "(aarch64|x86_64).*-linux"

DLSDK_GIT_URI = "git://git@github.com/DisplayLink/displaylink-direct.git;protocol=https;branch=main;destsuffix=git"
# v2.0.0
DLSDK_SRCREV ?= "4e7ed5ffe6a677512be0e33dd892ae5685c40f55"

SRC_URI = "${DLSDK_GIT_URI}"
SRCREV = "${DLSDK_SRCREV}"

# Scarthgap does not have UNPACKDIR
S = "${@d.getVar('UNPACKDIR') or d.getVar('WORKDIR')}/git/samples/video"

inherit cmake pkgconfig

DEPENDS = "libdlsdk gstreamer1.0 gstreamer1.0-plugins-base"
RDEPENDS:${PN} += "gstreamer1.0-plugins-base gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-libav"

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release -DUSE_GSTREAMER=ON"
OECMAKE_TARGET_COMPILE = "video-clone video-wall"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/video-clone ${D}${bindir}/video-clone
    install -m 0755 ${B}/video-wall ${D}${bindir}/video-wall
}
