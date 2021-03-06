/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.chromium.mixin.client;

import io.sapphiremc.chromium.ChromiumMod;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Mixin(ChatHud.class)
public abstract class MixinChatHud extends DrawableHelper {

    @ModifyConstant(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", constant = @Constant(intValue = 100))
    private int chromium$getMaxMessages(int max) {
        return ChromiumMod.getConfig().getMaxMessages();
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Text chromium$addMessageTimePrefix(Text message) {
        if (ChromiumMod.getConfig().isShowMessagesTime()) {
            Text hoverText = Text.translatable(Formatting.YELLOW + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ").format(new Date()) + TimeZone.getDefault().getID());
            Text timeText = Text.translatable(Formatting.GRAY + new SimpleDateFormat("[HH:mm:ss] ").format(new Date()) + Formatting.RESET).styled(
                    (style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))));
            return Text.translatable("%s%s", timeText, message);
        } else {
            return Text.translatable("%s", message);
        }
    }
}
