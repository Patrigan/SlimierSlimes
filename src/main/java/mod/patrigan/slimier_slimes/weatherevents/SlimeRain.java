package mod.patrigan.slimier_slimes.weatherevents;

public class SlimeRain {

    /*public static void acidRainEvent(Chunk chunk, ServerWorld world, int gameRuleTickSpeed, long worldTime) {
        ChunkPos chunkpos = chunk.getPos();
        int chunkXStart = chunkpos.getXStart();
        int chunkZStart = chunkpos.getZStart();
        IProfiler iprofiler = world.getProfiler();
        iprofiler.startSection("acidrain");
        BlockPos blockpos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, world.getBlockRandomPos(chunkXStart, 0, chunkZStart, 15));
        if (world.isAreaLoaded(blockpos, 1)) {
            if (BetterWeather.BetterWeatherEvents.weatherData.isAcidRain() && world.getWorldInfo().isRaining() && worldTime % BetterWeatherConfig.tickBlockDestroySpeed.get() == 0 && BetterWeatherConfig.destroyBlocks.get() && world.getBiome(blockpos).getPrecipitation() == Biome.RainType.RAIN) {

            }
        }
        iprofiler.endSection();
    }

    public static void addSlimeRainParticles(ActiveRenderInfo activeRenderInfoIn, Minecraft mc, WorldRenderer worldRenderer) {
        float f = mc.world.getRainStrength(1.0F) / (Minecraft.isFancyGraphicsEnabled() ? 1.0F : 2.0F);
        if (!(f <= 0.0F)) {
            Random random = new Random(worldRenderer.ticks * 312987231L);
            IWorldReader iworldreader = mc.world;
            BlockPos blockpos = new BlockPos(activeRenderInfoIn.getProjectedView());
            BlockPos blockpos1 = null;
            int i = (int)(100.0F * f * f) / (mc.gameSettings.particles == ParticleStatus.DECREASED ? 2 : 1);

            for(int j = 0; j < i; ++j) {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockpos2 = iworldreader.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos.add(k, 0, l)).down();
                Biome biome = iworldreader.getBiome(blockpos2);
                if (blockpos2.getY() > 0 && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && biome.getPrecipitation() == Biome.RainType.RAIN && biome.getTemperature(blockpos2) >= 0.15F) {
                    blockpos1 = blockpos2;
                    if (mc.gameSettings.particles == ParticleStatus.MINIMAL) {
                        break;
                    }

                    double d0 = random.nextDouble();
                    double d1 = random.nextDouble();
                    BlockState blockstate = iworldreader.getBlockState(blockpos2);
                    FluidState fluidstate = iworldreader.getFluidState(blockpos2);
                    VoxelShape voxelshape = blockstate.getCollisionShape(iworldreader, blockpos2);
                    double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
                    double d3 = (double)fluidstate.getActualHeight(iworldreader, blockpos2);
                    double d4 = Math.max(d2, d3);
                    IParticleData iparticledata = ParticleTypes.SMOKE;
                    mc.world.addParticle(iparticledata, (double)blockpos2.getX() + d0, (double)blockpos2.getY() + d4, (double)blockpos2.getZ() + d1, 0.0D, 0.0D, 0.0D);
                }
            }

            if (blockpos1 != null && random.nextInt(3) < worldRenderer.rainSoundTime++) {
                worldRenderer.rainSoundTime = 0;
                if (blockpos1.getY() > blockpos.getY() + 1 && iworldreader.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos).getY() > MathHelper.floor((float)blockpos.getY())) {
                    mc.world.playSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
                } else {
                    mc.world.playSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
                }
            }
        }
    }*/
}
