package dev.wingtweaks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WingTweaksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WingTweaksConfig.load();

        // /wg wings [on|off]  (and the same under /wingtweaks, in case the server's own /wg tree
        // stops the chat box from highlighting the client-side /wg wings)
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(root("wg"));
            dispatcher.register(root("wingtweaks"));
        });
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> root(String name) {
        return LiteralArgumentBuilder.<FabricClientCommandSource>literal(name)
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("wings")
                        .executes(ctx -> setWings(ctx.getSource(), !WingTweaksConfig.ownWingsVisible))
                        .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("on")
                                .executes(ctx -> setWings(ctx.getSource(), true)))
                        .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("off")
                                .executes(ctx -> setWings(ctx.getSource(), false)))
                        .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("all")
                                .executes(ctx -> setAllWings(ctx.getSource(), !WingTweaksConfig.allWingsVisible))
                                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("on")
                                        .executes(ctx -> setAllWings(ctx.getSource(), true)))
                                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("off")
                                        .executes(ctx -> setAllWings(ctx.getSource(), false)))));
    }

    private static int setAllWings(FabricClientCommandSource source, boolean visible) {
        WingTweaksConfig.allWingsVisible = visible;
        WingTweaksConfig.save();
        source.sendFeedback(Component.literal(
                visible ? "Everyone's Fantastic Wings are now visible." : "Everyone's Fantastic Wings are now hidden."));
        return 1;
    }

    private static int setWings(FabricClientCommandSource source, boolean visible) {
        WingTweaksConfig.ownWingsVisible = visible;
        WingTweaksConfig.save();
        source.sendFeedback(Component.literal(
                visible ? "Your Fantastic Wings are now visible." : "Your Fantastic Wings are now hidden."));
        return 1;
    }

    /**
     * 0 = body upright, 1 = body clearly tilted (flying / elytra / swimming / crawling).
     * Measured from the pose stack the layers are drawn with, so it also catches Fantastic Wings' own flight pose.
     */
    public static float tiltFactor(PoseStack poseStack) {
        Matrix4f matrix = poseStack.last().pose();
        Vector3f up = matrix.transformDirection(0.0F, 1.0F, 0.0F, new Vector3f());
        float length = up.length();
        if (length < 1.0E-4F) {
            return 0.0F;
        }
        float cos = Math.min(1.0F, Math.abs(up.y()) / length);
        float tiltDeg = (float) Math.toDegrees(Math.acos(cos));
        float start = WingTweaksConfig.tiltStartDeg;
        float full = Math.max(start + 0.1F, WingTweaksConfig.tiltFullDeg);
        float t = (tiltDeg - start) / (full - start);
        return Math.max(0.0F, Math.min(1.0F, t));
    }
}
