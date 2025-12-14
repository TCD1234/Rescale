package tcd.rescale; // ТЕПЕРЬ ТОЧНО НЕ ПРАВИЛЬНЫЙ ПАКЕТ! :D

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rescale")
public class RescaleConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enabled = true;


    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @ConfigEntry.Gui.Tooltip
    public RatioPreset preset = RatioPreset.R_16_9;


    @ConfigEntry.Gui.Tooltip
    public float customRatio = 1.333f;

    @ConfigEntry.Gui.CollapsibleObject
    public ViewModelSettings viewModel = new ViewModelSettings();

    public static class ViewModelSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        // --- ПОЗИЦИЯ (От -3.0 до 3.0) ---
        // Храним как -300..300
        @ConfigEntry.Category("position")
        @ConfigEntry.BoundedDiscrete(min = -300, max = 300)
        public long posX = 0;

        @ConfigEntry.Category("position")
        @ConfigEntry.BoundedDiscrete(min = -300, max = 300)
        public long posY = 0;

        @ConfigEntry.Category("position")
        @ConfigEntry.BoundedDiscrete(min = -300, max = 300)
        public long posZ = 0;

        // --- ВРАЩЕНИЕ (От -180 до 180 градусов) ---
        @ConfigEntry.Category("rotation")
        @ConfigEntry.BoundedDiscrete(min = -180, max = 180)
        public long rotX = 0;

        @ConfigEntry.Category("rotation")
        @ConfigEntry.BoundedDiscrete(min = -180, max = 180)
        public long rotY = 0;

        @ConfigEntry.Category("rotation")
        @ConfigEntry.BoundedDiscrete(min = -180, max = 180)
        public long rotZ = 0;

        // --- РАЗМЕР (От 0.1 до 2.0) ---
        // 100 = 1.0 (Обычный размер)
        @ConfigEntry.Category("scale")
        @ConfigEntry.BoundedDiscrete(min = 10, max = 200)
        public long scale = 100;
    }

    public float getEffectiveRatio() {
        if (preset == RatioPreset.CUSTOM) {
            return customRatio;
        }
        return preset.value;
    }


    public enum RatioPreset {
        CUSTOM(0.0f),       // Не важно
        R_16_9(1.7777f),    // Стандарт (Full HD)
        R_16_10(1.6000f),   // Ноуты
        R_4_3(1.3333f),     // CS:GO
        R_5_4(1.2500f);

        public final float value;
        RatioPreset(float value) { this.value = value; }
    }
}