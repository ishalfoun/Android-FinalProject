# Final Project: Dawson Student App 2017

## Members
* [@theoathomas - Theodore Acco-Thomas](https://github.com/theoathomas)
* [@ishalfoun - Issak Shalfoun](https://github.com/ishalfoun)
* [@jacobr98 - Jacob Riendeau](https://github.com/jacobr98)

## Project Overview

**DangerousClub** is intended to be useful for new and current Dawson students. The purpose of this project is to create an application that will exercise what you have learned about the Android framework  and Mobile software development.  It is to be integrated with your final project for PHP. In phase 1 you will implement most app only components, in phase 2 you will integrate with your PHP and PostgresSL server side data.    

##General functionality

On startup
- [X] 1st launch: ask for last name, first name, email address and password  save it in shared preferences with date stamp,  (phase 2 will involve checking the back end web REST resources from the PHP project for updates and downloading data, using this information)
- [X] Subsequent launch you will not ask for authentication, the auth data will be used for access to the back end.  
General
- [X] Unique App Name: Dawson<TeamName>
- [X] Unique repo name: must include TeamName
- [X] Unique Launcher icon, (you may make your own)  
- [X] Include appropriate inline documentation and JavaDocs for your methods and classes.
- [X] All images should be scalable (png, or jpg) and must resize on rotate
- [X] All UI strings and integers must be resources, no constants.
- [X] All code-internal data must be constants, no literals, even for log TAGs.
- [X] Use logging with a unique tag for each Activity/Fragment
- [X] You are required to use Fragments in at least one UI
- [X] Maintain state on rotation and wherever necessary for all UIs


We may not use all of the following apis, you may delay getting keys until you need them.
- [X] Sign up for an API key for Open Weather Map https://openweathermap.org/api
❏	Sign up for an API key for Google Places  https://developers.google.com/places/android-api/start  
❏	Use PlacePicker https://developers.google.com/places/android-api/placepicker
❏	Use CurrentPlace https://developers.google.com/places/android-api/current-place

## Specifications

Menu -> About,  Dawson, Settings 
- [X] About -> Launches About Activity
- [X] Dawson -> Launches Dawson Computer Science web page
- [X] Settings -> Launches Settings Activity 

About Activity show the App Credits:  
- [X] Paragraph describing the app, format the text in a non black colour and with a largish font “Dawson College”  Course Id, 
 Authors names, mages of each or all of you together. 
- [X] onclick Course Id -> Launches Dawson Computer Science web page
- [X] onclick Your name or image -> a Dialogue with a blurb about you.  Grad Dawson CS 2017 etc

Settings Activity show any settings saved for the app
- [X] First and last name, email address, password and date stamp (when created or modified) and any others you deem necessary.  If changes are made update the date stamp. 
- [X] Implement a save button, if back is pressed launch a dialogue to confirm discard.
- [X] Dawson Image -> Launches Dawson Computer Science web page  
- [X] Logo Image -> Launches About Activity  

CanceledActivity using the RSS feed from https://www.dawsoncollege.qc.ca/registrar/online-services/class-cancellations/
- [X] CanceledActivity is an Activity/Fragment to display the canceled classes from the feed.
○	widgets as needed, use an AdapterView to show the course+section number.
- [X] AdapterView onItemClick show all of the information for this course> ShowCancelActivity
- [X] Add onItemLongClick it will invoke a search for friends in this course  using the data from the API
- [X] Display all friends in this course in an AdapterView or pop up a dialog saying “no one available”   The onItemClick will invoke an email intent subject: <appname>  

ShowCancelActivity is an Activity/Fragment to display the full information from the RSS feed for the item selected.  
- [X] Implement a button or click on the teacher name  to display Teacher Contact info (same fragment asTeacherContactActivity)

Find Teachers-> FindTeacherActivity (use Fragments)
(you are given a phone json file, you must import it into firebase to create a database and use it here)
- [X] FindTeacherActivty an Activity/Fragment lookup the teacher in the phone directory.   (use first and last name)
- [X] Widgets as needed for search fields of first name and last name.
- [X] Validate that at least one is set for the search. 
- [X] search button to launch the search
- [X] a radio button for like/exact search (use appropriate searches)
- [X] Results only show after the search 
 	>1 result ChooseTeacherActivity Activity/Fragment
 	==1 result TeacherContactActivity Activity/Fragment
- [X] ChooseTeacherActivity Display list of teacher’s retrieved in the search previous
- [X] ListView (or other AdapterView) to show the teacher first & last names
    onItemClick > TeacherContactActivity
- [X] TeacherContactActivity  display all information from the firebase database for this teacher
 	The fragment will be used in two places 
 	Widgets as needed for all fields
 	onClick for email address launches an implicit intent for email 
	subject:  “from <yourappname>”
 	onClick for local launches an implicit intent to make a phonecall tel://number,local
	See https://developer.android.com/guide/components/intents-common.html
 
School Notes->  NotesActivity  
- [X] NotesActivity  Show existing or add new notes from the sqlite db  Activity/Fragment.  
- [X] widgets for new notes input and a button to add a note
- [X] Use an AdapterView to show the first few chars of the notes in the db.
- [X] onItemClick will launch ItemNoteActivity to display the whole note
- [X] ItemNoteActivity  display a single note from the notes db
- [X] Widget with large text to display the notes (you may need a ScrollView)


Add To Calendar ->  CalendarActivity
- [X] CalendarActivity Activity/Fragment add an event to the calendar
- [X] widgets as needed for date, start time, end time & event
- [X] Add the event to the default calendar https://developer.android.com/guide/topics/providers/calendar-provider.html#add-event
- [] optional: create a Dawson calendar https://developer.android.com/guide/topics/providers/calendar-provider.html#insert-calendar
Academic Calendar >  Load the url for the academic calendar in a WebView
- [X] By default load the current term (fall 2017) have 
- [X] idgets to allow for entering the year and a  radio button for fall/winter and a Button.  
- [X] Change the widget in the WebView  when the button is hit.

Current Temperature... (text, not clickable)  display the current temperature.
- [X] You will use the Location Manager to obtain the device’s current latitude and longitude. (and a LocationListener to keep it up to date) This is a pretty good tutorial  http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
	Google is pushing towards using Google Play services to obtain this information but getting the information directly from GPS is preferrable to having everything tracked by Google and required for this assignment.
- [X] You will need to make sure & check that GPS is on at least for your app Settings” > “Location Access” > “Access my location”
- [X] Use the openweather api + lat/long to get the current temperature in centigrade.  
- [X] Format it in a large text & display the temperature in centigrade.
●	https://openweathermap.org/current

Weather Activity
- [X] Have a widget to input the city & use a spinner for the ISO 3166  country codes 
- [X] Default to Montreal,CA
- [X] Have a button to display the UV index and the 5 day forecast 
- [X] This can be in the same Activity/Fragment or launch a new one
●	https://openweathermap.org/forecast5
●	https://openweathermap.org/api/uvi

PHASE 2
FindFriends -> AdapterView showing all friends, data retrieved via the API
- [X] onItemClick find where the friend is at this time (or “Unknown Whereabouts”)  retrieve data via the API 
- [X] Display all information returned by the API
- [X] This can be two separate Activities / Fragments  or all in one, design is up to you 

WhosFree -> find friends on a break 
●	Display input fields for
- [X] Day (Monday to Friday)  Radio, spinner or …
- [X] Start time 
- [X] End time
- [X] A find button should invoke the appropriate API
- [X] Display all friends available in an AdapterView or pop up a dialog saying “no one available”   
- [X] The onItemClick will invoke an email intent subject: <appname>  
●	This can be two separate Activities / Fragments  or all in one, design is up to you 

Grading  
(same as the PHP side of the project)
Part of your grade will reflect how you are able to organize your work and advance during the time that has been allocated. You must be able to show progress (procrastinators will be penalized). Please be prepared to attend your scheduled lab session for spot checks.



