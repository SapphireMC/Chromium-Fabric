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
package io.sapphiremc.client.config;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class Config {

    @Getter @Setter @Expose boolean showFps = false;
    @Getter @Setter @Expose boolean showTime = false;
    @Getter @Setter @Expose boolean showCoords = true;
    @Getter @Setter @Expose boolean showLight = false;
    @Getter @Setter @Expose boolean showBiome = false;

    @Getter @Setter @Expose boolean showMessagesTime = false;

    @Getter @Setter @Expose TitleScreenProvider titleScreenProvider = TitleScreenProvider.SAPPHIRECLIENT;

    public enum TitleScreenProvider {
        SAPPHIRECLIENT,
        MINECRAFT
    }
}
