# DisplayLink Direct - Yocto Layer

Yocto Project layer for packaging the [DisplayLink Direct SDK](https://github.com/DisplayLink/displaylink-direct) and its
sample applications for Linux.

The layer provides the prebuilt DisplayLink Direct runtime, development files, Python bindings, a
[Qt 6 platform plugin](https://github.com/DisplayLink/displaylink-direct-qt), and sample programs created in C, C++ and
Python. The layer also contains an end-to-end demo for the Arduino UNO Q, buildable via [KAS](https://github.com/siemens/kas).

## Recipes

| Recipe | Contents |
| --- | --- |
| `libdlsdk` | Prebuilt DisplayLink Direct runtime library |
| `dlsdk-samples-basic` | Minimal C and C++ samples and the EDID utility |
| `dlsdk-samples-video` | GStreamer video clone and video wall samples |
| `dlsdk-qt-plugin` | DisplayLink Direct Qt 6 platform plugin |
| `python3-dlsdk` | Prebuilt CPython bindings for the target Python ABI |
| `python3-dlsdk-samples` | Minimal Python and SDL2 screensaver samples |
| `python3-pysdl2` | PySDL2 dependency used by the screensaver sample |

The `dlsdk-samples-basic` and `python3-dlsdk-samples` packages install all their respective samples. To select samples
individually, install `dlsdk-samples-basic-minimal`, `dlsdk-samples-basic-edid`, `python3-dlsdk-samples-minimal`, or
`python3-dlsdk-samples-screensaver`. The `libdlsdk-dev` package provides the C headers and CMake files.

## Layer dependencies

The base layer depends on OpenEmbedded Core. Some recipes require additional layers to be enabled:
| Component | Required layer collection |
| --- | --- |
| Video samples | `multimedia-layer` |
| Qt platform plugin | `qt6-layer` |
| Python samples and PySDL2 | `meta-python` |

The supplied KAS files include all required repositories and layers.

## Provided KAS configurations

| Yocto release | Architecture | Included KAS configuration |
| --- | --- | --- |
| Scarthgap | x86-64 | Recipe build using `qemux86-64` |
| Wrynose | x86-64 | Recipe build using `qemux86-64` |
| Wrynose | AArch64 | Arduino UNO Q demo image |

The prebuilt DisplayLink Direct binary recipes are limited to `x86_64` and `aarch64` architectures. All source revisions
and release artifacts used by the KAS configurations are pinned.

## Building with KAS

Install [KAS](https://kas.readthedocs.io/) or `kas-container`, then run the commands from the repository root. The
examples below use `kas-container`. Replace it with `kas` if building directly on the host.

### QEMU x86-64 recipe builds

Build the listed targets against Scarthgap:
```bash
kas-container build kas/qemu-x86-64/scarthgap.yml
```

Build them against Wrynose:
```bash
kas-container build kas/qemu-x86-64/wrynose.yml
```

These builds check recipe and package compatibility for x86-64 on both Yocto releases. They build and package the
targets, but do not create a bootable QEMU image.

## Arduino UNO Q demo image

The UNO Q image includes the DisplayLink Direct runtime, Qt plugin, Python bindings, and sample programs, including the Qt
documentation examples. It also sets the board's USB-C port to host mode so it can connect to a DisplayLink dock.

### Build the image

Run this from the repository root:

```bash
kas-container build kas/uno-q/wrynose.yml
```

The build produces a Qualcomm `qcomflash` image. Its artifacts are written to:

```text
build/tmp/deploy/images/uno-q/
```

### Ethernet and remote access

Connect the UNO Q to a DisplayLink dock and connect the dock's Ethernet port to a network with DHCP. The system will
automatically request an IP address when the Ethernet-over-USB interface appears.

The screensaver sample starts on boot and shows the board's IP address on the DisplayLink display. Stop it before running
another sample:

```bash
systemctl stop screensaver
```

OpenSSH and root password login are enabled. Connect using the displayed address and the root account:

```bash
ssh root@<board-ip>
```

The sample root password is `displaylink`. SSH is enabled in the UNO Q image configuration. To set a different
root password, update `ROOT_PASSWORD_HASH` in [`kas/uno-q/wrynose.yml`](kas/uno-q/wrynose.yml). Make sure to escape the
`$` characters in the string.

## Using the layer in another build

Add this repository to `BBLAYERS` together with the layers required by the components you want to build. Packages can
then be added to an image in the usual way, for example:
```bitbake
IMAGE_INSTALL:append = " \
    libdlsdk \
    dlsdk-samples-basic \
    dlsdk-samples-video \
    dlsdk-qt-plugin \
    python3-dlsdk \
    python3-dlsdk-samples \
"
```

The video samples depend on Libav, which requires accepting the `commercial` license flag. Add to build configuration:
```bitbake
LICENSE_FLAGS_ACCEPTED:append = " commercial"
```

## Installed samples

The native samples are installed in `${bindir}`, normally `/usr/bin`:
```text
dlsdk-minimal-c-sample
dlsdk-minimal-cxx-sample
dl-get-edid
video-clone
video-wall
```

The Python samples are installed below:
```text
/usr/share/dlsdk/samples/minimal-python/
/usr/share/dlsdk/samples/screensaver/
```

For trying out the Qt platform plugin, generic Qt sample apps were installed to:
```text
/usr/share/examples/demos
```

The default Qt platform is set to `dlsdk`. Applications can override it with `-platform`, for example to run a sample Qt
app (e.g., coffee) with DisplayLink Direct plugin using custom DPI:
```bash
/usr/share/examples/demos/coffee/bin/coffeemachine -platform dlsdk:dpi=144
```

Some Qt sample applications may not run with the DisplayLink Direct platform plugin because they require QRhi-based
rendering and a 3D graphics API, which the plugin does not support. The recommended samples include `coffee` and
`thermostat`.

## Updating the layer

When upgrading to a new DisplayLink Direct release, make sure to update the source revision and release artifact hashes
in all recipe files.

The `python3-dlsdk` recipe selects a wheel using the target architecture and ABI. To regenerate the wheel checksums, run
in the repository root:
```bash
scripts/generate-wheel-checksums.sh <release-tag> <wheel-version>
```

For example:
```bash
scripts/generate-wheel-checksums.sh v2.0.0 2.0.0+g287e926058.build4
```

Finally, remember to update the recipe filenames accordingly.

## License

This Yocto layer is distributed under the MIT License, see [LICENSE](LICENSE).

The DisplayLink Direct runtime library and Python bindings are distributed as prebuilt proprietary components. Other
upstream components and dependencies retain their respective licenses.
