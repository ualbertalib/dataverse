# Dataverse-Data-Explorer
The Data Explorer provides a graphical user interface (GUI) which lists the variables in a tabular data file and allows users to search, chart, and conduct cross tabulation analysis.

This stand-alone component is built to complement [The Dataverse Project](http://dataverse.org/). The Data Explorer is integrated as part of the dataset.xhtml and file.xhtml pages, revealing an **Explore** button for tabular data files upon installation.

Here's a demo of the tool in action [https://scholarsportal.github.io/Dataverse-Data-Explorer/?siteUrl=https://dataverse.scholarsportal.info&fileId=8988](https://scholarsportal.github.io/Dataverse-Data-Explorer/?siteUrl=https://dataverse.scholarsportal.info&fileId=8988)

### Installation
To use the Data Explorer as part of your Dataverse installation simply download the config file [https://scholarsportal.github.io/Dataverse-Data-Explorer/dataExplorer.json](https://scholarsportal.github.io/Dataverse-Data-Explorer/dataExplorer.json) to your local computer and then call the following curl command
``curl -X POST -H 'Content-type: application/json' --upload-file dataExplorer.json http://localhost:8080/api/admin/externalTools``

The config file [https://scholarsportal.github.io/Dataverse-Data-Explorer/dataExplorerLocal.json](https://scholarsportal.github.io/Dataverse-Data-Explorer/dataExplorerLocal.json) can be used for local installations assuming the Data Explorer application is placed in the Dataverse project directory {Dataverse Dir}/src/main/webapp/ddi_explore 


See [http://guides.dataverse.org/en/4.8.6/installation/external-tools.html](http://guides.dataverse.org/en/4.8.6/installation/external-tools.html) for further information.

### Main Interface
The main interface displays the first 10 variables from the data file. 
Paging controls on the bottom left allow you to view additional variables and a dropdown on the bottom right allows you to control the number of variables to display per page.
The search field allows you to search across ID, Name and Label, updating the interface as you type.

## Chart View
Clicking within the row of a variable will display a chart for the data in the "Chart View" tab on the right. Pie charts and bar charts will be automatically displayed based on the variable type.
Clicking on another variable will update the "Chart View" tab with the newly selected variable on top, pushing previous selections below for comparison.
Clicking on a selected variable will deselect it and clicking the "X" to the right of the tabs deselects all the selected variables.
Summary statistics are also provided in the "Chart View" tab below the chart.

![alt text](https://user-images.githubusercontent.com/34171553/36049600-5c543cc4-0db1-11e8-8c1e-e2a2ccfe74d4.PNG)

## Table View
Next to the "Chart View" tab (revealed when a variable is selected), is the "Table View" tab. 
Clicking the "Table View" tab will display a cross tabulation of the selected variables.
By default selected variables are shown along a row in the cross tabulation.  
Variables can be switched to columns in two ways: 1) you can drag the four-way arrow icon to the empty column on the left of the cross tabulation table and release when the column darkens, or 2) you can click the "Add as Column" icon (beneath the "Add as Row" icon) on the left of the desired variable row.
With no variable selected, clicking either of these icons will display the "Table View" tab directly.
Below the table, z-scores provide a measure of the number of standard deviations above or below the population mean.

![alt text](https://user-images.githubusercontent.com/34171553/36048745-4d287d30-0dae-11e8-843d-c1f55eabace6.PNG)

### Z-score
For convenience the z-score (also know as the standard score) of each value in the table view has been visualized to 2 standard deviations. The z-score is calculated by taking the value (x) minus the mean (µ) which is then divided by the standard deviation (σ). The z-score of each value is then divided by 2 and coloured using the resultant value.

## Downloading Data
The "Download" dropdown can be found near the top of the interface. The first option "Download Subset" becomes enabled when one or more variables are selected.
All other options remain enabled regardless of selection.


### Reference
This project has been adapted from the Ontario Data Documentation, Extraction Service and Infrastructure known as [ODESI](odesi.ca)
