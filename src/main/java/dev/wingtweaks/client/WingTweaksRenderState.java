package dev.wingtweaks.client;

/**
 * Extra data we attach to AvatarRenderState (implemented by AvatarRenderStateMixin).
 */
public interface WingTweaksRenderState {
    boolean wingtweaks$isLocal();

    void wingtweaks$setLocal(boolean value);

    boolean wingtweaks$hasOffhandPaper();

    void wingtweaks$setOffhandPaper(boolean value);

    /** true if the offhand is the player's LEFT arm */
    boolean wingtweaks$isOffhandLeft();

    void wingtweaks$setOffhandLeft(boolean value);

    boolean wingtweaks$isCrouching();

    void wingtweaks$setCrouching(boolean value);
}
