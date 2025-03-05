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

package com.wildfire.main.config;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import net.minecraftforge.fml.loading.FMLPaths;

public class BreastPresetConfiguration extends Configuration {

    private static final Path PRESET_DIR = FMLPaths.getOrCreateGameRelativePath(FMLPaths.CONFIGDIR.get().resolve("WildfireGender/presets"));
    public static final StringConfigKey PRESET_NAME = new StringConfigKey("preset_name", "");
    public static final FloatConfigKey BUST_SIZE = ClientConfiguration.BUST_SIZE;

    //added
    public static final FloatConfigKey BREASTS_DX = ClientConfiguration.BREASTS_DX;
    public static final FloatConfigKey BREASTS_DY = ClientConfiguration.BREASTS_DY;
    //

    public static final FloatConfigKey BREASTS_OFFSET_X = ClientConfiguration.BREASTS_OFFSET_X;
    public static final FloatConfigKey BREASTS_OFFSET_Y = ClientConfiguration.BREASTS_OFFSET_Y;
    public static final FloatConfigKey BREASTS_OFFSET_Z = ClientConfiguration.BREASTS_OFFSET_Z;
    public static final BooleanConfigKey BREASTS_UNIBOOB = ClientConfiguration.BREASTS_UNIBOOB;
    public static final FloatConfigKey BREASTS_CLEAVAGE = ClientConfiguration.BREASTS_CLEAVAGE;

    public static final FloatConfigKey HIPS_SIZE = ClientConfiguration.HIPS_SIZE;
    public static final FloatConfigKey HIPS_DX = ClientConfiguration.HIPS_DX;
    public static final FloatConfigKey HIPS_DY = ClientConfiguration.HIPS_DY;
    public static final FloatConfigKey HIPS_OFFSET_X = ClientConfiguration.HIPS_OFFSET_X;
    public static final FloatConfigKey HIPS_OFFSET_Y = ClientConfiguration.HIPS_OFFSET_Y;
    public static final FloatConfigKey HIPS_OFFSET_Z = ClientConfiguration.HIPS_OFFSET_Z;
    public static final BooleanConfigKey HIPS_UNIHIPS = ClientConfiguration.HIPS_UNIHIPS;
    public static final FloatConfigKey HIPS_CLEAVAGE = ClientConfiguration.HIPS_CLEAVAGE;

    public BreastPresetConfiguration(String cfgName) {
        super("WildfireGender/presets", cfgName);
    }

    public static BreastPresetConfiguration[] getBreastPresetConfigurationFiles() {
        File[] presetFiles = PRESET_DIR.toFile().listFiles();
        if (presetFiles != null) {
            return Arrays.stream(presetFiles).map(file -> {
                BreastPresetConfiguration cfg = new BreastPresetConfiguration(file.getName().replace(".json", ""));
                cfg.load(); // Load the preset values
                return cfg;
            }).toArray(BreastPresetConfiguration[]::new);
        }
        return new BreastPresetConfiguration[] {};
    }
}