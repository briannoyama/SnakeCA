# SnakeCA

## Introduction
I wrote this code as a prototype for a homework assignment for undergraduate students enrolled in an Introduction to Object Oriented Programming course. Below is a class diagram describing the overall organization:

![A Class Diagram for this repository.](/ClassDiagram.png)

As a programmer, I value shallow over deep type heirarchies, and I think that dependency injection leads to better organized code. In other words, I try to avoid implementation inheritance. I also believe that OO is best learned through design patterns. For example:

* Implementations of `clock.Updatable` use the strategy pattern.
* `graph.Cell.updateNeighbors` uses the composite pattern.
* Implementations of `state.State` let cells use the state pattern.
* `graph.GraphMapFactory` uses the factory pattern and relfection to dynamically load new classes.

I also do not pattern my code to death. By importing `java.awt.Polygon` in cells I coupled the view with the model to simplify the implementation of GraphMaps (i.e. this does not use MVC). Though I do think not using MVC adds a bit of code smell, overall I am proud of this code.

## Screenshot

![An example game running on a square lattice.](/Arena.png)


## License

Licensed under the MIT license.
