SUMMARY = "DisplayLink Direct Python bindings"
DESCRIPTION = "Python bindings for the DisplayLink Direct SDK"
HOMEPAGE = "https://github.com/DisplayLink/displaylink-direct"
LICENSE = "CLOSED"
COMPATIBLE_HOST = "(aarch64|x86_64).*-linux"

inherit python3-dir python3native python3targetconfig

DEPENDS = "libdlsdk python3-installer-native"
RDEPENDS:${PN} += "libdlsdk python3-core libusb1"

WHEEL_RELEASE = "v${PV}"
WHEEL_VERSION = "${PV}+g287e926058.build4"

# Pick correct wheel platform and version from GitHub release page
WHEEL_ABI = "cp${@d.getVar('PYTHON_BASEVERSION').replace('.', '')}"
WHEEL_PLATFORM = "manylinux_2_31_${TARGET_ARCH}"
WHEEL_FILENAME = "dlsdk-${WHEEL_VERSION}-${WHEEL_ABI}-${WHEEL_ABI}-${WHEEL_PLATFORM}.whl"

SRC_URI = " \
    https://github.com/DisplayLink/displaylink-direct/releases/download/${WHEEL_RELEASE}/${WHEEL_FILENAME} \
"

require python3-dlsdk-wheel-checksums_${PV}.inc
SRC_URI[sha256sum] = "${@d.getVar('WHEEL_SHA256_%s_%s' % (d.getVar('WHEEL_ABI'), d.getVar('TARGET_ARCH')))}"

# Scarthgap does not have UNPACKDIR
S = "${@d.getVar('UNPACKDIR') or d.getVar('WORKDIR')}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    test -f "${S}/${WHEEL_FILENAME}" || bbfatal "Wheel not found: ${S}/${WHEEL_FILENAME}"
    ${PYTHON} -m installer \
        --compile-bytecode=0 \
        --interpreter "${USRBINPATH}/env python3" \
        --destdir="${D}" \
        --prefix="${prefix}" \
        "${S}/${WHEEL_FILENAME}"
    # Delete RECORD to prevent pip from interfering with the OE-managed packages
    find "${D}" -path '*.dist-info/RECORD' -delete
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}"

INSANE_SKIP:${PN} += "already-stripped ldflags"
