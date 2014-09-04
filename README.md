# WTSI X-Ray Image Analysis KNIME extension #

Contains all the nodes for to analyse the X-Ray as well as an example workflow.

## Bitmask Cropper ##
Created to crop out specific regions using a given bitmask. 

*Non-functional* at the moment due to change of requirements.

## Bounding Box ##
Calculates the bounding box using given thresholds

## DicomAligner ##
Given an image and its centroid, calculates the angle from the object faces upwards.

## Node Toolset ##
Not a node, rather a toolset used throughout all of the provided nodes in this package. Simplifies creation of new nodes.

## Region Cropper ##
Given specific boundaries (upper, lower, left, right) crops out the region inside these boundaries.

Fixes:

2014-09-04

2014-09-03
- ~~Rename tailless mouse image file~~
- ~~Rename threshold & crop node in the tailless mouse path to only crop~~
- ~~Tidy up documentation of Region Cropper~~
- ~~Potentially fix up Aligner to make code more similar to newer nodes~~

