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
    -added commands to modify gender settings
*/

package com.wildfire.main.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.networking.WildfireSync;
import com.wildfire.main.playerData.GenderPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = WildfireGender.MODID)
public class GenderCommand {

    private static final SuggestionProvider<CommandSourceStack> ALL_KEY_SUGGESTIONS = ((context, builder) ->
            SharedSuggestionProvider.suggest(new String[]{
                        "bust_size", "breasts_dx", "breasts_dy", "breasts_xOffset", "breasts_yOffset", "breasts_zOffset",
                        "breasts_cleavage", "hips_size", "hips_dx", "hips_dy", "hips_xOffset", "hips_yOffset", "hips_zOffset",
                        "hips_cleavage", "bounce_multiplier", "floppy_multiplier",
                        "breast_physics", "hurt_sounds", "armor_physics_override", "show_in_armor",
                        "breasts_uniboob", "hips_uniboob","gender"
                    }, builder));
    private static Object parseValue(String key, String value) {
        try {
            if (isFloatKey(key)) {
                return Float.parseFloat(value);
            } else if (isBooleanKey(key)) {
                if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                    return Boolean.parseBoolean(value);
                }
            } else {
                return value;
            }
        } catch (NumberFormatException ignored) {}
        return null;
    }

    // 各型の key 判定
    private static boolean isFloatKey(String key) {
        return key.matches("bust_size|breasts_dx|breasts_dy|breasts_xOffset|breasts_yOffset|breasts_zOffset|" +
                "breasts_cleavage|hips_size|hips_dx|hips_dy|hips_xOffset|hips_yOffset|hips_zOffset|" +
                "hips_cleavage|bounce_multiplier|floppy_multiplier");
    }

    private static boolean isBooleanKey(String key) {
        return key.matches("breast_physics|hurt_sounds|armor_physics_override|show_in_armor|breasts_uniboob|hips_uniboob");
    }

    private static boolean isStringKey(String key) {
        return key.equalsIgnoreCase("gender");
    }

    public static Map<String, Object> getGenderAttributes(GenderPlayer genderPlayer) {
        if (genderPlayer == null) return new HashMap<>();

        Map<String, Object> attributes = new HashMap<>();

        // Float 型のデータ
        attributes.put("bust_size", genderPlayer.getBustSize());
        attributes.put("breasts_dx", genderPlayer.getBreasts().getDx());
        attributes.put("breasts_dy", genderPlayer.getBreasts().getDy());
        attributes.put("breasts_xOffset", genderPlayer.getBreasts().getXOffset());
        attributes.put("breasts_yOffset", genderPlayer.getBreasts().getYOffset());
        attributes.put("breasts_zOffset", genderPlayer.getBreasts().getZOffset());
        attributes.put("breasts_cleavage", genderPlayer.getBreasts().getCleavage());

        attributes.put("hips_size", genderPlayer.getHipSize());
        attributes.put("hips_dx", genderPlayer.getHips().getDx());
        attributes.put("hips_dy", genderPlayer.getHips().getDy());
        attributes.put("hips_xOffset", genderPlayer.getHips().getXOffset());
        attributes.put("hips_yOffset", genderPlayer.getHips().getYOffset());
        attributes.put("hips_zOffset", genderPlayer.getHips().getZOffset());
        attributes.put("hips_cleavage", genderPlayer.getHips().getCleavage());

        attributes.put("bounce_multiplier", genderPlayer.getBounceMultiplier());
        attributes.put("floppy_multiplier", genderPlayer.getFloppiness());

        // Boolean 型のデータ
        attributes.put("breast_physics", genderPlayer.hasBreastPhysics());
        attributes.put("hurt_sounds", genderPlayer.hasHurtSounds());
        attributes.put("armor_physics_override", genderPlayer.getArmorPhysicsOverride());
        attributes.put("show_in_armor", genderPlayer.showBreastsInArmor());
        attributes.put("breasts_uniboob", genderPlayer.getBreasts().isUniboob());
        attributes.put("hips_uniboob", genderPlayer.getHips().isUniHips());

        // String 型のデータ (Gender)
        attributes.put("gender", genderPlayer.getGender().toString());

        return attributes;
    }


    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("gender")
                .requires(player -> player.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("key", StringArgumentType.word())
                                .suggests(ALL_KEY_SUGGESTIONS)
                                    .then(Commands.argument("value", StringArgumentType.word())
                                            .suggests((context, builder) -> {
                                                String key = StringArgumentType.getString(context, "key");
                                                if (isBooleanKey(key)) {
                                                    return SharedSuggestionProvider.suggest(new String[]{"true", "false"}, builder);
                                                } else if (isStringKey(key)) {
                                                    return SharedSuggestionProvider.suggest(new String[]{"male", "female", "other"}, builder);
                                                }
                                                return builder.buildFuture();
                                            })
                                            .executes(context -> {
                                                String key = StringArgumentType.getString(context, "key");
                                                String valueString = StringArgumentType.getString(context, "value");

                                                Object value = parseValue(key, valueString);
                                                if (value == null) {
                                                    context.getSource().sendFailure(Component.literal(
                                                            ChatFormatting.RED + "Invalid value for " + key + ": " + valueString));
                                                    return 0;
                                                }

                                                for (Player player : EntityArgument.getPlayers(context, "targets")) {
                                                    if (value instanceof Float floatValue) {
                                                        valueString = String.valueOf(updateGenderInfo(player, key, floatValue));
                                                    } else if (value instanceof Boolean booleanValue) {
                                                        updateGenderInfo(player, key, booleanValue);
                                                    } else {
                                                        if(!valueString.equalsIgnoreCase("male") && !valueString.equalsIgnoreCase("female")){
                                                            valueString = "other";
                                                        }
                                                        updateGenderInfo(player, key, value.toString());
                                                    }
                                                }

                                                context.getSource().sendSystemMessage(Component.literal(
                                                        ChatFormatting.GREEN + "Updated " + key + " to " + ChatFormatting.AQUA + valueString));
                                                return Command.SINGLE_SUCCESS;
                                            })
                                    )
                            )
                            .executes(context -> {
                                Collection<ServerPlayer> targetPlayers = EntityArgument.getPlayers(context, "targets");

                                for (ServerPlayer player : targetPlayers) {
                                    GenderPlayer genderPlayer = WildfireGender.getOrAddPlayerById(player.getUUID());
                                    if (genderPlayer == null) {
                                        context.getSource().sendFailure(Component.literal(
                                                ChatFormatting.RED + "Failed to retrieve gender data for " + player.getName().getString()));
                                        continue;
                                    }

                                    Map<String, Object> attributes = getGenderAttributes(genderPlayer);

                                    StringBuilder message = new StringBuilder(ChatFormatting.AQUA + player.getName().getString() + ChatFormatting.WHITE +"'s Gender Attributes:");
                                    for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                                        message.append("\n").append(ChatFormatting.GREEN).append(entry.getKey()).append(": ")
                                                .append(ChatFormatting.AQUA).append(entry.getValue());
                                    }
                                    context.getSource().sendSystemMessage(Component.literal(message.toString()));
                                    return Command.SINGLE_SUCCESS;
                                }
                                return Command.SINGLE_SUCCESS;
                            })
                    )
                    .then(Commands.argument("targetPlayer", EntityArgument.players())
                            .then(Commands.literal("copyFrom")
                                    .then(Commands.argument("sourcePlayer", EntityArgument.player())
                                            .executes(context -> {
                                                ServerPlayer sourcePlayer = EntityArgument.getPlayer(context, "sourcePlayer");
                                                Collection<ServerPlayer> targetPlayers = EntityArgument.getPlayers(context, "targetPlayer");
                                                return copyGenderSettings(context.getSource(), sourcePlayer, targetPlayers);
                                            })
                                    )
                            )
                    )
        );
    }

    private static int copyGenderSettings(CommandSourceStack source, ServerPlayer sourcePlayer, Collection<ServerPlayer> targetPlayers) {
        // PlayerAugment を使ってデータ取得
        GenderPlayer sourceGender = WildfireGender.getOrAddPlayerById(sourcePlayer.getUUID());
        for(ServerPlayer targetPlayer : targetPlayers){
            GenderPlayer targetGender = WildfireGender.getOrAddPlayerById(targetPlayer.getUUID());

            if (sourceGender != null && targetGender != null) {
                targetGender.updateBustSize(sourceGender.getBustSize());
                targetGender.getBreasts().updateDx(sourceGender.getBreasts().getDx());
                targetGender.getBreasts().updateDy(sourceGender.getBreasts().getDy());
                targetGender.getBreasts().updateXOffset(sourceGender.getBreasts().getXOffset());
                targetGender.getBreasts().updateYOffset(sourceGender.getBreasts().getYOffset());
                targetGender.getBreasts().updateZOffset(sourceGender.getBreasts().getZOffset());
                targetGender.getBreasts().updateCleavage(sourceGender.getBreasts().getCleavage());
                targetGender.getBreasts().updateUniboob(sourceGender.getBreasts().isUniboob());

                targetGender.updateHipSize(sourceGender.getHipSize());
                targetGender.getHips().updateDx(sourceGender.getHips().getDx());
                targetGender.getHips().updateDy(sourceGender.getHips().getDy());
                targetGender.getHips().updateXOffset(sourceGender.getHips().getXOffset());
                targetGender.getHips().updateYOffset(sourceGender.getHips().getYOffset());
                targetGender.getHips().updateZOffset(sourceGender.getHips().getZOffset());
                targetGender.getHips().updateCleavage(sourceGender.getHips().getCleavage());
                targetGender.getHips().updateUniHips(sourceGender.getHips().isUniHips());

                targetGender.updateBounceMultiplier(sourceGender.getBounceMultiplier());
                targetGender.updateFloppiness(sourceGender.getFloppiness());

                targetGender.updateBreastPhysics(sourceGender.hasBreastPhysics());
                targetGender.updateHurtSounds(sourceGender.hasHurtSounds());
                targetGender.updateArmorPhysicsOverride(sourceGender.getArmorPhysicsOverride());
                targetGender.updateShowBreastsInArmor(sourceGender.showBreastsInArmor());
                targetGender.updateGender(sourceGender.getGender());

                WildfireSync.setClient(targetPlayer, targetGender);
            }
        }

        source.sendSuccess(()->Component.literal(ChatFormatting.GREEN+"Copied gender settings from " + ChatFormatting.AQUA + sourcePlayer.getName().getString() + ChatFormatting.GREEN + " to Player(s) " + targetPlayers.size()), true);
        return Command.SINGLE_SUCCESS;
    }


    private static float updateGenderInfo(Player player, String key, float value) {
        GenderPlayer genderPlayer = WildfireGender.getOrAddPlayerById(player.getUUID());
        if (genderPlayer == null) return Float.MIN_VALUE;

        value = switch (key) {
            case "bust_size", "hips_size" -> clamp(value, 0, 3.2f);
            case "breasts_dx", "breasts_dy", "hips_dx", "hips_dy" -> clamp(value, 2.5f, 6.0f);
            case "breasts_xOffset", "breasts_yOffset", "hips_xOffset" -> clamp(value, -1, 1);
            case "breasts_zOffset", "hips_zOffset" -> clamp(value, -2, 0);
            case "breasts_cleavage", "hips_cleavage" -> clamp(value, -0.1f, 0.3f);
            case "hips_yOffset" -> clamp(value, -1, 2);
            case "bounce_multiplier" -> clamp(value, 0, 0.5f);
            case "floppy_multiplier" -> clamp(value, 0.25f, 1f);
            default -> Float.MIN_VALUE;
        };

        switch (key) {
            case "bust_size" -> genderPlayer.updateBustSize(value);
            case "breasts_dx" -> genderPlayer.getBreasts().updateDx(value);
            case "breasts_dy" -> genderPlayer.getBreasts().updateDy(value);
            case "breasts_xOffset" -> genderPlayer.getBreasts().updateXOffset(value);
            case "breasts_yOffset" -> genderPlayer.getBreasts().updateYOffset(value);
            case "breasts_zOffset" -> genderPlayer.getBreasts().updateZOffset(value);
            case "breasts_cleavage" -> genderPlayer.getBreasts().updateCleavage(value);
            case "hips_size" -> genderPlayer.updateHipSize(value);
            case "hips_dx" -> genderPlayer.getHips().updateDx(value);
            case "hips_dy" -> genderPlayer.getHips().updateDy(value);
            case "hips_xOffset" -> genderPlayer.getHips().updateXOffset(value);
            case "hips_yOffset" -> genderPlayer.getHips().updateYOffset(value);
            case "hips_zOffset" -> genderPlayer.getHips().updateZOffset(value);
            case "hips_cleavage" -> genderPlayer.getHips().updateCleavage(value);
            case "bounce_multiplier" -> genderPlayer.updateBounceMultiplier(value);
            case "floppy_multiplier" -> genderPlayer.updateFloppiness(value);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            WildfireSync.setClient(serverPlayer, genderPlayer);
        }
        return value;
    }


    private static void updateGenderInfo(Player player, String key, boolean value) {
        GenderPlayer genderPlayer = WildfireGender.getOrAddPlayerById(player.getUUID());
        if (genderPlayer == null) return;

        switch (key) {
            case "breast_physics" -> genderPlayer.updateBreastPhysics(value);
            case "hurt_sounds" -> genderPlayer.updateHurtSounds(value);
            case "armor_physics_override" -> genderPlayer.updateArmorPhysicsOverride(value);
            case "show_in_armor" -> genderPlayer.updateShowBreastsInArmor(value);
            case "breasts_uniboob" -> genderPlayer.getBreasts().updateUniboob(value);
            case "hips_uniboob" -> genderPlayer.getHips().updateUniHips(value);
            default -> {
                return;
            }
        }
        if(player instanceof ServerPlayer serverPlayer) WildfireSync.setClient(serverPlayer, genderPlayer);

    }

    private static void updateGenderInfo(Player player, String key, String value) {
        GenderPlayer genderPlayer = WildfireGender.getOrAddPlayerById(player.getUUID());
        if (genderPlayer == null) return;

        if ("gender".equals(key)) {
            if ("male".equalsIgnoreCase(value)){
                genderPlayer.updateGender(GenderPlayer.Gender.MALE);
            }else if("female".equalsIgnoreCase(value)) {
                genderPlayer.updateGender(GenderPlayer.Gender.FEMALE);
            }else{
                genderPlayer.updateGender(GenderPlayer.Gender.OTHER);
            }
            if(player instanceof ServerPlayer serverPlayer) WildfireSync.setClient(serverPlayer, genderPlayer);

        }
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
