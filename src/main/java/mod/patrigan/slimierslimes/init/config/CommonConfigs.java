package mod.patrigan.slimierslimes.init.config;

import mod.patrigan.slimierslimes.util.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {

    public static class CommonConfigValues {
        public ConfigHelper.ConfigValueListener<Boolean> allowVanillaSlime;
        public ConfigHelper.ConfigValueListener<Boolean> maintainChunkSpawning;
        public ConfigHelper.ConfigValueListener<Boolean> useTotalSlimeSpawnWeight;
        public ConfigHelper.ConfigValueListener<Integer> totalSlimeSpawnWeight;
        public ConfigHelper.ConfigValueListener<Float> shroomSlimeSuspiciousChance;
        public ConfigHelper.ConfigValueListener<Boolean> allowSlimeBlockEffects;

        public CommonConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
            builder.push("Slimier Slimes Options");

            builder.push("Vanilla Slime Options");

            allowVanillaSlime = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Allows the spawning of the Vanilla Slime. This will also allow\r\n"
                            +" slimeballs and the original slimeblock to reappear.\r\n"
                            +" Defaults to false.\n\n")
                    .translation("slimier_slimes.config.main.allowvanillaslime")
                    .define("allowVanillaSlime", false));

            maintainChunkSpawning = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Maintains the spawning of new slimes within chunks.\r\n"
                            +" Otherwise slimes will have spawns everywhere, like normal mobs.\r\n"
                            +" Defaults to false.\n\n")
                    .translation("slimier_slimes.config.mobs.maintainChunkSpawning")
                    .define("maintainChunkSpawning", false));

            builder.pop();

            builder.push("Slime Spawning Options");

            useTotalSlimeSpawnWeight = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Determines if slimes should be bounded by a total weight. \r\n"
                            +" Disabling this will (likely) make slimes more common in the world.\r\n"
                            +" Enabling it will cause a calculation to happen that limits the total weight.\r\n"
                            +" Defaults to true.\n\n")
                    .translation("slimier_slimes.config.mobs.useTotalSlimeSpawnWeight")
                    .define("useTotalSlimeSpawnWeight", true));

            totalSlimeSpawnWeight = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Determines the spawning weight of slimes. All Slimes together\r\n"
                            +" are bound by this weight. 100 makes slimes as common as zombies.\r\n"
                            +" Defaults to 150.\n\n")
                    .translation("slimier_slimes.config.mobs.totalSlimeSpawnWeight")
                    .define("totalSlimeSpawnWeight", 150));

            builder.pop();

            builder.push("Slime Entity Options");

            shroomSlimeSuspiciousChance = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Determines the chance for a shroomslime to create a suspicious stew. \r\n"
                            +" Defaults to 0.2.\n\n")
                    .translation("slimier_slimes.config.mobs.shroomSlimeSuspiciousChance")
                    .define("shroomSlimeSuspiciousChance", 0.2F));

            builder.pop();

            builder.push("Slime Block Options");

            allowSlimeBlockEffects = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Activates special slime block effects. If you do not want slime blocks to\r\n"
                            +" bump you around or be used as a factory conveyor belt system, \r\n"
                            +" set this to false. \r\n"
                            +" Defaults to true.\n\n")
                    .translation("slimier_slimes.config.main.allowSlimeBlockEffects")
                    .define("allowSlimeBlockEffects", true));

            builder.pop();

            builder.pop();
        }
    }
}
