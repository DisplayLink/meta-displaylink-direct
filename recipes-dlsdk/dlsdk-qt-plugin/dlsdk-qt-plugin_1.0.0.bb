SUMMARY = "DisplayLink Direct Qt6 platform plugin"
DESCRIPTION = "DisplayLink Direct Qt6 platform plugin for Linux"
HOMEPAGE = "https://github.com/DisplayLink/displaylink-direct-qt"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f9c9dc109ce2b4429279c18a37d00652"
COMPATIBLE_HOST = "(aarch64|x86_64).*-linux"

SRC_URI = "git://git@github.com/DisplayLink/displaylink-direct-qt.git;protocol=https;branch=main;destsuffix=git"
# v1.0.0
SRCREV = "1f93001e09ff7bcc0f91e1db8914477520733bd1"

# Scarthgap does not have UNPACKDIR
S = "${@d.getVar('UNPACKDIR') or d.getVar('WORKDIR')}/git"

DEPENDS = "libdlsdk qtbase"

inherit qt6-cmake

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release -DBUILD_TESTS=OFF"

do_install() {
    install -d ${D}${libdir}/plugins/platforms
    install -m 0755 ${B}/src/plugin/plugins/platforms/libqdlsdk.so ${D}${libdir}/plugins/platforms/libqdlsdk.so
}

FILES:${PN} += "${libdir}/plugins/platforms/libqdlsdk.so"
