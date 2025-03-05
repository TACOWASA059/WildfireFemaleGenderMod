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
/*
    Modifications:
    - 2025-03-03: tacowasa059 - Added breast width and height settings
    - 2025-03-04: tacowasa059 - Added hip settings
*/

package com.wildfire.main.networking;

import com.wildfire.main.playerData.Breasts;
import com.wildfire.main.playerData.GenderPlayer;
import com.wildfire.main.playerData.GenderPlayer.Gender;
import java.util.UUID;

import com.wildfire.main.playerData.Hips;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PacketGenderInfo {
    protected final UUID uuid;
    private final Gender gender;


    //physics variables
    private final boolean breast_physics;
    private final boolean show_in_armor;
    private final float bounceMultiplier;
    private final float floppyMultiplier;

    private final float bust_size;
    private final float dx, dy;
    private final float xOffset, yOffset, zOffset;
    private final boolean uniboob;
    private final float cleavage;

    //hips
    private final float hips_size;
    private final float hip_dx, hip_dy;
    private final float hip_xOffset, hip_yOffset, hip_zOffset;
    private final boolean hip_uniHip;
    private final float hip_cleavage;
    //
    private final boolean hurtSounds;

    protected PacketGenderInfo(GenderPlayer plr) {
        this.uuid = plr.uuid;
        this.gender = plr.getGender();
        this.bust_size = plr.getBustSize();
        this.hips_size = plr.getHipSize();
        this.hurtSounds = plr.hasHurtSounds();

        //physics variables
        this.breast_physics = plr.hasBreastPhysics();
        this.show_in_armor = plr.showBreastsInArmor();
        this.bounceMultiplier = plr.getBounceMultiplierRaw();
        this.floppyMultiplier = plr.getFloppiness();

        Breasts breasts = plr.getBreasts();
        this.dx = breasts.getDx();
        this.dy = breasts.getDy();
        this.xOffset = breasts.getXOffset();
        this.yOffset = breasts.getYOffset();
        this.zOffset = breasts.getZOffset();

        this.uniboob = breasts.isUniboob();
        this.cleavage = breasts.getCleavage();

        Hips hips = plr.getHips();
        this.hip_dx = hips.getDx();
        this.hip_dy = hips.getDy();
        this.hip_xOffset = hips.getXOffset();
        this.hip_yOffset = hips.getYOffset();
        this.hip_zOffset = hips.getZOffset();

        this.hip_uniHip = hips.isUniHips();
        this.hip_cleavage = hips.getCleavage();
    }

    protected PacketGenderInfo(FriendlyByteBuf buffer) {
        this.uuid = buffer.readUUID();
        this.gender = buffer.readEnum(Gender.class);
        this.bust_size = buffer.readFloat();
        this.hips_size = buffer.readFloat();
        this.hurtSounds = buffer.readBoolean();

        //physics variables
        this.breast_physics = buffer.readBoolean();
        this.show_in_armor = buffer.readBoolean();
        this.bounceMultiplier = buffer.readFloat();
        this.floppyMultiplier = buffer.readFloat();

        this.dx = buffer.readFloat();
        this.dy = buffer.readFloat();
        this.xOffset = buffer.readFloat();
        this.yOffset = buffer.readFloat();
        this.zOffset = buffer.readFloat();
        this.uniboob = buffer.readBoolean();
        this.cleavage = buffer.readFloat();

        this.hip_dx = buffer.readFloat();
        this.hip_dy = buffer.readFloat();
        this.hip_xOffset = buffer.readFloat();
        this.hip_yOffset = buffer.readFloat();
        this.hip_zOffset = buffer.readFloat();
        this.hip_uniHip = buffer.readBoolean();
        this.hip_cleavage = buffer.readFloat();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.uuid);
        buffer.writeEnum(this.gender);
        buffer.writeFloat(this.bust_size);
        buffer.writeFloat(this.hips_size);
        buffer.writeBoolean(this.hurtSounds);
        buffer.writeBoolean(this.breast_physics);
        buffer.writeBoolean(this.show_in_armor);
        buffer.writeFloat(this.bounceMultiplier);
        buffer.writeFloat(this.floppyMultiplier);

        buffer.writeFloat(this.dx);
        buffer.writeFloat(this.dy);
        buffer.writeFloat(this.xOffset);
        buffer.writeFloat(this.yOffset);
        buffer.writeFloat(this.zOffset);
        buffer.writeBoolean(this.uniboob);
        buffer.writeFloat(this.cleavage);

        buffer.writeFloat(this.hip_dx);
        buffer.writeFloat(this.hip_dy);
        buffer.writeFloat(this.hip_xOffset);
        buffer.writeFloat(this.hip_yOffset);
        buffer.writeFloat(this.hip_zOffset);
        buffer.writeBoolean(this.hip_uniHip);
        buffer.writeFloat(this.hip_cleavage);
    }

    protected void updatePlayerFromPacket(GenderPlayer plr) {
        plr.updateGender(gender);
        plr.updateBustSize(bust_size);
        plr.updateHipSize(hips_size);
        plr.updateHurtSounds(hurtSounds);

        //physics
        plr.updateBreastPhysics(breast_physics);
        plr.updateShowBreastsInArmor(show_in_armor);
        plr.updateBounceMultiplier(bounceMultiplier);
        plr.updateFloppiness(floppyMultiplier);
        //WildfireGender.logger.debug("{} - {}", plr.username, plr.gender);

        Breasts breasts = plr.getBreasts();
        breasts.updateDx(dx);
        breasts.updateDy(dy);
        breasts.updateXOffset(xOffset);
        breasts.updateYOffset(yOffset);
        breasts.updateZOffset(zOffset);
        breasts.updateUniboob(uniboob);
        breasts.updateCleavage(cleavage);

        Hips hips = plr.getHips();
        hips.updateDx(hip_dx);
        hips.updateDy(hip_dy);
        hips.updateXOffset(hip_xOffset);
        hips.updateYOffset(hip_yOffset);
        hips.updateZOffset(hip_zOffset);
        hips.updateUniHips(hip_uniHip);
        hips.updateCleavage(hip_cleavage);

    }
}
