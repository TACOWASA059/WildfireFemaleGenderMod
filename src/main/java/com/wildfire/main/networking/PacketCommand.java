/*
    Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
    Copyright (C) 2023 WildfireRomeo
    Additional modifications (C) 2025 tacowasa_059

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    ---------------------------------------------------------------------------
    This file is part of the Wildfire's Female Gender Mod.
    Changes from the original version:
    - added command packet( 2025-03-04)
*/

package com.wildfire.main.networking;

import com.wildfire.main.WildfireGender;
import com.wildfire.main.playerData.GenderPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCommand extends PacketGenderInfo{
    protected PacketCommand(GenderPlayer plr) {
        super(plr);
    }

    public PacketCommand(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public static void handle(final PacketCommand packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                GenderPlayer plr = WildfireGender.getOrAddPlayerById(packet.uuid);
                packet.updatePlayerFromPacket(plr);
                GenderPlayer.saveGenderInfo(plr);
            }
        });
        context.get().setPacketHandled(true);
    }
}
