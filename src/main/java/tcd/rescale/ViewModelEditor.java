package tcd.rescale;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import tcd.rescale.Rescale;
import tcd.rescale.RescaleConfig;

public class ViewModelEditor {

    private static boolean isEditing = false;


    private static int cooldown = 0;

    public static void onTick(MinecraftClient client) {
        if (client.player == null) return;

        while (tcd.rescale.Rescale.EDIT_MODE_KEY.wasPressed()) {
            isEditing = !isEditing;
            client.player.sendMessage(Text.of(isEditing ? "§a[Rescale] Edit Mode: ON (Arrows + Ctrl)" : "§c[Rescale] Edit Mode: OFF"), true);
        }

        if (!isEditing) return;


        if (cooldown > 0) {
            cooldown--;


        }

        var settings = Rescale.CONFIG.viewModel;
        boolean changed = false;
        long step = 1;

        long handle = client.getWindow().getHandle();

        boolean isModifier = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL) ||
                InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_CONTROL);

        boolean isShift = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);

        if (isShift) step = 5;

        if (!isShift && cooldown > 0) {
            cooldown--;
            return;
        }

        if (!isShift) cooldown = 3;


        // --- ЛОГИКА ---

        // UP
        if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_UP)) {
            if (isModifier) settings.posZ += step; // Ctrl + Up = Ближе/Дальше
            else settings.posY += step;            // Up = Вверх
            changed = true;
        }

        // DOWN
        if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_DOWN)) {
            if (isModifier) settings.posZ -= step;
            else settings.posY -= step;
            changed = true;
        }

        // RIGHT
        if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT)) {
            if (isModifier) settings.scale += step; // Ctrl + Right = Увеличить
            else settings.posX += step;             // Right = Вправо
            changed = true;
        }

        // LEFT
        if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT)) {
            if (isModifier) settings.scale -= step; // Ctrl + Left = Уменьшить
            else settings.posX -= step;             // Left = Влево
            changed = true;
        }

        if (changed) {
            if (settings.scale < 10) settings.scale = 10;

            AutoConfig.getConfigHolder(RescaleConfig.class).save();

            String msg = String.format("§eX: %d §7| §eY: %d §7| §bZ: %d §7| §dScale: %d%%",
                    settings.posX, settings.posY, settings.posZ, settings.scale);
            client.player.sendMessage(Text.of(msg), true);
        }
    }
}