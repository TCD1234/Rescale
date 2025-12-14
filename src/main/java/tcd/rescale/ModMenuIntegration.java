package tcd.rescale;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            RescaleConfig currentConfig = AutoConfig.getConfigHolder(RescaleConfig.class).getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("text.autoconfig.rescale.title"));

            builder.setTransparentBackground(true);

            builder.setSavingRunnable(() -> {
                AutoConfig.getConfigHolder(RescaleConfig.class).save();
            });

            ConfigEntryBuilder entry = builder.entryBuilder();

            // ==========================================
            // КАТЕГОРИЯ 1: GENERAL
            // ==========================================
            ConfigCategory general = builder.getOrCreateCategory(Text.translatable("text.autoconfig.rescale.category.general"));

            general.addEntry(entry.startBooleanToggle(Text.translatable("text.autoconfig.rescale.option.enabled"), currentConfig.enabled)
                    .setDefaultValue(true)
//                    .setTooltip(Text.translatable("text.autoconfig.rescale.option.enabled.@Tooltip"))
                    .setSaveConsumer(newValue -> currentConfig.enabled = newValue)
                    .build());

            general.addEntry(entry.startEnumSelector(Text.translatable("text.autoconfig.rescale.option.preset"), RescaleConfig.RatioPreset.class, currentConfig.preset)
                    .setDefaultValue(RescaleConfig.RatioPreset.R_16_9)
                    .setEnumNameProvider(value -> Text.translatable("text.autoconfig.rescale.option.preset." + value.name()))
                    .setTooltip(Text.translatable("text.autoconfig.rescale.option.preset.@Tooltip"))
                    .setSaveConsumer(newValue -> currentConfig.preset = newValue)
                    .build());

            general.addEntry(entry.startFloatField(Text.translatable("text.autoconfig.rescale.option.customRatio"), currentConfig.customRatio)
                    .setDefaultValue(1.333f)
                    .setTooltip(Text.translatable("text.autoconfig.rescale.option.customRatio.@Tooltip"))
                    .setSaveConsumer(newValue -> currentConfig.customRatio = newValue)
                    .build());

            // ==========================================
            // КАТЕГОРИЯ 2: VIEW MODEL
            // ==========================================
            ConfigCategory vm = builder.getOrCreateCategory(Text.translatable("text.autoconfig.rescale.option.viewModel"));
            var vmSettings = currentConfig.viewModel;

            vm.addEntry(entry.startBooleanToggle(Text.translatable("text.autoconfig.rescale.option.viewModel.enabled"), vmSettings.enabled)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> vmSettings.enabled = newValue)
                    .build());

            // --- POSITION ---

            // X
            vm.addEntry(entry.startIntSlider(Text.translatable("text.autoconfig.rescale.option.viewModel.posX"), (int)vmSettings.posX, -300, 300)
                    .setDefaultValue(0)
                    .setTooltip(Text.translatable("text.autoconfig.rescale.option.viewModel.posX.@Tooltip"))
                    .setSaveConsumer(newValue -> vmSettings.posX = newValue)
                    .build());
            // Y
            vm.addEntry(entry.startIntSlider(Text.translatable("text.autoconfig.rescale.option.viewModel.posY"), (int)vmSettings.posY, -300, 300)
                    .setDefaultValue(0)
                    .setTooltip(Text.translatable("text.autoconfig.rescale.option.viewModel.posY.@Tooltip"))
                    .setSaveConsumer(newValue -> vmSettings.posY = newValue)
                    .build());
            // Z
            vm.addEntry(entry.startIntSlider(Text.translatable("text.autoconfig.rescale.option.viewModel.posZ"), (int)vmSettings.posZ, -300, 300)
                    .setDefaultValue(0)
                    .setTooltip(Text.translatable("text.autoconfig.rescale.option.viewModel.posZ.@Tooltip"))
                    .setSaveConsumer(newValue -> vmSettings.posZ = newValue)
                    .build());

            // --- SCALE ---
            vm.addEntry(entry.startIntSlider(Text.translatable("text.autoconfig.rescale.option.viewModel.scale"), (int)vmSettings.scale, 10, 200)
                    .setDefaultValue(100)
                    .setTooltip(Text.translatable("text.autoconfig.rescale.option.viewModel.scale.@Tooltip"))
                    .setSaveConsumer(newValue -> vmSettings.scale = newValue)
                    .build());

            // --- ROTATION (будет по аналогии: startIntSlider(-180, 180)) ---

            return builder.build();
        };
    }
}