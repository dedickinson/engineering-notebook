class Car {
    def drive() {
        println 'Broom broom'
    }
}

class RaceCar extends Car {
    def driv() {
        println 'Zoom Zoom'
    }
}

class HoverCar extends Car {
    @Override
    def drive() {
        println 'Woop woop'
    }
}

def myCar = new RaceCar()
myCar.drive()

def futureCar = new HoverCar()
futureCar.drive()