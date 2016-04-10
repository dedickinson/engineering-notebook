import domain.RectangularAquarium
import domain.WaterType
import domain.NeonTetra

def aquarium = new RectangularAquarium(WaterType.FRESH, 1, 1, 1)
aquarium.addWater(0.5).addFish(new NeonTetra())

println aquarium.toString()
