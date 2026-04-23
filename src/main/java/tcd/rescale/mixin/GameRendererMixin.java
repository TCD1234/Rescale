package tcd.rescale.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tcd.rescale.Rescale;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    private float renderDistance;

    @Inject(method = "getProjectionMatrix(D)Lorg/joml/Matrix4f;", at = @At("HEAD"), cancellable = true)
    private void rescale$injectProjection(double fov, CallbackInfoReturnable<Matrix4f> cir) {

        if (!Rescale.CONFIG.enabled) return;

        cir.cancel();

        float aspectRatio = Rescale.CONFIG.getEffectiveRatio();

        if (aspectRatio <= 0.1f) aspectRatio = 1.777f;

        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setPerspective((float) Math.toRadians(fov), aspectRatio, 0.05F, this.renderDistance * 4.0F);

        cir.setReturnValue(matrix4f);
    }
}
