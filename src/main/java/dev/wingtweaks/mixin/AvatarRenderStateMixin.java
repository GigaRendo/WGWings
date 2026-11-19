package dev.wingtweaks.mixin;

import dev.wingtweaks.client.WingTweaksRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public abstract class AvatarRenderStateMixin implements WingTweaksRenderState {
    @Unique
    private boolean wingtweaks$local;
    @Unique
    private boolean wingtweaks$offhandPaper;
    @Unique
    private boolean wingtweaks$offhandLeft;

    @Override
    public boolean wingtweaks$isLocal() {
        return this.wingtweaks$local;
    }

    @Override
    public void wingtweaks$setLocal(boolean value) {
        this.wingtweaks$local = value;
    }

    @Override
    public boolean wingtweaks$hasOffhandPaper() {
        return this.wingtweaks$offhandPaper;
    }

    @Override
    public void wingtweaks$setOffhandPaper(boolean value) {
        this.wingtweaks$offhandPaper = value;
    }

    @Override
    public boolean wingtweaks$isOffhandLeft() {
        return this.wingtweaks$offhandLeft;
    }

    @Override
    public void wingtweaks$setOffhandLeft(boolean value) {
        this.wingtweaks$offhandLeft = value;
    }
}
