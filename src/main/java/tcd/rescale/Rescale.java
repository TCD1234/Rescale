package tcd.rescale;

import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Rescale implements ClientModInitializer {
	public static final String MOD_ID = "rescale";

    public static RescaleConfig CONFIG;
    public static KeyBinding EDIT_MODE_KEY;

	@Override
	public void onInitializeClient() {

        ConfigHolder<RescaleConfig> holder = AutoConfig.register(RescaleConfig.class, GsonConfigSerializer::new);

        CONFIG = holder.getConfig();

        holder.registerSaveListener((manager, data) -> {
            CONFIG = data;
            return net.minecraft.util.ActionResult.PASS;
        });

        EDIT_MODE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rescale.edit_mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_INSERT,
                "category.rescale"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(ViewModelEditor::onTick);
    }
}