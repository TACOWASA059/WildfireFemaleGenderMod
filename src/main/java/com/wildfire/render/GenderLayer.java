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
    - 2025-03-03: tacowasa059 - Added breast width and height settings
    - 2025-03-04: tacowasa059 - Added Hip Renderer
    - 2025-03-06: tacowasa059 - changed clipping
*/
package com.wildfire.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wildfire.api.IGenderArmor;
import com.wildfire.main.playerData.Breasts;
import com.wildfire.main.WildfireHelper;
import com.wildfire.main.config.GeneralClientConfig;
import com.wildfire.main.playerData.Hips;
import com.wildfire.physics.BreastPhysics;
import com.wildfire.physics.HipPhysics;
import com.wildfire.render.WildfireModelRenderer.BreastModelBox;
import com.wildfire.render.WildfireModelRenderer.OverlayModelBox;
import com.wildfire.render.WildfireModelRenderer.PositionTextureVertex;
import java.util.Locale;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.wildfire.main.playerData.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.*;

import javax.annotation.Nullable;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	private final TextureAtlas armorTrimAtlas;

	private BreastModelBox lBreast, rBreast;
	private OverlayModelBox lBreastWear, rBreastWear;
	private BreastModelBox lBoobArmor, rBoobArmor;

	private WildfireModelRenderer.HipModelBox lHip, rHip;
	private WildfireModelRenderer.HipModelBox lHipWear, rHipWear;

	private WildfireModelRenderer.HipModelBox lHipArmor, rHipArmor;

	private float preBreastSize = 0f;
	private float preDx = 0f, preDy = 0f;

	private float preHipSize = 0f;
	private float preHipDx = 0f, preHipDy = 0f;

	public GenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> player, ModelManager modelManager) {
		super(player);

		armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);

		lBreast = new BreastModelBox(64, 64, 16, 17, -4F, 0.0F, 0F, 4, 5, 4, 0.0F, false);
		rBreast = new BreastModelBox(64, 64, 20, 17, 0, 0.0F, 0F, 4, 5, 4, 0.0F, false);
		lBreastWear = new OverlayModelBox(true,64, 64, 17, 34, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
		rBreastWear = new OverlayModelBox(false,64, 64, 21, 34, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);

		lBoobArmor = new BreastModelBox(64, 32, 16, 17, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
		rBoobArmor = new BreastModelBox(64, 32, 20, 17, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);

		lHip = new WildfireModelRenderer.HipModelBox(64, 64, 34, 27, -4F, 0, 0, 4, 5, 2, 0.0F, true, 8, false, false);
		rHip = new WildfireModelRenderer.HipModelBox(64, 64, 30, 27, 0, 0, 0, 4, 5, 2, 0.0F, false, 8, false, false);

		lHipWear = new WildfireModelRenderer.HipModelBox(64, 64, 34, 43, -4F, 0, 0, 4, 5, 2, 0.0F, true, 6, false, true);
		rHipWear = new WildfireModelRenderer.HipModelBox(64, 64, 30, 43, 0, 0, 0, 4, 5, 2, 0.0F, false, 6, false, true);

		lHipArmor = new WildfireModelRenderer.HipModelBox(64, 32, 34, 25, -4F, 0, 0, 4, 5, 2, 0.0F, true, 3, false, false);
		rHipArmor = new WildfireModelRenderer.HipModelBox(64, 32, 30, 25, 0, 0, 0, 4, 5, 2, 0.0F, false, 3, false, false);
	}

	//Copy of Forge's patched in HumanoidArmorLayer#getArmorResource but with the removal of the string to rl map lookup
	public ResourceLocation getArmorResource(AbstractClientPlayer entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
		ArmorItem item = (ArmorItem) stack.getItem();
		String texture = item.getMaterial().getName();
		String domain = "minecraft";
		int idx = texture.indexOf(':');
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}
		String s1 = String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture,
			(slot == EquipmentSlot.LEGS ? 2 : 1), type == null ? "" : String.format(Locale.ROOT, "_%s", type));

		s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		return new ResourceLocation(s1);
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource bufferSource, int packedLightIn, @Nonnull AbstractClientPlayer ent, float limbAngle,
		float limbDistance, float partialTicks, float animationProgress, float headYaw, float headPitch) {
		if (GeneralClientConfig.INSTANCE.disableRendering.get() || ent.isSpectator()) {
			//Rendering is disabled client side, or the player is in spectator so only the head will be rendered
			return;
		}
		renderBreasts(matrixStack, bufferSource, packedLightIn, ent, partialTicks);
		renderHips(matrixStack, bufferSource, packedLightIn, ent, partialTicks);
	}

	private void renderBreasts(@NotNull PoseStack matrixStack, @NotNull MultiBufferSource bufferSource, int packedLightIn, @NotNull AbstractClientPlayer ent, float partialTicks) {
		//Surround with a try/catch to fix for essential mod.
		try {
			GenderPlayer plr = WildfireGender.getPlayerById(ent.getUUID());
			if(plr == null) return;

			ItemStack armorStack = ent.getItemBySlot(EquipmentSlot.CHEST);
			//Note: When the stack is empty the helper will fall back to an implementation that returns the proper data
			IGenderArmor genderArmor = WildfireHelper.getArmorConfig(armorStack);
			boolean isChestplateOccupied = genderArmor.coversBreasts();
			if (genderArmor.alwaysHidesBreasts() || !plr.showBreastsInArmor() && isChestplateOccupied) {
				//If the armor always hides breasts or there is armor and the player configured breasts
				// to be hidden when wearing armor, we can just exit early rather than doing any calculations
				return;
			}

			Minecraft minecraft = Minecraft.getInstance();
			PlayerRenderer rend = (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(ent);
			PlayerModel<AbstractClientPlayer> model = rend.getModel();

			Breasts breasts = plr.getBreasts();
			float breastOffsetX = Math.round((Math.round(breasts.getXOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetY = -Math.round((Math.round(breasts.getYOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetZ = -Math.round((Math.round(breasts.getZOffset() * 100f) / 100f) * 10) / 10f;

			BreastPhysics leftBreastPhysics = plr.getLeftBreastPhysics();
			final float bSize = leftBreastPhysics.getBreastSize(partialTicks);
			float outwardAngle = (Math.round(breasts.getCleavage() * 100f) / 100f) * 100f;
			outwardAngle = Math.max(Math.min(outwardAngle, 30), -10);


			float reducer = -2;
			if (bSize < 2.8f) reducer ++;
			if (bSize < 1.4f) reducer ++;
			if (bSize < 0.84f) reducer ++;
			if (bSize < 0.72f) reducer ++;

			float dx = breasts.getDx();
			float dy = breasts.getDy();

			if (preBreastSize != bSize || preDx != dx || preDy != dy) {

				lBreast = new BreastModelBox(64, 64, 16, 17, -4F + (4-dx), 0.0F, 0F, dx, dy, (int) (4 - breastOffsetZ - reducer), 0.0F, false);
				rBreast = new BreastModelBox(64, 64, 20, 17, 0, 0.0F, 0F, dx, dy, (int) (4 - breastOffsetZ - reducer), 0.0F, false);

				lBreastWear = new OverlayModelBox(true,64, 64, 17, 34, -4F + (4-dx), 0.0F, 0F, dx, dy, 3, 0.0F, false);
				rBreastWear = new OverlayModelBox(false,64, 64, 21, 34, 0, 0.0F, 0F, dx, dy, 3, 0.0F, false);

				lBoobArmor = new BreastModelBox(64, 32, 16, 17, -4F + (4-dx), 0.0F, 0F, dx, dy, 3, 0.0F, false);
				rBoobArmor = new BreastModelBox(64, 32, 20, 17, 0, 0.0F, 0F, dx, dy, 3, 0.0F, false);

				preBreastSize = bSize;
				preDx = dx;
				preDy = dy;
			}

			//Note: We only render if the entity is not visible to the player, so we can assume it is visible to the player
			float overlayAlpha = ent.isInvisible() ? 0.15F : 1;

			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

			float lTotal = Mth.lerp(partialTicks, leftBreastPhysics.getPreBounceY(), leftBreastPhysics.getBounceY());
			float lTotalX = Mth.lerp(partialTicks, leftBreastPhysics.getPreBounceX(), leftBreastPhysics.getBounceX());
			float leftBounceRotation = Mth.lerp(partialTicks, leftBreastPhysics.getPreBounceRotation(), leftBreastPhysics.getBounceRotation());
			float rTotal;
			float rTotalX;
			float rightBounceRotation;
			if (breasts.isUniboob()) {
				rTotal = lTotal;
				rTotalX = lTotalX;
				rightBounceRotation = leftBounceRotation;
			} else {
				BreastPhysics rightBreastPhysics = plr.getRightBreastPhysics();
				rTotal = Mth.lerp(partialTicks, rightBreastPhysics.getPreBounceY(), rightBreastPhysics.getBounceY());
				rTotalX = Mth.lerp(partialTicks, rightBreastPhysics.getPreBounceX(), rightBreastPhysics.getBounceX());
				rightBounceRotation = Mth.lerp(partialTicks, rightBreastPhysics.getPreBounceRotation(), rightBreastPhysics.getBounceRotation());
			}
			float breastSize = bSize * 1.5f;
			if (breastSize > 0.7f) breastSize = 0.7f;
			if (bSize > 0.7f) {
				breastSize = bSize;
			}

			if (breastSize < 0.02f) return;

			float zOff = 0.0625f - (bSize * 0.0625f);
			breastSize = bSize + 0.5f * Math.abs(bSize - 0.7f) * 2f;

			//If the armor physics is overridden ignore resistance
			float resistance = plr.getArmorPhysicsOverride() ? 0 : Mth.clamp(genderArmor.physicsResistance(), 0, 1);
			//Note: We only check if the breathing animation should be enabled if the chestplate's physics resistance
			// is less than or equal to 0.5 so that if we won't be rendering it we can avoid doing extra calculations
			boolean breathingAnimation = resistance <= 0.5F &&
										 (!ent.isUnderWater() || MobEffectUtil.hasWaterBreathing(ent) ||
										  ent.level().getBlockState(BlockPos.containing(ent.getX(), ent.getEyeY(), ent.getZ())).is(Blocks.BUBBLE_COLUMN));
			boolean bounceEnabled = plr.hasBreastPhysics() && (!isChestplateOccupied || resistance < 1); //oh, you found this?

			int combineTex = LivingEntityRenderer.getOverlayCoords(ent, 0);
			ResourceLocation entityTexture = ent.getSkinTextureLocation();
			//RenderType selection copied from LivingEntityRenderer#getRenderType
			RenderType type;
			boolean bodyVisible = !ent.isInvisible();
			boolean translucent = !bodyVisible && minecraft.player != null && !ent.isInvisibleTo(minecraft.player);
			if (translucent) {
				type = RenderType.itemEntityTranslucentCull(entityTexture);
			} else if (bodyVisible) {
				type = RenderType.entityTranslucent(entityTexture);
			} else if (minecraft.shouldEntityAppearGlowing(ent)) {
				type = RenderType.outline(entityTexture);
			} else {
				if (!isChestplateOccupied) {
					//Exit early if we don't need to render the breasts, and we don't need to render the armor
					return;
				}
				type = null;
			}
			renderBreastWithTransforms(ent, model.body, armorStack, matrixStack, bufferSource, type, packedLightIn, combineTex, overlayAlpha, bounceEnabled,
				lTotalX, lTotal, leftBounceRotation, breastSize, breastOffsetX, breastOffsetY, breastOffsetZ, zOff, outwardAngle, breasts.isUniboob(),
				isChestplateOccupied, breathingAnimation, true);
			renderBreastWithTransforms(ent, model.body, armorStack, matrixStack, bufferSource, type, packedLightIn, combineTex, overlayAlpha, bounceEnabled,
				rTotalX, rTotal, rightBounceRotation, breastSize, -breastOffsetX, breastOffsetY, breastOffsetZ, zOff, -outwardAngle, breasts.isUniboob(),
				isChestplateOccupied, breathingAnimation, false);
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		} catch(Exception e) {
			WildfireGender.logger.error("Failed to render gender layer", e);
		}
	}

	private void renderHips(@NotNull PoseStack matrixStack, @NotNull MultiBufferSource bufferSource, int packedLightIn, @NotNull AbstractClientPlayer ent, float partialTicks) {
		//Surround with a try/catch to fix for essential mod.
		try {
			GenderPlayer plr = WildfireGender.getPlayerById(ent.getUUID());
			if(plr == null) return;

			ItemStack armorStack = ent.getItemBySlot(EquipmentSlot.CHEST);
			//Note: When the stack is empty the helper will fall back to an implementation that returns the proper data
			IGenderArmor genderArmor = WildfireHelper.getArmorConfig(armorStack);
			boolean isChestplateOccupied = genderArmor.coversBreasts();
			if (genderArmor.alwaysHidesBreasts() || !plr.showBreastsInArmor() && isChestplateOccupied) {
				//If the armor always hides breasts or there is armor and the player configured breasts
				// to be hidden when wearing armor, we can just exit early rather than doing any calculations
				return;
			}

			Minecraft minecraft = Minecraft.getInstance();
			PlayerRenderer rend = (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(ent);
			PlayerModel<AbstractClientPlayer> model = rend.getModel();

			Hips hips = plr.getHips();
			float breastOffsetX = Math.round((Math.round(hips.getXOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetY = -Math.round((Math.round(hips.getYOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetZ = Math.round((Math.round(hips.getZOffset() * 100f) / 100f) * 10) / 10f;

			HipPhysics leftHipPhysics = plr.getLeftHipPhysics();
			final float bSize = leftHipPhysics.getHipSize(partialTicks);
			float outwardAngle = (Math.round(hips.getCleavage() * 100f) / 100f) * 100f;
			outwardAngle = Math.max(Math.min(outwardAngle, 30), -10);


			float reducer = -1;
			if (bSize < 2.8f) reducer ++;
			if (bSize < 1.4f) reducer ++;
			if (bSize < 0.72f) reducer ++;

			float dx = hips.getDx();
			float dy = hips.getDy();

			if (preHipSize != bSize || preHipDx != dx || preHipDy != dy) {

				lHip = new WildfireModelRenderer.HipModelBox(64, 64, 34, 27, -4F + (4-dx), 0, 0, dx, dy, (int) (4 - breastOffsetZ - reducer), 0.0F, true, 8, false, false);
				rHip = new WildfireModelRenderer.HipModelBox(64, 64, 30, 27, 0, 0, 0, dx, dy, (int) (4 - breastOffsetZ - reducer), 0.0F, false, 8, false, false);

				lHipWear = new WildfireModelRenderer.HipModelBox(64, 64, 34, 43, -4F + (4-dx), 0, 0, dx, dy,2, 0.0F, true, 6, false, true);
				rHipWear = new WildfireModelRenderer.HipModelBox(64, 64, 30, 43, 0, 0, 0, dx, dy,2, 0.0F, false, 6, false, true);

				lHipArmor = new WildfireModelRenderer.HipModelBox(64, 32, 34, 25, -4F + (4-dx), 0, 0, dx, dy,2,  0.0F, true, 3, false, false);
				rHipArmor = new WildfireModelRenderer.HipModelBox(64, 32, 30, 25, 0, 0, 0, dx, dy,2, 0.0F, false, 3, false, false);


				preHipSize = bSize;
				preHipDx = dx;
				preHipDy = dy;
			}

			//Note: We only render if the entity is not visible to the player, so we can assume it is visible to the player
			float overlayAlpha = ent.isInvisible() ? 0.15F : 1;

			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

			float lTotal = Mth.lerp(partialTicks, leftHipPhysics.getPreBounceY(), leftHipPhysics.getBounceY());
			float lTotalX = Mth.lerp(partialTicks, leftHipPhysics.getPreBounceX(), leftHipPhysics.getBounceX());
			float leftBounceRotation = Mth.lerp(partialTicks, leftHipPhysics.getPreBounceRotation(), leftHipPhysics.getBounceRotation());
			float rTotal;
			float rTotalX;
			float rightBounceRotation;
			if (hips.isUniHips()) {
				rTotal = lTotal;
				rTotalX = lTotalX;
				rightBounceRotation = leftBounceRotation;
			} else {
				HipPhysics rightHipPhysics = plr.getRightHipPhysics();
				rTotal = Mth.lerp(partialTicks, rightHipPhysics.getPreBounceY(), rightHipPhysics.getBounceY());
				rTotalX = Mth.lerp(partialTicks, rightHipPhysics.getPreBounceX(), rightHipPhysics.getBounceX());
				rightBounceRotation = Mth.lerp(partialTicks, rightHipPhysics.getPreBounceRotation(), rightHipPhysics.getBounceRotation());
			}
			float HipSize = bSize * 1.5f;
			if (HipSize > 0.7f) HipSize = 0.7f;
			if (bSize > 0.7f) {
				HipSize = bSize;
			}

			if (HipSize < 0.02f) return;

			float zOff = 0.0625f + (bSize * 0.0625f) * 0;
			HipSize = bSize + 0.5f * Math.abs(bSize - 0.7f) * 2f;

			//If the armor physics is overridden ignore resistance
			float resistance = plr.getArmorPhysicsOverride() ? 0 : Mth.clamp(genderArmor.physicsResistance(), 0, 1);
			//Note: We only check if the breathing animation should be enabled if the chestplate's physics resistance
			// is less than or equal to 0.5 so that if we won't be rendering it we can avoid doing extra calculations
			boolean breathingAnimation = resistance <= 0.5F &&
					(!ent.isUnderWater() || MobEffectUtil.hasWaterBreathing(ent) ||
							ent.level().getBlockState(BlockPos.containing(ent.getX(), ent.getEyeY(), ent.getZ())).is(Blocks.BUBBLE_COLUMN));
			boolean bounceEnabled = plr.hasBreastPhysics() && (!isChestplateOccupied || resistance < 1); //oh, you found this?

			int combineTex = LivingEntityRenderer.getOverlayCoords(ent, 0);
			ResourceLocation entityTexture = ent.getSkinTextureLocation();
			//RenderType selection copied from LivingEntityRenderer#getRenderType
			RenderType type;
			boolean bodyVisible = !ent.isInvisible();
			boolean translucent = !bodyVisible && minecraft.player != null && !ent.isInvisibleTo(minecraft.player);
			if (translucent) {
				type = RenderType.itemEntityTranslucentCull(entityTexture);
			} else if (bodyVisible) {
				type = RenderType.entityTranslucent(entityTexture);
			} else if (minecraft.shouldEntityAppearGlowing(ent)) {
				type = RenderType.outline(entityTexture);
			} else {
				if (!isChestplateOccupied) {
					//Exit early if we don't need to render the breasts, and we don't need to render the armor
					return;
				}
				type = null;
			}

			renderHipWithTransforms(ent, model.body, armorStack, matrixStack, bufferSource, type, packedLightIn, combineTex, overlayAlpha, bounceEnabled,
					lTotalX, lTotal, leftBounceRotation, HipSize, breastOffsetX, breastOffsetY, breastOffsetZ, zOff, outwardAngle, hips.isUniHips(),
					isChestplateOccupied, breathingAnimation, true);
			renderHipWithTransforms(ent, model.body, armorStack, matrixStack, bufferSource, type, packedLightIn, combineTex, overlayAlpha, bounceEnabled,
					rTotalX, rTotal, rightBounceRotation, HipSize, -breastOffsetX, breastOffsetY, breastOffsetZ, zOff, -outwardAngle, hips.isUniHips(),
					isChestplateOccupied, breathingAnimation, false);

			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		} catch(Exception e) {
			WildfireGender.logger.error("Failed to render gender layer", e);
		}
	}

	private void renderBreastWithTransforms(AbstractClientPlayer entity, ModelPart body, ItemStack armorStack, PoseStack matrixStack, MultiBufferSource bufferSource,
		@Nullable RenderType breastRenderType, int packedLightIn, int combineTex, float alpha, boolean bounceEnabled, float totalX, float total, float bounceRotation,
		float breastSize, float breastOffsetX, float breastOffsetY, float breastOffsetZ, float zOff, float outwardAngle, boolean uniboob, boolean isChestplateOccupied,
		boolean breathingAnimation, boolean left) {
		matrixStack.pushPose();
		//Surround with a try/catch to fix for essential mod.
		try {
			matrixStack.translate(body.x * 0.0625f, body.y * 0.0625f, body.z * 0.0625f); // 0.0625f = 1/16f body.y * 0.0625f + body.yScale/2f,  + body.zScale/2

			if (body.zRot != 0.0F) {
				matrixStack.mulPose(new Quaternionf().rotationXYZ(0f, 0f, body.zRot));
			}
			if (body.yRot != 0.0F) {
				matrixStack.mulPose(new Quaternionf().rotationXYZ(0f, body.yRot, 0f));
			}
			if (body.xRot != 0.0F) {
				matrixStack.mulPose(new Quaternionf().rotationXYZ(body.xRot, 0f, 0f));
			}

			// matrixStack.mulPose(new Quaternionf().rotateY((float) Math.PI));
			// matrixStack.translate(0, body.yScale/2f,  + body.zScale/4);

			if (bounceEnabled) {
				matrixStack.translate(totalX / 32f, 0, 0);
				matrixStack.translate(0, total / 32f, 0);
			}

			matrixStack.translate(breastOffsetX * 0.0625f, 0.05625f + (breastOffsetY * 0.0625f), zOff - 0.0625f * 2f + (breastOffsetZ * 0.0625f)); //shift down to correct position

			if (!uniboob) {
				matrixStack.translate(-0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}
			if (bounceEnabled) {
				matrixStack.mulPose(new Quaternionf().rotationXYZ(0, (float) (bounceRotation * (Math.PI / 180f)), 0));
			}
			if (!uniboob) {
				matrixStack.translate(0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}

			float rotationMultiplier = 0;
			if (bounceEnabled) {
				matrixStack.translate(0, -0.035f * breastSize, 0); //shift down to correct position
				rotationMultiplier = -total / 12f;
			}
			float totalRotation = breastSize;
			if (!bounceEnabled) {
				totalRotation = breastSize;
			}
			if (totalRotation > breastSize + 0.2F) {
				totalRotation = breastSize + 0.2F;
			}
			totalRotation = Math.min(totalRotation, 1); //hard limit for MAX
			totalRotation += rotationMultiplier;

			if (isChestplateOccupied) {
				matrixStack.translate(0, 0, 0.01f);
			}

			matrixStack.mulPose(new Quaternionf().rotationXYZ(0, (float) (outwardAngle * (Math.PI / 180f)), 0));
			matrixStack.mulPose(new Quaternionf().rotationXYZ((float) (-35f * totalRotation * (Math.PI / 180f)), 0, 0));

			if (breathingAnimation) {
				float f5 = -Mth.cos(entity.tickCount * 0.09F) * 0.45F + 0.45F;
				matrixStack.mulPose(new Quaternionf().rotationXYZ((float) (f5 * (Math.PI / 180f)), 0, 0));
			}

			matrixStack.scale(0.9995f, 1f, 1f); //z-fighting FIXXX

			renderBreast(entity, armorStack, matrixStack, bufferSource, breastRenderType, packedLightIn, combineTex, alpha, left);
		} catch(Exception e) {
			WildfireGender.logger.error("Failed to render breast", e);
		}
		matrixStack.popPose();
	}
	private void renderHipWithTransforms(AbstractClientPlayer entity, ModelPart body, ItemStack armorStack, PoseStack matrixStack, MultiBufferSource bufferSource,
											@Nullable RenderType breastRenderType, int packedLightIn, int combineTex, float alpha, boolean bounceEnabled, float totalX, float total, float bounceRotation,
											float breastSize, float breastOffsetX, float breastOffsetY, float breastOffsetZ, float zOff, float outwardAngle, boolean uniboob, boolean isChestplateOccupied,
											boolean breathingAnimation, boolean left) {
		matrixStack.pushPose();
		//Surround with a try/catch to fix for essential mod.
		try {
			matrixStack.translate(body.x * 0.0625f, body.y * 0.0625f, body.z * 0.0625f); // 0.0625f = 1/16f body.y * 0.0625f + body.yScale/2f,  + body.zScale/2

			if (body.zRot != 0.0F) {
				matrixStack.mulPose(new Quaternionf().rotationXYZ(0f, 0f, body.zRot));
			}

			if (body.yRot != 0.0F) {
				matrixStack.mulPose(new Quaternionf().rotationXYZ(0f, body.yRot, 0f));
			}
			if (body.xRot != 0.0F){
				matrixStack.mulPose(new Quaternionf().rotationXYZ(body.xRot, 0f, 0f));
			}
			matrixStack.translate(0, body.yScale/2f,  + body.zScale/4f);



			if (bounceEnabled) {
				matrixStack.translate(totalX / 32f, 0, 0);
				matrixStack.translate(0, total / 32f, 0);
			}

			matrixStack.translate(breastOffsetX * 0.0625f, 0.05625f + (breastOffsetY * 0.0625f), zOff - 0.0625f * 2f + (breastOffsetZ * 0.0625f)); //shift down to correct position

			if (!uniboob) {
				matrixStack.translate(-0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}
			if (bounceEnabled) {
				matrixStack.mulPose(new Quaternionf().rotationXYZ(0, (float) (bounceRotation * (Math.PI / 180f)), 0));
			}
			if (!uniboob) {
				matrixStack.translate(0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}

			float rotationMultiplier = 0;
			if (bounceEnabled) {
				matrixStack.translate(0, -0.035f * breastSize, 0); //shift down to correct position
				rotationMultiplier = -total / 12f;
			}
			float totalRotation = breastSize;
			if (!bounceEnabled) {
				totalRotation = breastSize;
			}
			if (totalRotation > breastSize + 0.2F) {
				totalRotation = breastSize + 0.2F;
			}
			totalRotation = Math.min(totalRotation, 1); //hard limit for MAX
			totalRotation += rotationMultiplier;

			if (isChestplateOccupied) {
				matrixStack.translate(0, 0, 0.01f);
			}

			matrixStack.mulPose(new Quaternionf().rotationXYZ(0, (float) (outwardAngle * (Math.PI / 180f)), 0));
			matrixStack.mulPose(new Quaternionf().rotationXYZ((float) (35f * totalRotation * (Math.PI / 180f)), 0, 0));

			if (breathingAnimation) {
				float f5 = -Mth.cos(entity.tickCount * 0.09F) * 0.45F + 0.45F;
				matrixStack.mulPose(new Quaternionf().rotationXYZ((float) (f5 * (Math.PI / 180f)), 0, 0));
			}

			matrixStack.scale(0.9995f, 1f, 1f); //z-fighting FIXXX

			renderHip(entity, armorStack, matrixStack, bufferSource, breastRenderType, packedLightIn, combineTex, alpha, left);
		} catch(Exception e) {
			WildfireGender.logger.error("Failed to render breast", e);
		}
		matrixStack.popPose();
	}

	private void renderBreast(AbstractClientPlayer entity, ItemStack armorStack, PoseStack matrixStack, MultiBufferSource bufferSource,
		@Nullable RenderType breastRenderType, int packedLightIn, int packedOverlayIn, float alpha, boolean left) {
		if (breastRenderType != null) {
			//Only render the breasts if we have a render type for them
			VertexConsumer vertexConsumer = bufferSource.getBuffer(breastRenderType);
			renderBox(left ? lBreast : rBreast, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, 1F, 1F, 1F, alpha);
			if (entity.isModelPartShown(PlayerModelPart.JACKET)) {
				matrixStack.translate(0, 0, -0.015f);
				matrixStack.scale(1.05f, 1.05f, 1.05f);
				renderBox(left ? lBreastWear : rBreastWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, 1F, 1F, 1F, alpha);
			}
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) { // armor
			ResourceLocation armorTexture = getArmorResource(entity, armorStack, EquipmentSlot.CHEST, null);
			ResourceLocation overlayTexture = null;
			float armorR = 1f;
			float armorG = 1f;
			float armorB = 1f;
			if (armorItem instanceof DyeableLeatherItem dyeableItem) {
				overlayTexture = getArmorResource(entity, armorStack, EquipmentSlot.CHEST, "overlay");
				int color = dyeableItem.getColor(armorStack);
				armorR = (float) (color >> 16 & 255) / 255.0F;
				armorG = (float) (color >> 8 & 255) / 255.0F;
				armorB = (float) (color & 255) / 255.0F;
			}
			matrixStack.pushPose();
			matrixStack.translate(left ? 0.001f : -0.001f, 0.015f, -0.015f);
			matrixStack.scale(1.05f, 1, 1);
			WildfireModelRenderer.BreastModelBox armor = left ? lBoobArmor : rBoobArmor;
			RenderType armorType = RenderType.armorCutoutNoCull(armorTexture);
			VertexConsumer armorVertexConsumer = bufferSource.getBuffer(armorType);
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, armorR, armorG, armorB, 1);
			if (overlayTexture != null) {
				RenderType overlayType = RenderType.armorCutoutNoCull(overlayTexture);
				VertexConsumer overlayVertexConsumer = bufferSource.getBuffer(overlayType);
				renderBox(armor, matrixStack, overlayVertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
			ArmorTrim.getTrim(entity.level().registryAccess(), armorStack).ifPresent(trim -> {
				ArmorMaterial armorMaterial = armorItem.getMaterial();
				TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(trim.outerTexture(armorMaterial));
				VertexConsumer trimVertexConsumer = sprite.wrap(bufferSource.getBuffer(Sheets.armorTrimsSheet()));
				renderBox(armor, matrixStack, trimVertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			});

			if (armorStack.hasFoil()) {
				renderBox(armor, matrixStack, bufferSource.getBuffer(RenderType.armorEntityGlint()), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}

			matrixStack.popPose();
		}
	}

	private void renderHip(AbstractClientPlayer entity, ItemStack armorStack, PoseStack matrixStack, MultiBufferSource bufferSource,
							  @Nullable RenderType breastRenderType, int packedLightIn, int packedOverlayIn, float alpha, boolean left) {
		if (breastRenderType != null) {
			//Only render the breasts if we have a render type for them
			VertexConsumer vertexConsumer = bufferSource.getBuffer(breastRenderType);
			renderBox(left ? lHip : rHip, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, 1F, 1F, 1F, alpha);
			if (entity.isModelPartShown(PlayerModelPart.JACKET)) {
				matrixStack.translate(0, 0, 0.015f);
				matrixStack.scale(1.05f, 1.05f, 1.05f);
				renderBox(left ? lHipWear : rHipWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, 1F, 1F, 1F, alpha);
			}
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) { // armor
			ResourceLocation armorTexture = getArmorResource(entity, armorStack, EquipmentSlot.CHEST, null);
			ResourceLocation overlayTexture = null;
			float armorR = 1f;
			float armorG = 1f;
			float armorB = 1f;
			if (armorItem instanceof DyeableLeatherItem dyeableItem) {
				overlayTexture = getArmorResource(entity, armorStack, EquipmentSlot.CHEST, "overlay");
				int color = dyeableItem.getColor(armorStack);
				armorR = (float) (color >> 16 & 255) / 255.0F;
				armorG = (float) (color >> 8 & 255) / 255.0F;
				armorB = (float) (color & 255) / 255.0F;
			}
			matrixStack.pushPose();
			matrixStack.translate(left ? 0.001f : -0.001f, 0.015f, 0.015f);
			matrixStack.scale(1.06f, 1.06f, 1.06f);
			WildfireModelRenderer.HipModelBox armor = left ? lHipArmor : rHipArmor;
			RenderType armorType = RenderType.armorCutoutNoCull(armorTexture);
			VertexConsumer armorVertexConsumer = bufferSource.getBuffer(armorType);
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, armorR, armorG, armorB, 1);
			if (overlayTexture != null) {
				RenderType overlayType = RenderType.armorCutoutNoCull(overlayTexture);
				VertexConsumer overlayVertexConsumer = bufferSource.getBuffer(overlayType);
				renderBox(armor, matrixStack, overlayVertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
			ArmorTrim.getTrim(entity.level().registryAccess(), armorStack).ifPresent(trim -> {
				ArmorMaterial armorMaterial = armorItem.getMaterial();
				TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(trim.outerTexture(armorMaterial));
				VertexConsumer trimVertexConsumer = sprite.wrap(bufferSource.getBuffer(Sheets.armorTrimsSheet()));
				renderBox(armor, matrixStack, trimVertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			});

			if (armorStack.hasFoil()) {
				renderBox(armor, matrixStack, bufferSource.getBuffer(RenderType.armorEntityGlint()), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}

			matrixStack.popPose();
		}
	}

	private static void renderBox(WildfireModelRenderer.ModelBox model, PoseStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
		float red, float green, float blue, float alpha) {
		Matrix4f matrix4f = matrixStack.last().pose();
		Matrix3f matrix3f =	matrixStack.last().normal();
		for (WildfireModelRenderer.TexturedQuad quad : model.quads) {
			Vector3f vector3f = new Vector3f(quad.normal.getX(), quad.normal.getY(), quad.normal.getZ());
			vector3f.mul(matrix3f);
			for (PositionTextureVertex vertex : quad.vertexPositions) {
				//TODO - 1.20: Switch back to chaining https://github.com/MinecraftForge/MinecraftForge/pull/9564
				bufferIn.vertex(matrix4f, vertex.x() / 16.0F, vertex.y() / 16.0F, vertex.z() / 16.0F);
				bufferIn.color(red, green, blue, alpha);
				bufferIn.uv(vertex.texturePositionX(), vertex.texturePositionY());
				bufferIn.overlayCoords(packedOverlayIn);
				bufferIn.uv2(packedLightIn);
				bufferIn.normal(vector3f.x(), vector3f.y(), vector3f.z());
				bufferIn.endVertex();
			}
		}
	}

	private static void renderBox(WildfireModelRenderer.HipModelBox model, PoseStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
								  float red, float green, float blue, float alpha) {
		Matrix4f matrix4f = matrixStack.last().pose();
		Matrix3f matrix3f =	matrixStack.last().normal();
		for (WildfireModelRenderer.TexturedQuad quad : model.quads) {
			Vector3f vector3f = new Vector3f(quad.normal.getX(), quad.normal.getY(), quad.normal.getZ());
			vector3f.mul(matrix3f);
			for (PositionTextureVertex vertex : quad.vertexPositions) {
				//TODO - 1.20: Switch back to chaining https://github.com/MinecraftForge/MinecraftForge/pull/9564
				bufferIn.vertex(matrix4f, vertex.x() / 16.0F, vertex.y() / 16.0F, vertex.z() / 16.0F);
				bufferIn.color(red, green, blue, alpha);
				bufferIn.uv(vertex.texturePositionX(), vertex.texturePositionY());
				bufferIn.overlayCoords(packedOverlayIn);
				bufferIn.uv2(packedLightIn);
				bufferIn.normal(vector3f.x(), vector3f.y(), vector3f.z());
				bufferIn.endVertex();
			}
		}
	}
}
