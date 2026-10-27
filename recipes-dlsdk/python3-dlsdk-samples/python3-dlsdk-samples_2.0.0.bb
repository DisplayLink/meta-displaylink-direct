SUMMARY = "DisplayLink Direct Python Samples"
DESCRIPTION = "Sample scripts for the DisplayLink Direct SDK Python bindings"
HOMEPAGE = "https://github.com/DisplayLink/displaylink-direct"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69f83c02392b3d2162b055caf09bef86"
COMPATIBLE_HOST = "(aarch64|x86_64).*-linux"

SRC_URI = "git://git@github.com/DisplayLink/displaylink-direct.git;protocol=https;branch=main;destsuffix=git \
           file://screensaver-prerelease.patch"
# v2.0.0
SRCREV ?= "4e7ed5ffe6a677512be0e33dd892ae5685c40f55"

# Scarthgap does not have UNPACKDIR
S = "${@d.getVar('UNPACKDIR') or d.getVar('WORKDIR')}/git/samples"

PACKAGES =+ " \
    ${PN}-minimal \
    ${PN}-screensaver \
"

# Installing the main package pulls in both samples
ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} = "${PN}-minimal ${PN}-screensaver"

RDEPENDS:${PN}-minimal = "python3-dlsdk python3-numpy"
RDEPENDS:${PN}-screensaver = "python3-dlsdk python3-numpy python3-pysdl2 ttf-dejavu-sans"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d "${D}${datadir}/dlsdk/samples/minimal-python"
    install -d "${D}${datadir}/dlsdk/samples/screensaver"

    install -m 0644 "${S}/minimal-python/minimal-example.py" \
        "${D}${datadir}/dlsdk/samples/minimal-python/minimal-example.py"

    install -m 0644 "${S}/screensaver/screensaver.py" \
        "${D}${datadir}/dlsdk/samples/screensaver/screensaver.py"

    install -m 0644 "${S}/screensaver/dl_logo.png" \
        "${D}${datadir}/dlsdk/samples/screensaver/dl_logo.png"
}

FILES:${PN}-minimal = "${datadir}/dlsdk/samples/minimal-python"
FILES:${PN}-screensaver = "${datadir}/dlsdk/samples/screensaver"
