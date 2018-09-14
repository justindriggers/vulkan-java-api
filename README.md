[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/justindriggers/vulkan-java-api/blob/master/LICENSE.md)

## Vulkan Java API

The Vulkan Java API is an object-oriented Java library, backed by the
[Lightweight Java Game Library](https://www.lwjgl.org/) Vulkan API. The Java API aims to encapsulate the complexities
of struct construction and memory management into classes that are more easily understood by those uncomfortable with
such concepts.

### But why?

The original goal behind writing this library was, first and foremost, to familiarize myself with the Vulkan API.

However, this library does have a lot of potential for the future:
- Apple recently [deprecated OpenGL](https://developer.apple.com/macos/whats-new/#deprecationofopenglandopencl), pushing
developers to use Metal instead.
- KhronosGroup released [MoltenVK](https://github.com/KhronosGroup/MoltenVK), providing a Vulkan-to-Metal compatibility
layer for Apple systems.
- It is unlikely that libGDX (the most prominent Java-based game library at the time of this writing) will
[support Vulkan](https://github.com/libgdx/libgdx/issues/3344#issuecomment-312908022) any time soon.

### Concepts

- Vulkan APIs that begin with `vkCreate` are covered by Java Object instantiation.
  - The `vkCreateSemaphore` function (which requires a `VkDevice`) is invoked simply by constructing a
  `new Semaphore(device)`.
- Vulkan APIs that begin with `vkDestroy` are invoked by implementing the `AutoCloseable` interface.
  - The `vkDestroySemaphore` function is invoked by calling `semaphore.close()`, or using a `try-with-resources` block.
- Vulkan APIs that act on a specific parameter are implemented as methods on the corresponding object.
  - The `vkDeviceWaitIdle` function (which requires a `VkDevice`) is invoked by calling `device.waitIdle()`.

### Getting Started

#### build.gradle

```
import org.gradle.internal.os.OperatingSystem
   
switch (OperatingSystem.current()) {
   case OperatingSystem.WINDOWS:
       project.ext.lwjglNatives = "natives-windows"
       break
   case OperatingSystem.LINUX:
       project.ext.lwjglNatives = "natives-linux"
       break
   case OperatingSystem.MAC_OS:
       project.ext.lwjglNatives = "natives-macos"
       break
}
   
repositories {
   mavenCentral()
   maven {
       url = 'https://dl.bintray.com/justindriggers/vulkan-java-api'
   }
}
   
dependencies {
   implementation('com.justindriggers:vulkan-java-api:0.+')

   implementation("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
   implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
   implementation("org.lwjgl:lwjgl-vulkan:$lwjglVersion:$lwjglNatives")
}
```

#### Example

Check out https://github.com/justindriggers/vulkan-java-api-example for a more comprehensive usage example.