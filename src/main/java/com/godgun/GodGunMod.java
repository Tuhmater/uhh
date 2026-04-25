package com.godgun;

import com.godgun.command.GodGunCommand;
import com.godgun.event.ClientEventHandler;
import com.godgun.event.GunEventHandler;
import com.godgun.item.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(GodGunMod.MODID)
public class GodGunMod {

    public static final String MODID = "godgun";

    public GodGunMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modBus);

        MinecraftForge.EVENT_BUS.register(new GunEventHandler());
        MinecraftForge.EVENT_BUS.register(this);

        // Register client-only events only on the client side
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(this::clientSetup);
        }
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        GodGunCommand.register(event.getDispatcher());
    }
}
