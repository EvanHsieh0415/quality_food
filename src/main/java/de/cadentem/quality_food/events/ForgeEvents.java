package de.cadentem.quality_food.events;

import de.cadentem.quality_food.util.QualityUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void handleFishing(final ItemFishedEvent event) {
        if (event.getEntity().getLevel().isClientSide()) {
            return;
        }

        event.getDrops().forEach(drop -> QualityUtils.applyQuality(drop, event.getEntity().getRandom(), event.getEntity().getLuck()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void handleLoot(final LivingDropsEvent event) {
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof LivingEntity livingAttacker) {
            event.getDrops().forEach(drop -> QualityUtils.applyQuality(drop.getItem(), livingAttacker.getRandom(), livingAttacker instanceof Player player ? player.getLuck() : 0));
        }
    }

    @SubscribeEvent
    public static void handleCrafting(final PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity().getLevel().isClientSide()) {
            return;
        }

        int size = event.getInventory().getContainerSize();
        float qualityBonus = 0;

        for (int slot = 0; slot < size; slot++) {
            ItemStack stack = event.getInventory().getItem(slot);

            if (stack != event.getCrafting()) {
                qualityBonus += QualityUtils.getQuality(stack).ordinal() * 3;
            }
        }

        QualityUtils.applyQuality(event.getCrafting(), event.getEntity().getRandom(), event.getEntity().getLuck() + qualityBonus);
    }
}
