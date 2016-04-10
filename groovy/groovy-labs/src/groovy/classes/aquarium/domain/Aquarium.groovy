package domain

import domain.Fish
import domain.Basics


/**
 * Describes the type of water that can be used in a fish-based environment
 *
 */
enum WaterType {
	FRESH,
	SALT
}

class OverfilledTank extends Exception{
}

class TooManyFish extends Exception{
}

@Basics
abstract class Aquarium {
	private final WaterType waterType
	protected BigDecimal waterCapacity
	private Integer currentWaterVolume = 0
	private population = []

	abstract protected void setWaterCapacity()

	public Aquarium(WaterType waterType) {
		this.waterType = waterType
	}

	/**
	 * Adds water to an aquarium
	 * @param litres	The number of litres of water to add to the aquarium
	 * @throws OverfilledTank	If the tank would be over-filled
	 */
	public Aquarium addWater(BigDecimal litres = 1.0) throws OverfilledTank {
		if (this.waterCapacity - this.currentWaterVolume >= litres) {
			this.currentWaterVolume += litres
			return this
		} else {
			throw new OverfilledTank()
		}
	}
	
	public boolean isAtWaterCapacity() {
		if (this.waterCapacity == this.currentWaterVolume)
			true
		else
			false
	}
	
	public int getCurrentPopulationSize() {
		int total = 0
		this.population.each {
			total += it.getSize()
		}
		return total
	}

	/**
	 * Basic algorithm of 2 litres = 1 point of FishSize.
	 * For example, 10 litres of water allows for either:
	 *   - 5xFishSize.SMALL fish;
	 *   - 2xFishSize.MEDIUM and 1xFishSize.SMALL fish;
	 *   - 1xFishSize.LARGE and 1xFishSize.MEDIUM fish; or
	 *   - 1xFishSize.LARGE and 2xFishSize.SMALL fish
	 * @see FishSize
	 * @return The maximum total fish capacity for the aquarium given the current water level
	 */
	public int getFishCapacity() {
		this.currentWaterVolume / 2
	}

	public Aquarium addFish(Fish fish) {
		if (getFishCapacity() - getCurrentPopulationSize() - fish.getSize() >= 0 ) {
			this.population << fish
			return this
		} else {
			throw new TooManyFish()
		}
	}
}

/**
 * Describes an aquarium (fish tank)
 * @author labnotebook
 *
 */
@Basics(includeSuper=true)
class RectangularAquarium extends Aquarium {

	/**
	 * Dimensions for the tank
	 */
	private final BigDecimal height, width, depth


	/**
	 * @param waterType
	 * @param waterCapacity
	 * @param height
	 * @param width
	 * @param depth
	 */
	public RectangularAquarium(WaterType waterType, BigDecimal height, BigDecimal width, BigDecimal depth) {
		super(waterType)
		this.height = height
		this.width = width
		this.depth = depth
		this.setWaterCapacity()
	}

	@Override
	protected void setWaterCapacity() {
		this.waterCapacity = (this.height * this.width * this.depth) * 0.9
	}
}

@Basics
class CylindricalAquarium extends Aquarium {

	private final Integer radius, height

	public CylindricalAquarium(BigDecimal radius, BigDecimal height) {
		this.radius = radius
		this.height = height
		this.setWaterCapacity()
	}

	/**
	 * Formula: V = PI * r^2 * h
	 */
	@Override
	protected void setWaterCapacity() {
		// Uses java.math.PI
		this.waterCapacity = PI * pow(this.radius,2) * this.height
	}
}

