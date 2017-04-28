Do skonfigurowania projektu potrzebny jest klucz -> klucz API dla google maps api:
https://developers.google.com/maps/documentation/javascript/get-api-key
trzeba go pobrac z tego linku wpisujac nazwe tworzonego projektu
Nastepnie w gradle.propertis nalezy dodac cos takiego:

GOOGLE_MAPS_KEY=AIzaSyBVJKKSwTgwoX0Sp8FhXjUbi7uuUxnBQSM

oraz w manifests:

<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="YOUR_GOOGLE_KEY" />

jesli dalej beda problemy z autoryzacja nalezy ten klucz podac w manifests:

<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="AIzaSyBVJKKSwTgwoX0Sp8FhXjUbi7uuUxnBQSM" />

Po wiecej szczegó³ów odsy³am tutaj:
https://developers.google.com/maps/documentation/javascript/tutorial
szczególnie do sekcji 'Get API Key'

