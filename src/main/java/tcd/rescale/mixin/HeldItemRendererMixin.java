package tcd.rescale.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tcd.rescale.Rescale;

@Mixin(ItemInHandRenderer.class)
public class HeldItemRendererMixin {

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"))
    private void onRenderItem(
            LivingEntity entity,
            ItemStack stack,
            ItemDisplayContext displayContext,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci) {

        if (Rescale.CONFIG.viewModel.enabled &&
                (displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ||
                        displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND)) {

            var settings = Rescale.CONFIG.viewModel;

            float px = settings.posX / 100.0f;
            float py = settings.posY / 100.0f;
            float pz = settings.posZ / 100.0f;

            if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
                px = -px;
            }

            poseStack.translate(px, py, pz);

            float s = settings.scale / 100.0f;
            poseStack.scale(s, s, s);

//            poseStack.mulPose(Axis.XP.rotationDegrees((float) settings.rotX));
//            poseStack.mulPose(Axis.YP.rotationDegrees((float) settings.rotY));
//            poseStack.mulPose(Axis.ZP.rotationDegrees((float) settings.rotZ));
        }
    }
}
