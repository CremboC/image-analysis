# WTSI X-Ray Image Analysis KNIME extension

Contains all the nodes for to analyse the X-Ray as well as an example workflow.

## Nodes

### Bitmask Cropper
Created to crop out specific regions using a given bitmask. 

*Non-functional* at the moment due to change of requirements.

### Bounding Box
Calculates the bounding box using given thresholds

### DicomAligner
Given an image and its centroid, calculates the angle from the object faces upwards.

### Centraliser
Given the centroid of a white blob, moves it to match the centre of the whole image

### Node Toolset
Not a node, rather a toolset used throughout all of the provided nodes in this package. Simplifies creation of new nodes.

### Region Cropper
Given specific boundaries (upper, lower, left, right) crops out the region inside these boundaries.

### Interval Thresholder
Thresholds an image using a given interval. All pixels outside the interval are assigned a given "background pixel value".

### Trend Line
Calculates the trend line in respect to the bright pixels on an image. 4 methods are availble - logarithmic, power, polynomial and exponential.

### Trend Line Cropper
Using the trend line previously calculates by the Trend Line node, crops out a specific region with a user-defined left and right margin from the actual trend line.
Also gives the possibility to choose from which row to start cropping as well as finishing row.

## Manual: Creating new nodes in this package

1. Get KNIME SDK (http://www.knime.org/downloads/overview)
2. Clone this repository and import at least **Node Toolset** as a project
3. Create a new node using the "Create a new KNIME Node-Extension" under "KNIME" in the SDK
4. Type in info, mostly likely the node type will be "Manipulator" (may choose to include sample code or better to look at the code of nodes in this package) and click Finish
5. This will create a new project which will include 5 default classes, all of which must be included. See http://tech.knime.org/developer-guide for more info about them.
6. Now you will need to do a few things to prepare this node and make it consistent with the other nodes in this package:
  1. Open plugin.xml. Go to dependencies tab and add "uk.ac.sanger.mig.analysis", this is the Node Toolset package, it contains utilities and other helpers.
  2. (*optional*) Mostly likely you will manipulate image pixels, in this case add "org.knime.knip.base" and "org.knime.knip.core"
  3. To add this node to the WTSI X-Ray Image Analysis package go to the actual plugin.xml XML editor, delete the commented XML and change it with the labled as "org.knime.workbench.repository.categories" in one of the other nodes in this package.
  4. In the same file, change the category-path from "/" to "/wtsixray/"
  5. Go to <NodeName>NodeModel which was created for you by the wizard and change the "extends NodeModel" to "extends GenericNodeModel". Now you can remove the methods *reset*, *saveSettingsTo*, *loadValidatedSettingsFrom*, *validateSettings*, *loadInternals* and *saveInternals*. The methods *execute* and *configure* must be implemented by you (even though configure can be empty).
  6. At this point it is much easier to look at an existing node to see finalise the preparations, the steps are essentially add settings to *settingsModels* and modify the <NodeName>Dialog class accordingly.

## Fixes:

2014-09-04
- add copying over settings models to manual and explain what does it do, explain different settings types
- add copying over column name and types and explain what they do
- add copying over adding components to dialog and explain them
- ~~add comments to Utils~~
- ~~change COLUMNS to COLUMN_NAMES~~

2014-09-03
- ~~Rename tailless mouse image file~~
- ~~Rename threshold & crop node in the tailless mouse path to only crop~~
- ~~Tidy up documentation of Region Cropper~~
- ~~Potentially fix up Aligner to make code more similar to newer nodes~~