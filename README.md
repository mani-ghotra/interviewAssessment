An Android app that displays a list of countries fetched from a JSON API.
The app demonstrates modern Android development practices using Kotlin, RecyclerView with DiffUtil, and MVVM architecture.

Features
Fetches a list of countries in JSON format.
Displays countries in a RecyclerView with:
Country Name
Region
Code
Capital

Orientation-aware layout:
Portrait → Linear layout
Landscape → Grid layout

Live search:
Filter countries by name, capital, region, or code
Updates results as you type

Scroll position preservation on rotation
Empty state handling:
Shows “No results found” if the search does not match

Edge-to-edge layout for modern full-screen display
Error handling via Toast messages
