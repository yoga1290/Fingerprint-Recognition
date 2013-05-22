First we will convert images to greyscale as RGB(x,y)= (R(x,y)+G(x,y)+B(x,y))/3
then, follow these steps:

![Steps](readme/steps.png)

# Thinning process & generating the Skeleton:


Based on the “A Fast Parallel Algorithm for Thinning Digital Patterns” by T. Y. Zhang & C.Y. Suen, for each center point P1, it's neighboring cells will be defined by:

![definning points](readme/thinning.png)

We go through all center cells and see which cells to re-color as Black/1 based on these conditions:

* 3 <= B(P1)= P2+P3+P4+P5+P6+P7+P8+9 <= 6
* A(P1)= 1; center point must be Black/1 itself!

* FOR EVEN iterations ((x+y)%2==0):
*	P2*P4*P6=0
*	P4*P6*P8=0 , where (*) indicates ANDing or multiplying!

* FOR OLD iterations ((x+y)%2==1):
*	P2*P4*P8=0
*	P2*P6*P8=0 , where (*) indicates ANDing or multiplying!


# Minutiae extraction using Rutovitz’s Crossing-Number:

> Left/Top trimming before it might help?!

For each center point (P), values are calculated by:
![Defining CN](readme/cn.png)
![CN Values](readme/cn2.png)

# Comparing:

It's easy to create array of nodes where each has X,Y,Type & Types of neighboring cells but somehow I'm running out of heap space here even before comparing!
.:. I'm picking the nears X,Y cell with similar type!


# References:

* [ScienceDirect:"Preprocessing and postprocessing for skeleton-based fingerprint minutiae extraction",Feng Zhao,Xiaoou Tang](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.108.5844&rep=rep1&type=pdf)
* ["A Brief Introduction to Skeleton-Based Fingerprint Minutiae Extraction",Feng Zhao](http://vc.sce.ntu.edu.sg/index_files/upload/USTC_BOOK/chapter6.pdf)
* [Binarization and Thinning of Fingerprint Images by Pipelining, MVIP Tehran 2005]( http://www.academia.edu/1946178/Binarization_and_Thinning_of_Fingerprint_Images_by_Pipelining )