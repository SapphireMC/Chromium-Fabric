/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.chromium.mixin.client;

import io.sapphiremc.chromium.client.gui.ChromiumTitleScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class MixinWindow {

    @Shadow @Final private long handle;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwMakeContextCurrent(J)V"), remap = false)
    private void chromium$setMinimumWindowSize(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, @Nullable String videoMode, String title, CallbackInfo callbackInfo) {
        GLFW.glfwSetWindowSizeLimits(handle, 1000, 700, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE);
    }

    @Inject(method = "toggleFullscreen", at = @At("HEAD"))
    private void chromium$closeExitMenu(CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof ChromiumTitleScreen screen && screen.isConfirmOpened()) {
            screen.setConfirmOpened(false);
        }
    }
}
