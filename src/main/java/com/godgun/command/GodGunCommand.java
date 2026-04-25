package com.godgun.command;

import com.godgun.item.ModItems;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class GodGunCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("godgun")
                .then(Commands.literal("give")
                    .executes(ctx -> {
                        CommandSourceStack source = ctx.getSource();
                        ServerPlayer player = source.getPlayerOrException();

                        ItemStack gun = new ItemStack(ModItems.GOD_GUN.get());
                        player.getInventory().add(gun);

                        source.sendSuccess(() ->
                            Component.literal("§4§l☠ GOD GUN §r§cgiven. Point and hold right-click."),
                            false
                        );
                        return 1;
                    })
                )
        );
    }
}
