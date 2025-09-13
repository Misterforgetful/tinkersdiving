package com.example.tinkersdiving.config;

import net.minecraftforge.common.ForgeConfigSpec;
//I have no clue how this works, I just asked copilot in this case because it'd be kind to users to have.
public class TinkersDivingConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        COMMON_SPEC = builder.build();
    }

    public static class Common {
        public final ForgeConfigSpec.IntValue airPerLevel;
        public final ForgeConfigSpec.IntValue defaultModifierCapacity;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("backtank");
            airPerLevel = builder
                .comment("How much additional air is storable per extra backtank level. 300 for Create default, 1200 for mine.")
                .defineInRange("airPerLevel", 1200, 100, 10000);
            defaultModifierCapacity = builder
                .comment("How much air is stored for the initial level of backtank. 900 for Create default, 1200 for mine.")
                .defineInRange("defaultModifierCapacity", 1200, 100, 10000);
            builder.pop();
        }
    }
}
