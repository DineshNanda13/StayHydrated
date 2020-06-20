# StayHydrated
> *Test-driven development (TDD)*
* ## Introducation
Stay Hydrated is a single-view app which allows users to keep track of their daily
water consumption and encourages them to drink the 2500mL recommended
intake.
* ## Tools And Libraries Used:
1. SQLite Database
2. jUnit And Espresso Library
3. ListView
4. Custom Adapter
and more.

* ## Application Functions
>The app consists of three buttons which each represent a certain amount,
respectively 30mL, 200mL and 350mL, are represented by corresponding
symbols, a droplet, a glass and a bottle.
Whenever the user clicks one of the buttons, his intake is added to the history
list and the progress bar at the bottom of the screen fills in accordingly.  
The app save the list of intakes in a database and reload it on opening.
Since the app calculates daily intake, `the data from the previous day is dropped
everytime we start the app on a new day`.

### Two Integration Tests Are Implemented For Each Of These Function

```
Feature #1
```
>When one of the three images at the top of the view is clicked by the user, an
element should be added to the list of intakes. The icon of the element added to
the list should correspond to the button clicked.

<img src="https://github.com/DavinderSinghKharoud/Images/blob/master/emptyList.png" width="180" height="370"><img src="https://github.com/DavinderSinghKharoud/Images/blob/master/dropOfWater.png" width="180" height="370">  
  When clicking the droplet, an item is added to the list with the droplet symbol.
```
Feature #2
```
>Items displayed in the list should display two values.
The top line of text should be the quantity: it should be 50 mL for the droplet
button, 200 mL for the glass button and 350 mL for the bottle button.
The bottom line of text should display a timestamp of when the button was
pressed. The time should take format “HH:MM - DD/MM”.

<img src="https://github.com/DavinderSinghKharoud/Images/blob/master/dropOfWater.png" width="180" height="370">  
The intake is displayed in mL and timestamp is displayed below.

```
Feature #3
```
>When a button is clicked, either 50, 200 or 350 mL, the progress bar should fill in.
The progress bar should be out of 2500 and fill proportionally to the amount
represented by the button pressed.
If the user adds more than 2500 mL to the list, the progress bar should simply
be displayed as full.

<img src="https://github.com/DavinderSinghKharoud/Images/blob/master/bottleWater.png" width="180" height="370"><img src="https://github.com/DavinderSinghKharoud/Images/blob/master/fullList.png" width="180" height="370">   
When user click on the bottle icon 350mL is filled out of 2500mL, If the user adds more than 2500mL, the progress bar would be displayed as full.

```
Feature #4
```
>In case the user makes a mistake, they can double click a value in the list to
remove it. This should both remove the item from the list and update the
progress bar which should always fit the total amount in the list.

<img src="https://github.com/DavinderSinghKharoud/Images/blob/master/bottleWater.png" width="180" height="370"><img src="https://github.com/DavinderSinghKharoud/Images/blob/master/emptyList.png" width="180" height="370">   
When user double click on the 350mL item view, it will be removed from the database.
