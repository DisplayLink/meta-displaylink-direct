SUMMARY = "Python bindings for SDL2"
HOMEPAGE = "https://github.com/py-sdl/py-sdl2"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://doc/copying.rst;md5=7e42038ef4284ebf295b4b84cf7dbfce"

SRC_URI[sha256sum] = "48c6ef01a4eb123db5f7e46e1a1b565675755b07e615f3fe20a623c94735b52b"

PYPI_PACKAGE = "pysdl2"
inherit pypi setuptools3

RDEPENDS:${PN} += "python3-core python3-ctypes libsdl2 libsdl2-image libsdl2-ttf"
