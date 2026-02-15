package jewtvet.elytrahud.mixin;

import jewtvet.elytrahud.Common;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "isUsingRiptide", at = @At("HEAD"), cancellable = true)
    public void isUsingRiptide(CallbackInfoReturnable<Boolean> cir) {
        if (Common.CONFIG != null && Common.CONFIG.modEnabled
            && Common.CONFIG.disableRiptideAnim
            && Common.client != null
            && Common.client.player != null
            && Common.client.player.isGliding()) {
            cir.setReturnValue(false);
        }
    }
}
