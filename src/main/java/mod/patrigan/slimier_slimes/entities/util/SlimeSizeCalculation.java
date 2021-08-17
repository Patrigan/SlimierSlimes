package mod.patrigan.slimier_slimes.entities.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Random;

public class SlimeSizeCalculation {
    public static final Codec<SlimeSizeCalculation> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("isSizeSquared", false).forGetter(data -> data.isSizeSquared),
                    Codec.BOOL.optionalFieldOf("isCalculationSizeSquared", false).forGetter(data -> data.isCalculationSizeSquared),
                    Codec.FLOAT.optionalFieldOf("sizeMultiplier", 1F).forGetter(data -> data.sizeMultiplier),
                    Codec.FLOAT.optionalFieldOf("sizeModifier", 0F).forGetter(data -> data.sizeModifier),
                    Codec.FLOAT.optionalFieldOf("resultMultiplier", 1F).forGetter(data -> data.resultMultiplier),
                    Codec.FLOAT.optionalFieldOf("resultModifier", 0F).forGetter(data -> data.resultModifier),
                    Codec.INT.optionalFieldOf("resultRandomMultiplierBound", 0).forGetter(data -> data.resultRandomMultiplierBound),
                    Codec.INT.optionalFieldOf("resultRandomModifierBound", 0).forGetter(data -> data.resultRandomModifierBound)
            ).apply(builder, SlimeSizeCalculation::new));

    public static final SlimeSizeCalculation DEFAULT_SIZE = new SlimeSizeCalculation(false, false, 1F, 0F, 1F, 0F, 0, 0);
    public static final SlimeSizeCalculation DEFAULT_ZERO = new SlimeSizeCalculation(false, false, 0F, 0F, 1F, 0F, 0, 0);
    public static final SlimeSizeCalculation DEFAULT_SIZE_SQUARED = new SlimeSizeCalculation(true, false, 1F, 0F, 1F, 0F, 0, 0);
    public static final SlimeSizeCalculation DEFAULT_JUMP_DELAY = new SlimeSizeCalculation(false, false, 0F, 0F, 1F, 10F, 0, 20);
    public static final SlimeSizeCalculation DEFAULT_MOVEMENT_SPEED = new SlimeSizeCalculation(false, false, 1F, 0F, 0.1F, 0.2F, 0, 0);
    public static final SlimeSizeCalculation DEFAULT_JUMP_HEIGHT_MULTIPLIER = new SlimeSizeCalculation(false, false, 0F, 1F, 1F, 0F, 0, 0);
    public static final SlimeSizeCalculation DEFAULT_EXPERIENCE = new SlimeSizeCalculation(false, false, 0.76F, 0F, 1F, 0F, 0, 0);


    private final boolean isSizeSquared;
    private final boolean isCalculationSizeSquared;
    private final float sizeMultiplier;
    private final float sizeModifier;
    private final float resultMultiplier;
    private final float resultModifier;
    private final int resultRandomMultiplierBound;
    private final int resultRandomModifierBound;

    public SlimeSizeCalculation(boolean isSizeSquared, boolean isCalculationSizeSquared, float sizeMultiplier, float sizeModifier, float resultMultiplier, float resultModifier, int resultRandomMultiplierBound, int resultRandomModifierBound) {
        this.isSizeSquared = isSizeSquared;
        this.isCalculationSizeSquared = isCalculationSizeSquared;
        this.sizeMultiplier = sizeMultiplier;
        this.sizeModifier = sizeModifier;
        this.resultMultiplier = resultMultiplier;
        this.resultModifier = resultModifier;
        this.resultRandomMultiplierBound = resultRandomMultiplierBound;
        this.resultRandomModifierBound = resultRandomModifierBound;
    }

    public float apply(int size, Random rand){
        float calculationSize = size;
        if(isSizeSquared){
            calculationSize = size*size;
        }
        calculationSize = calculationSize*sizeMultiplier + sizeModifier;
        if(isCalculationSizeSquared){
            calculationSize = calculationSize*calculationSize;
        }
        return calculationSize*getTotalResultMultiplier(rand) + getTotalResultModifier(rand);
    }

    private float getTotalResultMultiplier(Random rand) {
        if(resultRandomMultiplierBound>0){
            return resultMultiplier * rand.nextInt(resultRandomMultiplierBound);
        }else {
            return resultMultiplier;
        }
    }

    private float getTotalResultModifier(Random rand) {
        if(resultRandomModifierBound>0){
            return resultModifier + rand.nextInt(resultRandomModifierBound);
        }else {
            return resultModifier;
        }
    }
}
