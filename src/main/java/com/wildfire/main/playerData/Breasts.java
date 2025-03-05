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
*/
package com.wildfire.main.playerData;

import com.wildfire.main.config.ClientConfiguration;
import com.wildfire.main.config.ConfigKey;
import com.wildfire.main.config.Configuration;

import java.util.function.Consumer;

public class Breasts {

    private float xOffset = ClientConfiguration.BREASTS_OFFSET_X.getDefault(), yOffset = ClientConfiguration.BREASTS_OFFSET_Y.getDefault(), zOffset = ClientConfiguration.BREASTS_OFFSET_Z.getDefault();
    private float cleavage = ClientConfiguration.BREASTS_CLEAVAGE.getDefault();

    private float dx = ClientConfiguration.BREASTS_DX.getDefault();
    private float dy = ClientConfiguration.BREASTS_DY.getDefault();
    private boolean uniboob = ClientConfiguration.BREASTS_UNIBOOB.getDefault();

    private <VALUE> boolean updateValue(ConfigKey<VALUE> key, VALUE value, Consumer<VALUE> setter) {
        if (key.validate(value)) {
            setter.accept(value);
            return true;
        }
        return false;
    }

    private <VALUE> boolean updateFrom(ConfigKey<VALUE> key, Configuration copyFrom, Consumer<VALUE> setter) {
        VALUE value = copyFrom.get(key);
        if (value == null) {
            return false;
        }
        return updateValue(key, value, setter);
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }
    public boolean updateDx(float value){
        return updateValue(ClientConfiguration.BREASTS_DX, value, v-> this.dx = v);
    }
    public boolean updateDy(float value){
        return updateValue(ClientConfiguration.BREASTS_DY, value, v-> this.dy = v);
    }

    public float getXOffset() {
        return xOffset;
    }

    public boolean updateXOffset(float value) {
        return updateValue(ClientConfiguration.BREASTS_OFFSET_X, value, v -> this.xOffset = v);
    }

    public float getYOffset() {
        return yOffset;
    }

    public boolean updateYOffset(float value) {
        return updateValue(ClientConfiguration.BREASTS_OFFSET_Y, value, v -> this.yOffset = v);
    }

    public float getZOffset() {
        return zOffset;
    }

    public boolean updateZOffset(float value) {
        return updateValue(ClientConfiguration.BREASTS_OFFSET_Z, value, v -> this.zOffset = v);
    }

    public float getCleavage() {
        return cleavage;
    }

    public boolean updateCleavage(float value) {
        return updateValue(ClientConfiguration.BREASTS_CLEAVAGE, value, v -> this.cleavage = v);
    }

    public boolean isUniboob() {
        return uniboob;
    }

    public boolean updateUniboob(boolean value) {
        return updateValue(ClientConfiguration.BREASTS_UNIBOOB, value, v -> this.uniboob = v);
    }

    public boolean copyFrom(Configuration copyFrom) {
        //Note: Use bitwise or to ensure all get ran
        return  updateFrom(ClientConfiguration.BREASTS_DX, copyFrom, v->this.dx = v)|
                updateFrom(ClientConfiguration.BREASTS_DY, copyFrom, v-> this.dy = v)|
                updateFrom(ClientConfiguration.BREASTS_OFFSET_X, copyFrom, v -> this.xOffset = v) |
                updateFrom(ClientConfiguration.BREASTS_OFFSET_Y, copyFrom, v -> this.yOffset = v) |
                updateFrom(ClientConfiguration.BREASTS_OFFSET_Z, copyFrom, v -> this.zOffset = v) |
                updateFrom(ClientConfiguration.BREASTS_CLEAVAGE, copyFrom, v -> this.cleavage = v) |
                updateFrom(ClientConfiguration.BREASTS_UNIBOOB, copyFrom, v -> this.uniboob = v);
    }
}
