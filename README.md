# Android home assignment

Use the supplied Android project (gradle) template to implement the following requirements: 

## General Note
* You should expect the total of ~ 10 hours of work effort.    
* If we find flaws, expect us to kindly ask you for a fix, we believe in second chance.  

## Requirements

* The app should fetch remote data from three endpoints (data is in JSON format).
Each endpoint returns a different format of JSON data, that contains an image url, title and a subtitle/text.
Each endpoint can return **more than one result**.
* The predefined URLs for the different endpoints are to be found under ```Consts``` class.
* The data should be parsed, aggregated and presented in a predefined ```RecyclerView``` in ```MainActivity```
* The layout for each item in the ```RecyclerView``` is already written for you at res/layout/card_item.xml
* ```card_item.xml``` contains ```ImageView``` and two ```TextView``` one for the title and the other for subtitle/text, which you will populate from the data you fetched and parsed from the different servers.
* Data should be presented in the ```RecyclerView``` **only** when **all** of the data is fetched from the different endpoints.
  (You can show a cool progress bar meanwhile)
* Add a caching layer for each data source. 
  * Cache stale times:
      * Source A - 5 min
      * Source B - 30 min
      * Source C - 60 min  
  
* Please Make sure your code is easily extensible for any future data endpoints, easy to understand for future developers, and beautiful!
* Feel free to make the UI as delightful as you like (animations and so on).

## Specifications
Please find below the specs for each Source's data.
##### Data source A:
Json sample:
```json
{
  "stories": [
    {
      "title": "Interesting News!",
      "subtitle": "You're not gonna believe this...",
      "imageUrl": "https://pbs.twimg.com/profile_images/658218628127588352/v0ZLUBrt.jpg"
    }
    ...
    ]
}
```

##### Data source B:
The relevant fields here are "header", "description" and "picture".

```json
 {
   "metadata": {
     "this": "isnotimportant",
     "innerdata": [
       {
         "aticleId": 1,
         "articlewrapper": {
           "header": "WOW",
           "description": "SHIGAON"
         },
         "picture": "https://pbs.twimg.com/profile_images/2084187780/avib_400x400.jpg"
       }
       ...
       ]
   }
 }            
```
    
##### Data source C:
subtitle here is a concatintation of subLine1 + subLine2.

```json
[
  {
    "topLine": "Latest article",
    "subLine1": "And here is the ",
    "subline2": "subtitle!",
    "image": "http://c-sf.smule.com/s25/arr/1d/4c/f0cf4342-e875-4601-b47e-c74a8d021d8d.jpg"
  }
  ...
]
```

## Important
You are encouraged to use any 3rd party library that you like working with, i.e. [Retrofit](http://square.github.io/retrofit/), as long as you trust it.
We expect you to be accountable for each file and each line of code you submit

## Bonus

* We love animations! Add some cool stuff to the RecyclerView and wherever you feel it fits.
* We believe in unit testing, show us that you are a believer as well.   
 
