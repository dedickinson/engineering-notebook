package domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import domain.WaterType
import domain.Basics

enum FishSize {
	SMALL (1),
	MEDIUM (2),
	LARGE (3)
}

/**
 * @author labnotebook
 *
 */
interface Fish {
	/**
	 *
	 * @see http://en.wikipedia.org/wiki/Binomial_nomenclature
	 * @return The binomial name of the fish
	 */
	public String getBinomialName()

	/**
	 * @return
	 */
	public String getCommonName()

	/**
	 *
	 * @return
	 */
	public WaterType getWaterType()

	/**
	 * @return
	 */
	public FishSize getSize()
}

@Basics
class NeonTetra implements Fish {
	static final String BINOMIAL_NAME = 'Paracheirodon innesi'
	static final String COMMON_NAME = 'neon tetra'
	static final WaterType WATER_TYPE = WaterType.FRESH
	static final FishSize SIZE = FishSize.SMALL

	public NeonTetra() {}
	
	/* (non-Javadoc)
	 * @see com.github.labnotebook.groovylabs.aquarium.Fish#getBinomialName()
	 */
	@Override
	public String getBinomialName() {
		// TODO Auto-generated method stub
		BINOMIAL_NAME
	}

	/* (non-Javadoc)
	 * @see com.github.labnotebook.groovylabs.aquarium.Fish#getCommonName()
	 */
	@Override
	public String getCommonName() {
		COMMON_NAME
	}

	/* (non-Javadoc)
	 * @see com.github.labnotebook.groovylabs.aquarium.Fish#getWaterType()
	 */
	@Override
	public WaterType getWaterType() {
		WATER_TYPE
	}

	/* (non-Javadoc)
	 * @see com.github.labnotebook.groovylabs.aquarium.Fish#getSize()
	 */
	@Override
	public FishSize getSize() {
		SIZE
	}
}

/**
 * @author labnotebook
 *
 */
/*
class ClownFish implements Fish {
	static final String BINOMIAL_NAME = 'Amphiprion ocellaris'
	static final String COMMON_NAME = 'Common clownfish'
	static final WaterType WATER_TYPE = WaterType.SALT
	static final FishSize SIZE = FishSize.SMALL
	
	private ClownFish(){}
}
*/
