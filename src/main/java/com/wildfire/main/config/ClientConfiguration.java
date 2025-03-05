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
    - 2025-03-03: tacowasa059 - Change the range of settings available in config.
    - 2025-03-04: tacowasa059 - Added hip settings
*/
package com.wildfire.main.config;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientConfiguration extends Configuration {

    public static final UUIDConfigKey USERNAME = new UUIDConfigKey("username", UUID.nameUUIDFromBytes("UNKNOWN".getBytes(StandardCharsets.UTF_8)));
    public static final GenderConfigKey GENDER = new GenderConfigKey("gender");


    public static final FloatConfigKey BUST_SIZE = new FloatConfigKey("bust_size", 0.6F, 0, 0.8f*4f); //modified
    //added
    public static final FloatConfigKey BREASTS_DX = new FloatConfigKey("breasts_dx", 4.0F, 2.5F, 6.0F);
    public static final FloatConfigKey BREASTS_DY = new FloatConfigKey("breasts_dy", 5.0F, 2.5F, 6.0F);
    //
    public static final FloatConfigKey BREASTS_OFFSET_X = new FloatConfigKey("breasts_xOffset", 0.0F, -1, 1);
    public static final FloatConfigKey BREASTS_OFFSET_Y = new FloatConfigKey("breasts_yOffset", 0.0F, -1, 1);
    public static final FloatConfigKey BREASTS_OFFSET_Z = new FloatConfigKey("breasts_zOffset", 0.0F, -2, 0);
    public static final BooleanConfigKey BREASTS_UNIBOOB = new BooleanConfigKey("breasts_uniboob", true);
    public static final FloatConfigKey BREASTS_CLEAVAGE = new FloatConfigKey("breasts_cleavage", 0, -0.1f, 0.1F *3);//modified

    //added
    public static final FloatConfigKey HIPS_SIZE = new FloatConfigKey("hips_size", 0.6F, 0, 0.8f*4f);
    public static final FloatConfigKey HIPS_DX = new FloatConfigKey("hips_dx", 4.0F, 2.5F, 6.0F);
    public static final FloatConfigKey HIPS_DY = new FloatConfigKey("hips_dy", 5.0F, 2.5F, 6.0F);
    public static final FloatConfigKey HIPS_OFFSET_X = new FloatConfigKey("hips_xOffset", 0.0F, -1, 1);
    public static final FloatConfigKey HIPS_OFFSET_Y = new FloatConfigKey("hips_yOffset", 0.0F, -1, 2);
    public static final FloatConfigKey HIPS_OFFSET_Z = new FloatConfigKey("hips_zOffset", 0.0F, -2, 0);
    public static final BooleanConfigKey HIPS_UNIHIPS = new BooleanConfigKey("hips_uniboob", false);
    public static final FloatConfigKey HIPS_CLEAVAGE = new FloatConfigKey("hips_cleavage", 0, -0.1f, 0.1F *3);
    //



    public static final BooleanConfigKey BREAST_PHYSICS = new BooleanConfigKey("breast_physics", true);
    public static final BooleanConfigKey HURT_SOUNDS = new BooleanConfigKey("hurt_sounds", true);
    public static final BooleanConfigKey ARMOR_PHYSICS_OVERRIDE = new BooleanConfigKey("armor_physics_override", false);
    public static final BooleanConfigKey SHOW_IN_ARMOR = new BooleanConfigKey("show_in_armor", true);
    public static final FloatConfigKey BOUNCE_MULTIPLIER = new FloatConfigKey("bounce_multiplier", 0.34F, 0, 0.5f);
    public static final FloatConfigKey FLOPPY_MULTIPLIER = new FloatConfigKey("floppy_multiplier", 0.75F, 0.25f, 1);


    public ClientConfiguration(String saveLoc, String cfgName) {
        super(saveLoc, cfgName);
    }
}
