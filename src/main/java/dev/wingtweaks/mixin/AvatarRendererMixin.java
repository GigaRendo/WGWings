package dev.wingtweaks.mixin;

import dev.wingtweaks.client.WingTweaksRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Copies the info we need from the player entity into the render state each frame.
 */
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void wingtweaks$extract(Avatar entity, AvatarRenderState state, float partialTick, CallbackInfo callback) {
        if (state instanceof WingTweaksRenderState extra) {
            extra.wingtweaks$setLocal(entity == Minecraft.getInstance().player);
            extra.wingtweaks$setOffhandPaper(entity.getOffhandItem().is(Items.PAPER));
            // offhand is the opposite arm of the main arm
            extra.wingtweaks$setOffhandLeft(entity.getMainArm() == HumanoidArm.RIGHT);
        }
    }
}
