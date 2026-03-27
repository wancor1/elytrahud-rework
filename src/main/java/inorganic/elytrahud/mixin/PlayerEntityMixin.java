package inorganic.elytrahud.mixin;


import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {
    @Unique
    private boolean wasElytraFlying = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        Player player = (Player)(Object)this;
        if (inorganic.elytrahud.ConfigManager.getConfig().modEnabled) {
            Minecraft client = Minecraft.getInstance();
            if (player.equals(client.player) && client.level != null) {
                boolean isElytraOpen = player.isFallFlying();
                if (isElytraOpen && !this.wasElytraFlying) {
                    inorganic.elytrahud.Common.isFlying = true;
                    this.wasElytraFlying = true;
                } else if (!isElytraOpen && this.wasElytraFlying) {
                    this.wasElytraFlying = false;
                }
            }
        }
    }
}