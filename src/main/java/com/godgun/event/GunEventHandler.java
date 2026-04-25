package com.godgun.event;

import com.godgun.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class GunEventHandler {

    // Fires every single server tick while right-click is held — full auto
    @SubscribeEvent
    public void onItemUseTick(LivingEntityUseItemEvent.Tick event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getItem().getItem() == ModItems.GOD_GUN.get())) return;

        fireAtEntitiesInFOV(player);
    }

    private void fireAtEntitiesInFOV(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        Vec3 eyePos   = player.getEyePosition();
        Vec3 lookVec  = player.getLookAngle();

        // Search box: 150-block radius around player
        AABB searchBox = player.getBoundingBox().inflate(150, 150, 150);

        List<Entity> targets = level.getEntities(player, searchBox,
                e -> e instanceof LivingEntity le
                        && le.isAlive()
                        && e != player);

        for (Entity target : targets) {
            Vec3 toTarget = target.getEyePosition().subtract(eyePos).normalize();
            double dot = toTarget.dot(lookVec);

            // dot > 0.60 ≈ within ~53° half-angle — wide FOV cone
            if (dot > 0.60) {
                DamageSource dmgSource = level.damageSources().playerAttack(player);
                // Tag the kill so we can intercept the death message
                target.getPersistentData().putString("god_gun_killer", player.getName().getString());
                target.hurt(dmgSource, Float.MAX_VALUE);
            }
        }
    }

    // ── Death message override ────────────────────────────────────────────────
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;

        // Was this a God Gun kill?
        String killerName = victim.getPersistentData().getString("god_gun_killer");
        if (killerName == null || killerName.isEmpty()) return;

        // Victim display name
        String victimName = victim.getName().getString();

        // Broadcast custom death message to everyone on the server
        Component deathMsg = Component.literal(
                "§4§l" + victimName + "§r §cwas obliterated off of the face of the earth by §4§l" + killerName
        );

        if (victim.level() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getPlayerList().broadcastSystemMessage(deathMsg, false);
        }
    }
}
