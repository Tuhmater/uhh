package com.godgun.event;

import com.godgun.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {

    /**
     * Client-side fallback: if the server doesn't have this mod installed,
     * we still rapid-fire vanilla attack packets at every entity in FOV.
     * Damage is limited to vanilla values without the server mod, but on
     * YOUR server (with the mod installed) the server handler takes over
     * for Float.MAX_VALUE damage.
     *
     * This also ensures the held-item animation plays correctly.
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;
        if (mc.isPaused()) return;

        // Check right mouse is held
        if (!mc.options.keyUse.isDown()) return;

        // Must be holding God Gun (or a renamed item with display name "GOD GUN" for vanilla servers)
        ItemStack held = mc.player.getMainHandItem();
        boolean isGodGun = held.getItem() == ModItems.GOD_GUN.get()
                || (held.hasCustomHoverName()
                    && held.getHoverName().getString().equalsIgnoreCase("GOD GUN"));

        if (!isGodGun) return;

        Vec3 eyePos  = mc.player.getEyePosition();
        Vec3 lookVec = mc.player.getLookAngle();

        // Iterate all rendered entities and attack those in FOV
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity le)) continue;
            if (le == mc.player || !le.isAlive()) continue;

            Vec3 toTarget = entity.getEyePosition().subtract(eyePos).normalize();
            double dot = toTarget.dot(lookVec);

            if (dot > 0.60) {
                // Sends ServerboundInteractPacket — works on ANY server
                mc.gameMode.attack(mc.player, entity);
            }
        }
    }
}
