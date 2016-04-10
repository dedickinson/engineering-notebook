interface TwoDimensionalShapeInterface {
    Integer getArea()
}

abstract class TwoDimensionalShape implements TwoDimensionalShapeInterface {    
    @Override
    String toString() {
        "The shape is a ${NAME} with an area of ${getArea()}"
    }
}

class Square extends TwoDimensionalShape {
    
    private final String NAME = "square"
    private Integer length
    
    Integer getArea() {
        return length * length
    } 
}

mySquare = new Square(length:10)
println mySquare

import java.lang.Math
class Circle extends TwoDimensionalShape {
    private final String NAME = "circle"
    private Integer radius
    
    Integer getArea() {
        Math.PI * Math.pow(radius, 2)
    }
}
myCircle = new Circle(radius:6)
println myCircle

class RightAngledTriangle extends TwoDimensionalShape {
    private final String NAME = "right angled triangle"
    private Integer a, b
    
    Integer getArea() {
        (a * b) / 2
    }
    
    Integer getHypotenuse() {
         Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2))
    }
    
    @Override
    String toString() {
        super.toString() + " and an hypotenuse of ${getHypotenuse()}"
    }
}

RightAngledTriangle myTriangle = new RightAngledTriangle(a:3, b:4)
println myTriangle

areaCompare = { shape1, shape2 -> 
    if (shape1.getArea() > shape2.getArea()) return "Shape 1"
    return "Shape 2"
}

println "The shape with the largest area is: " + areaCompare(mySquare, myTriangle)