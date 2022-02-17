## How to use
The program reads CSV (.csv) files, chosen from the user (with the help of a file chooser build in the program) which have a specific format.

## Formats supported
- 4 columns with the respective fields: EmployeeID, ProjectID, DateFrom, DateTo
- The values for DateFrom and DateTo need to be of a specific type, since only one date format is supported: yyyy-MM-dd

## Additional Features
- In addition to the statistics which are represented in the form of a table, there are 2 other buttons:
  * The button in the upper-left corner is to load a newly selected .csv file
  * The button in the lower-right corner is to preview the statistics in the form of a Pie Chart

## Known bugs
- If two employees worked on the same project and have both their DateFrom and DateTo set to the same date, in the Statistics section this will be displayed with a value of 0 days teamwork. This, however, will not be displayed in the Pie Chart.
