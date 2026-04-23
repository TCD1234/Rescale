package tcd.rescale;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Rescale.MOD_ID)
public class Rescale {
    public static final String MOD_ID = "rescale";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static RescaleConfig CONFIG;
    public static KeyMapping EDIT_MODE_KEY;

    public Rescale(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (client, parentScreen) -> AutoConfig.getConfigScreen(RescaleConfig.class, parentScreen).get()
        );

        if (FMLEnvironment.dist.isDedicatedServer()) {
            LOGGER.warn("Rescale is client-only and does nothing on the server.");
            return;
        }

        ConfigHolder<RescaleConfig> holder = AutoConfig.register(RescaleConfig.class, GsonConfigSerializer::new);
        CONFIG = holder.getConfig();
        holder.registerSaveListener((manager, data) -> {
            CONFIG = data;
            return InteractionResult.PASS;
        });

        EDIT_MODE_KEY = new KeyMapping(
                "key.rescale.edit_mode",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_INSERT,
                "category.rescale"
        );

        modEventBus.addListener(this::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(EDIT_MODE_KEY);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        ViewModelEditor.onTick(Minecraft.getInstance());
    }

}
