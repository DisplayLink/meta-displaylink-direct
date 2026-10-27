SUMMARY = "DisplayLink Direct basic samples"
DESCRIPTION = "Sample C and C++ programs for the DisplayLink Direct SDK"
HOMEPAGE = "https://github.com/DisplayLink/displaylink-direct"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69f83c02392b3d2162b055caf09bef86"
COMPATIBLE_HOST = "(aarch64|x86_64).*-linux"

DLSDK_GIT_URI = "git://git@github.com/DisplayLink/displaylink-direct.git;protocol=https;branch=main;destsuffix=git"
# v2.0.0
DLSDK_SRCREV ?= "4e7ed5ffe6a677512be0e33dd892ae5685c40f55"

SRC_URI = "${DLSDK_GIT_URI} \
           file://CMakeLists.txt;subdir=git/samples \
"
SRCREV = "${DLSDK_SRCREV}"

# Scarthgap does not have UNPACKDIR
S = "${@d.getVar('UNPACKDIR') or d.getVar('WORKDIR')}/git/samples"

inherit cmake

DEPENDS = "libdlsdk"

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release"
OECMAKE_TARGET_COMPILE = "dlsdk-minimal-c-sample dlsdk-minimal-cxx-sample dl-get-edid"

# Installing the main package pulls in both minimal samples and EDID utility
ALLOW_EMPTY:${PN} = "1"
PACKAGES =+ "${PN}-minimal ${PN}-edid"
RDEPENDS:${PN} += "${PN}-minimal ${PN}-edid"

FILES:${PN}-minimal = "${bindir}/dlsdk-minimal-c-sample ${bindir}/dlsdk-minimal-cxx-sample"
FILES:${PN}-edid = "${bindir}/dl-get-edid"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/minimal-c/dlsdk-minimal-c-sample ${D}${bindir}/dlsdk-minimal-c-sample
    install -m 0755 ${B}/minimal-c++/dlsdk-minimal-cxx-sample ${D}${bindir}/dlsdk-minimal-cxx-sample
    install -m 0755 ${B}/dl-get-edid/dl-get-edid ${D}${bindir}/dl-get-edid
}
