package de.cadentem.quality_food.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public record DropData(BlockState state, float bonus) {
    public static DropData create(final BlockState state, @Nullable final Entity entity) {
        return new DropData(state, entity instanceof Player player ? player.getLuck() : 0);
    }
}