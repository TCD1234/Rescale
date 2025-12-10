package tcd.rescale.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tcd.rescale.Rescale;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"))
    private void onRenderItem(
            net.minecraft.entity.LivingEntity entity,
            ItemStack stack,
            ModelTransformationMode renderMode,
            boolean leftHanded,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci) {

        if (Rescale.CONFIG.viewModel.enabled &&
                (renderMode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND ||
                        renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND)) {

            var settings = Rescale.CONFIG.viewModel;

            // Пример: 50 -> 0.5
            float px = settings.posX / 100.0f;
            float py = settings.posY / 100.0f;
            float pz = settings.posZ / 100.0f;

            // Инверсия X для левой руки чтобы работало нормально
            if (renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND) {
                px = -px;
            }

            matrices.translate(px, py, pz);

            // Пример: 100 -> 1.0
            float s = settings.scale / 100.0f;
            matrices.scale(s, s, s);

            // 3. Поворот (Тут делить НЕ НАДО, градусы и в Африке градусы но пока они и нафиг не нужны)
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)settings.rotX));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)settings.rotY));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)settings.rotZ));
        }
    }
}