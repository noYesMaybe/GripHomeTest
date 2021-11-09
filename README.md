# Grip Home Test

This is an app that displays a list of Pokémon with images and names. When the
user selects a Pokémon, the app shows the detail with the Pokémon name, images,
stats and type (fire, poison, etc.).

The application uses APIs from the following address: https://pokeapi.co

# Implementation choices and reasons
* UI Architecture pattern - MVVM (Model-View-ViewModel), LiveData, ViewModel
  * Keep data model independent from the UI and views
  * No more manual lifecycle handling and no more crashes due to stop activities.
  * Used this pattern because it separates the data from the views, and eliminates the lifecycle-related problems.
    Once the view is destroyed, for example on configuration change, the data is kept making the application more efficient and responsive.
    One more advantage is that the ViewModel and Live data are responsible for updating the views.
* Retrofit: Used this library as HTTP client to communicate with the REST endpoint. Very convenient and easy to use.
* Fragment: Used Fragments for Pokemon list and for showing the Pokemon details because the whole user story is one logical component, 
  the output from the list is the input for the details.
  Fragments are sharing the same LiveData model so the passing of data was possible through one ViewModel, 
  which kept data model independent from the Fragments.
* Implemented loading of pokemon in list by batches of 10 pokemon per request.
  Also specified the maximum number of pokemon in list to 50, no specific reason for that just thought the 50 would be enough for showing. Can be changed anytime.
* Java and Kotlin: the list implementation is written in Kotlin and the detail screen implementation
  is written in Java.
  

# Areas of improvement
* Add unit tests
* Add local data persistence
* Implement error handling
* Add notification when there is no more data
* General layout improvements\
* Remove the limit of 50 Pokémon to display
