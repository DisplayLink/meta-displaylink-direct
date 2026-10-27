SUMMARY = "Arduino UNO Q sample configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "^uno-q$"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://usb-ethernet.network \
           file://screensaver.service"
S = "${UNPACKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "screensaver.service"
SYSTEMD_AUTO_ENABLE = "enable"
CONFFILES:${PN} = "/etc/systemd/network/10-usb-ethernet.network"
RDEPENDS:${PN} += "python3-dlsdk-samples-screensaver"

python do_install() {
    from pathlib import Path

    dest = Path(d.getVar("D"))
    unpackdir = Path(d.getVar("UNPACKDIR"))

    def write(path, content, mode=0o644):
        target = dest / path.lstrip("/")
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(content, encoding="utf-8")
        target.chmod(mode)

    # Set the USB-C controller to host mode so it can communicate with docks
    write(
        d.getVar("sysconfdir") + "/udev/rules.d/99-usb-host.rules",
        'ACTION=="add", SUBSYSTEM=="usb_role", KERNEL=="4e00000.usb-role-switch", ATTR{role}="host"\n',
    )

    unit = (unpackdir / "screensaver.service").read_text()
    write(d.getVar("systemd_system_unitdir") + "/screensaver.service", unit)

    network = (unpackdir / "usb-ethernet.network").read_text()
    write(d.getVar("sysconfdir") + "/systemd/network/10-usb-ethernet.network", network)
}

FILES:${PN} += "${systemd_system_unitdir}/screensaver.service"
