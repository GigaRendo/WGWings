package dev.wingtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wingtweaks.WingTweaksClient;
import dev.wingtweaks.WingTweaksConfig;
import dev.wingtweaks.client.WingTweaksRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * While the body is tilted (flying, elytra, swimming, crawling) and the OFFHAND holds paper, temporarily put that
 * arm into the same pose it has when you stand still holding an item. The paper is then drawn at the same spot on
 * your back as when standing (the pose stack already carries the body tilt, so it follows the body).
 * The arm is restored right after, so the visible arm / other items are untouched.
 */
@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {

    @Unique
    private ModelPart wingtweaks$arm;
    @Unique
    private final float[] wingtweaks$saved = new float[6];

    @Inject(method = "submit", at = @At("HEAD"))
    private void wingtweaks$beforeSubmit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
                                         ArmedEntityRenderState state, float yRot, float xRot, CallbackInfo callback) {
        this.wingtweaks$arm = null;

        if (!WingTweaksConfig.paperFixEnabled
                || !(state instanceof WingTweaksRenderState extra)
                || !extra.wingtweaks$hasOffhandPaper()) {
            return;
        }

        Object parentModel = ((RenderLayer<?, ?>) (Object) this).getParentModel();
        if (!(parentModel instanceof PlayerModel playerModel)) {
            return;
        }

        float factor = WingTweaksClient.tiltFactor(poseStack);
        if (factor <= 0.0F) {
            return;
        }

        boolean left = extra.wingtweaks$isOffhandLeft();
        ModelPart arm = left ? playerModel.leftArm : playerModel.rightArm;

        // remember the current pose
        this.wingtweaks$saved[0] = arm.x;
        this.wingtweaks$saved[1] = arm.y;
        this.wingtweaks$saved[2] = arm.z;
        this.wingtweaks$saved[3] = arm.xRot;
        this.wingtweaks$saved[4] = arm.yRot;
        this.wingtweaks$saved[5] = arm.zRot;
        this.wingtweaks$arm = arm;

        // go to the default arm position, then use the "standing, holding an item" rotation
        arm.resetPose();
        float targetX = (float) Math.toRadians(WingTweaksConfig.paperArmXDeg);
        float targetZ = (float) Math.toRadians(WingTweaksConfig.paperArmZDeg) * (left ? -1.0F : 1.0F);

        // blend so there is no snapping while the body tilts in / out
        arm.x = Mth.lerp(factor, this.wingtweaks$saved[0], arm.x);
        arm.y = Mth.lerp(factor, this.wingtweaks$saved[1], arm.y);
        arm.z = Mth.lerp(factor, this.wingtweaks$saved[2], arm.z);
        arm.xRot = Mth.lerp(factor, this.wingtweaks$saved[3], targetX);
        arm.yRot = Mth.lerp(factor, this.wingtweaks$saved[4], 0.0F);
        arm.zRot = Mth.lerp(factor, this.wingtweaks$saved[5], targetZ);
    }

    @Inject(method = "submit", at = @At("RETURN"))
    private void wingtweaks$afterSubmit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
                                        ArmedEntityRenderState state, float yRot, float xRot, CallbackInfo callback) {
        ModelPart arm = this.wingtweaks$arm;
        if (arm != null) {
            arm.x = this.wingtweaks$saved[0];
            arm.y = this.wingtweaks$saved[1];
            arm.z = this.wingtweaks$saved[2];
            arm.xRot = this.wingtweaks$saved[3];
            arm.yRot = this.wingtweaks$saved[4];
            arm.zRot = this.wingtweaks$saved[5];
            this.wingtweaks$arm = null;
        }
    }
}
