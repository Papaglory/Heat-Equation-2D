## Description

This program numerically solves the 2D heat equation using the finite difference method, approximating the heat distribution over a square domain with having value zero for the boundary conditions. The initial heat distribution is modeled by a polynomial spline surface constructed from user-defined input points in 3D-space.

## How to Use

No app exists for this project (apart from three demos in the 'executables' folder). To test with your own examples, download the code and run MainHeat.java. The following steps display how to proceed.

There is no standalone application for this project, but you can test it with your examples by downloading the code and running MainHeat.java. Follow these steps:

1. Adjust the desired settings in MainHeat.java, including parameters for graphical display (e.g., window size, pixel size), heat equation parameters (such as the thermal diffusion constant alpha), numerical parameters (e.g., time step, mesh size), and animation parameters.

2. Input at least one point to construct the spline surface representing the initial heat distribution at time 0.

3. Run 'MainHeat.java'.

## Implementations
### Hermite Spline interpolation

The program utilizes Hermite spline interpolation to construct a single spline surface over the entire domain without stitching. The spline interpolation is a weaker implementation of a general spline surfare, that is, it does not model the interaction between the x and y varaibles. Non-linear relationships between x and y can thus not be modeled (there are no terms on the form x^ay^b where a,b are positive integers). The result is a potensial less accurate spline model in cases of non-linearity between x,y.

### Finite Difference Method

The implementation of the finite difference method may exhibit instability, particularly when using high thermal diffusion constants or inappropriate time step values. Careful adjustment of parameters is advised to mitigate artifact issues.
		
### Drawing

The program renders the heat distribution by assigning grayscale colors to pixels based on temperature values. To ensure optimal use of the grayscale range, temperatures are scaled accordingly. Additionally, a natural dimming effect over time is achieved by adjusting pixel intensities relative to the highest temperature in the initial iteration.

## Dependencies

The java code relies on the javax.swing package for graphical rendering and requires the EJML Java library (version 0.43) for matrix calculations.

## Preview
<img src="assets/preview.gif" alt="Alt Text" width="330" height="400" />

## Author
Marius H. Naasen, originally created August 2021.
Marius H. Naasen, originally created August 2021.
