package mod.patrigan.slimierslimes.configs;

import mod.patrigan.slimierslimes.util.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class SlimeConfigs {

    public static class SlimeConfigValues {
        public ConfigHelper.ConfigValueListener<Boolean> allowVanillaSlime;
        public ConfigHelper.ConfigValueListener<Integer> totalSlimeSpawnWeight;
        public ConfigHelper.ConfigValueListener<Boolean> allowSlimeBlockEffects;

        public SlimeConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
            builder.push("Slimier Slimes Options");

            builder.push("Vanilla Slime Options");

            allowVanillaSlime = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Allows the spawning of the Vanilla Slime. This will also allow\r\n"
                            +" slimeballs and the original slimeblock to reappear.\r\n"
                            +" Defaults to false.\n\n")
                    .translation("slimier_slimes.config.main.allowvanillaslime")
                    .define("allowVanillaSlime", false));

            allowVanillaSlime = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Maintains the spawning of new slimes within chunks.\r\n"
                            +" Otherwise slimes will have spawns everywhere, like normal mobs.\r\n"
                            +" Defaults to false.\n\n")
                    .translation("slimier_slimes.config.main.maintainChunkSpawning")
                    .define("maintainChunkSpawning", false));

            builder.pop();

            builder.push("Slime Spawning Options");

            totalSlimeSpawnWeight = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Determines the spawning weight of slimes. All Slimes together\r\n"
                            +" are bound by this weight. 100 makes slimes as common as zombies.\r\n"
                            +" Defaults to 300.\n\n")
                    .translation("slimier_slimes.config.main.totalSlimeSpawnWeight")
                    .define("totalSlimeSpawnWeight", 300));

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
