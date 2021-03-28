package mod.patrigan.slimierslimes.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.slimierslimes.SlimierSlimes;
import mod.patrigan.slimierslimes.data.CodecJsonDataManager;
import mod.patrigan.slimierslimes.entities.util.SlimeSizeCalculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mod.patrigan.slimierslimes.entities.util.SlimeSizeCalculation.*;

public class SlimeData {
    public static final Codec<SlimeData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    SlimeSizeCalculation.CODEC.optionalFieldOf("maxHealth", DEFAULT_SIZE_SQUARED).forGetter(data -> data.maxHealth),
                    SlimeSizeCalculation.CODEC.optionalFieldOf("armor", DEFAULT_ZERO).forGetter(data -> data.armor),
                    SlimeSizeCalculation.CODEC.optionalFieldOf("armorToughness", DEFAULT_ZERO).forGetter(data -> data.armorToughness),
                    SlimeSizeCalculation.CODEC.optionalFieldOf("attackDamage", DEFAULT_SIZE).forGetter(data -> data.attackDamage),
                    SlimeSizeCalculation.CODEC.optionalFieldOf("jumpDelay", DEFAULT_JUMP_DELAY).forGetter(data -> data.jumpDelay),
                    SlimeSizeCalculation.CODEC.optionalFieldOf("movementSpeed", DEFAULT_MOVEMENT_SPEED).forGetter(data -> data.movementSpeed),
                    SlimeSizeCalculation.CODEC.optionalFieldOf("jumpHeightMultiplier", DEFAULT_JUMP_HEIGHT_MULTIPLIER).forGetter(data -> data.jumpHeightMultiplier),
                    Codec.DOUBLE.optionalFieldOf("entityGravity", 0.08D).forGetter(data -> data.entityGravity),
                    SlimeSizeCalculation.CODEC.optionalFieldOf("experienceValue", DEFAULT_EXPERIENCE).forGetter(data -> data.experienceValue),
                    Codec.INT.optionalFieldOf("maxInChunk", 6).forGetter(data -> data.maxInChunk),
                    Codec.BOOL.optionalFieldOf("spawnOnSurface", true).forGetter(data -> data.spawnOnSurface),
                    SquishParticleData.CODEC.fieldOf("squishParticleData").forGetter(data -> data.squishParticleData),
                    SlimeSpawnData.CODEC.listOf().fieldOf("slimeSpawnData").forGetter(data -> data.slimeSpawnData)
            ).apply(builder, SlimeData::new));

    private final SlimeSizeCalculation maxHealth;
    private final SlimeSizeCalculation armor;
    private final SlimeSizeCalculation armorToughness;
    private final SlimeSizeCalculation attackDamage;
    private final SlimeSizeCalculation jumpDelay;
    private final SlimeSizeCalculation movementSpeed;
    private final SlimeSizeCalculation jumpHeightMultiplier;
    private final double entityGravity;
    private final SlimeSizeCalculation experienceValue;
    private final int maxInChunk;
    private final boolean spawnOnSurface;
    private final SquishParticleData squishParticleData;
    private final List<SlimeSpawnData> slimeSpawnData;

    public SlimeData(SlimeSizeCalculation maxHealth, SlimeSizeCalculation armor, SlimeSizeCalculation armorToughness, SlimeSizeCalculation attackDamage, SlimeSizeCalculation jumpDelay, SlimeSizeCalculation movementSpeed, SlimeSizeCalculation jumpHeightMultiplier, double entityGravity, SlimeSizeCalculation experienceValue, int maxInChunk, boolean spawnOnSurface, SquishParticleData squishParticleData, List<SlimeSpawnData> slimeSpawnData) {
        this.maxHealth = maxHealth;
        this.armor = armor;
        this.armorToughness = armorToughness;
        this.attackDamage = attackDamage;
        this.jumpDelay = jumpDelay;
        this.movementSpeed = movementSpeed;
        this.jumpHeightMultiplier = jumpHeightMultiplier;
        this.entityGravity = entityGravity;
        this.experienceValue = experienceValue;
        this.maxInChunk = maxInChunk;
        this.spawnOnSurface = spawnOnSurface;
        this.squishParticleData = squishParticleData;
        this.slimeSpawnData = slimeSpawnData;
    }

    public SlimeData(SquishParticleData squishParticleData){
        this.maxHealth = DEFAULT_SIZE_SQUARED;
        this.armor = DEFAULT_ZERO;
        this.armorToughness = DEFAULT_ZERO;
        this.attackDamage = DEFAULT_SIZE;
        this.jumpDelay = DEFAULT_JUMP_DELAY;
        this.movementSpeed = DEFAULT_MOVEMENT_SPEED;
        this.jumpHeightMultiplier = DEFAULT_JUMP_HEIGHT_MULTIPLIER;
        this.entityGravity = 0.08D;
        this.experienceValue = DEFAULT_EXPERIENCE;
        this.maxInChunk = 6;
        this.spawnOnSurface = true;
        this.squishParticleData = squishParticleData;
        this.slimeSpawnData = new ArrayList<>();
    }

    public float getMaxHealth(int size, Random rand) {
        return maxHealth.apply(size, rand);
    }

    public float getArmor(int size, Random rand) {
        return armor.apply(size, rand);
    }

    public float getArmorToughness(int size, Random rand) {
        return armorToughness.apply(size, rand);
    }

    public float getAttackDamage(int size, Random rand) {
        return attackDamage.apply(size, rand);
    }

    public float getJumpDelay(int size, Random rand) {
        return jumpDelay.apply(size, rand);
    }

    public float getMovementSpeed(int size, Random rand) {
        return movementSpeed.apply(size, rand);
    }

    public float getJumpHeightMultiplier(int size, Random rand) {
        return jumpHeightMultiplier.apply(size, rand);
    }

    public int getExperienceValue(int size, Random rand) {
        return (int) Math.floor(experienceValue.apply(size, rand));
    }

    public double getEntityGravity() {
        return entityGravity;
    }

    public int getMaxInChunk() {
        return maxInChunk;
    }

    public boolean isSpawnOnSurface() {
        return spawnOnSurface;
    }

    public List<SlimeSpawnData> getSlimeSpawnData() {
        return slimeSpawnData;
    }

    public SquishParticleData getSquishParticleData() {
        return squishParticleData;
    }


}
