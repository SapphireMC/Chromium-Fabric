/*
 * Copyright (c) 2022 DenaryDev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package io.sapphiremc.client.mixin;

import com.google.common.base.Strings;
import io.sapphiremc.client.SapphireClientMod;
import io.sapphiremc.client.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Inject(method = "render", at = @At("TAIL"))
    private void sapphireclient$render(MatrixStack matrices, float tickDelta, CallbackInfo callbackInfo) {
        boolean flag = this.client.world != null && (!this.client.options.hudHidden || this.client.currentScreen != null) && !this.client.options.debugEnabled;
        if (flag) {
            Config config = SapphireClientMod.getInstance().getConfig();
            PlayerEntity player = getCameraPlayer();
            List<String> list = new ArrayList<>();

            if (config.isShowFps()) {
                list.add(SapphireClientMod.getFpsString());
            }

            if (config.isShowTime()) {
                list.add(SapphireClientMod.getTime());
            }

            if (config.isShowCoords()) {
                list.add(SapphireClientMod.getCoordsString(player));
            }

            if (config.isShowLight()) {
                list.add(SapphireClientMod.getLightString(player));
            }

            if (config.isShowBiome()) {
                list.add(SapphireClientMod.getBiomeString(player));
            }

            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String s = list.get(i);
                    if (Strings.isNullOrEmpty(s)) continue;
                    this.getTextRenderer().drawWithShadow(matrices, s, 2, 2 + (i * 9), 0xE0E0E0);
                }
            }
        }
    }
}
