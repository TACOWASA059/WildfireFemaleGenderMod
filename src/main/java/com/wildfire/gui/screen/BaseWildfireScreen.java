/*
    Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
    Copyright (C) 2023 WildfireRomeo

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.wildfire.gui.screen;

import com.wildfire.main.playerData.GenderPlayer;
import com.wildfire.main.WildfireGender;
import java.util.UUID;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class BaseWildfireScreen extends Screen {

    protected final UUID playerUUID;
    protected final Screen parent;

    protected BaseWildfireScreen(Component title, Screen parent, UUID uuid) {
        super(title);
        this.parent = parent;
        this.playerUUID = uuid;
    }

    public GenderPlayer getPlayer() {
        return WildfireGender.getPlayerById(this.playerUUID);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}