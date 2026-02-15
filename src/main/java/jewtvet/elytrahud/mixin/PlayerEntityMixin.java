package jewtvet.elytrahud.mixin;

import jewtvet.elytrahud.Common;
import jewtvet.elytrahud.HudData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Unique
    private boolean wasElytraFlying = false;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (Common.CONFIG != null && Common.CONFIG.modEnabled && player.getWorld().isClient) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (player.equals(client.player)) {
                boolean isElytraOpen = player.isGliding();
                if (isElytraOpen && !this.wasElytraFlying) {
                    GameOptions options = client.options;
                    if (Common.CONFIG.thirdPersonEnabled) {
                        Common.originalPerspective = options.getPerspective();
                        options.setPerspective(Perspective.THIRD_PERSON_BACK);
                    }

                    if (Common.CONFIG.highFovEnabled) {
                        Common.originalFov = (int) options.getFov().getValue();
                        options.getFov().setValue(110);
                    }

                    if (Common.CONFIG.pumpkinEnabled) {
                        Item[] itemsToFind = new Item[]{Items.ELYTRA};
                        Common.findInHotbar(client, itemsToFind);
                    }

                    Common.isFlying = true;
                    Common.hudData = new HudData();
                    this.wasElytraFlying = true;
                } else if (!isElytraOpen && this.wasElytraFlying) {
                    this.wasElytraFlying = false;
                }
            }
        }
    }
}
