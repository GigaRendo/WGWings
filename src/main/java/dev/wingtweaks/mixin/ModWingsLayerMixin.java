package dev.wingtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wingtweaks.WingTweaksConfig;
import dev.wingtweaks.client.WingTweaksRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fantastic Wings' render layer. If the wearer is YOU and you ran /wg wings off, skip drawing.
 * Target is given as a string so we don't need Fantastic Wings at compile time.
 */
@Mixin(targets = "fuzs.fantasticwings.common.client.renderer.entity.layers.ModWingsLayer")
public abstract class ModWingsLayerMixin {

    @Inject(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void wingtweaks$hideOwnWings(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
                                         AvatarRenderState state, float yRot, float xRot, CallbackInfo callback) {
        if (!WingTweaksConfig.allWingsVisible) {
            callback.cancel();
            return;
        }
        if (!WingTweaksConfig.ownWingsVisible
                && state instanceof WingTweaksRenderState extra
                && extra.wingtweaks$isLocal()) {
            callback.cancel();
        }
    }
}
