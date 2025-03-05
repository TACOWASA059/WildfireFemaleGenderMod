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
    - added render behind ( 2025-03-04)
*/
package com.wildfire.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventory;

/**
 * render entity behind
 */
public class RenderEntityBehind {
    public static void renderEntityInInventoryFollowsMouse(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float pMouseX, float pMouseY, LivingEntity pEntity) {
        float f = (float)Math.atan((double)(pMouseX / 40.0F));
        float f1 = (float)Math.atan((double)(pMouseY / 40.0F));
        // Forge: Allow passing in direct angle components instead of mouse position
        renderEntityInInventoryFollowsAngle(pGuiGraphics, pX, pY, pScale, f, f1, pEntity);
    }

    public static void renderEntityInInventoryFollowsAngle(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float angleXComponent, float angleYComponent, LivingEntity pEntity) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(-f1 * 20.0F * ((float)Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = pEntity.yBodyRot;
        float f3 = pEntity.getYRot();
        float f4 = pEntity.getXRot();
        float f5 = pEntity.yHeadRotO;
        float f6 = pEntity.yHeadRot;
        pEntity.yBodyRot = f * 20.0F;
        pEntity.setYRot( f * 40.0F);
        pEntity.setXRot(-f1 * 20.0F);
        pEntity.yHeadRot = pEntity.getYRot();
        pEntity.yHeadRotO = pEntity.getYRot();
        renderEntityInInventory(pGuiGraphics, pX, pY-100, pScale, quaternionf, quaternionf1, pEntity);
        pEntity.yBodyRot = f2;
        pEntity.setYRot(f3);
        pEntity.setXRot(f4);
        pEntity.yHeadRotO = f5;
        pEntity.yHeadRot = f6;
    }
}
