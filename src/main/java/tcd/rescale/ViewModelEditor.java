package tcd.rescale;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ViewModelEditor {

    private static boolean isEditing = false;
    private static int cooldown = 0;

    private static int Time = 0;
    private static boolean dirty = false;

    public static void onTick(Minecraft client) {
        if (client.player == null) return;

        if (dirty) {
            if (--Time <= 0) {
                AutoConfig.getConfigHolder(RescaleConfig.class).save();
                dirty = false;
            }
        }

        while (Rescale.EDIT_MODE_KEY.consumeClick()) {
            boolean wasEditing = isEditing;
            isEditing = !isEditing;

            client.player.displayClientMessage(
                    Component.literal(isEditing ? "§a[Rescale] Edit Mode: ON (Arrows + Ctrl)" : "§c[Rescale] Edit Mode: OFF"),
                    true
            );

            if (wasEditing && !isEditing && dirty) {
                AutoConfig.getConfigHolder(RescaleConfig.class).save();
                dirty = false;
                Time = 0;
            }
        }

        if (!isEditing) return;

        var settings = Rescale.CONFIG.viewModel;
        boolean changed = false;
        long step = 1;
        long handle = client.getWindow().getWindow();

        boolean isModifier = InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL) ||
                InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL);

        boolean isShift = InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);

        if (isShift) step = 5;

        if (!isShift) {
            if (cooldown > 0) {
                cooldown--;
                return;
            }
            cooldown = 3;
        }

        // UP
        if (InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_UP)) {
            if (isModifier) settings.posZ += step;
            else settings.posY += step;
            changed = true;
        }

        // DOWN
        if (InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_DOWN)) {
            if (isModifier) settings.posZ -= step;
            else settings.posY -= step;
            changed = true;
        }

        // RIGHT
        if (InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT)) {
            if (isModifier) settings.scale += step;
            else settings.posX += step;
            changed = true;
        }

        // LEFT
        if (InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT)) {
            if (isModifier) settings.scale -= step;
            else settings.posX -= step;
            changed = true;
        }

        if (changed) {
            if (settings.scale < 10) settings.scale = 10;

            dirty = true;
            Time = 100;

            String msg = String.format("§eX: %d §7| §eY: %d §7| §bZ: %d §7| §dScale: %d%%",
                    settings.posX, settings.posY, settings.posZ, settings.scale);
            client.player.displayClientMessage(Component.literal(msg), true);
        }
    }
}
