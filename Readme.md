## Recetas de Bruno

This is a Real Food recipes app that will help the users to decide what to cook according to the ingredients they have at home.

This app was designed first in classical Views and XML style but later, after descovering the great framework JetPack Compose
I migrated some of the screens to this wonderful new tool and ended up been a hybrid app.

In the Home Screen I implemented a horizontal Help scroll menu using a Lazy Row and zooming the central main item to help the user understand the app main functions.

This reactive menu will be replaced by the best rated recipes once the user puts rating stars to a recipe, suposing the user has already understood the app flow.

https://user-images.githubusercontent.com/102058754/231946102-1fb81bfb-30e1-46ed-95da-1e1c2845c86b.mp4

In the lower part of the Home Screen in the News Section I've used the Jsoup library to scrap in real time the articles titles and links. This content is from a public blog about nutrition and healthy food and the order of the titles is in a random mode.

For display the news I embebed a WebView surface with a loading animation and a close button to collapse the webview.

https://user-images.githubusercontent.com/102058754/231947753-dc0c6eeb-8e60-464b-8168-ff5230f412e5.mp4

In the second screen there is the main Recycler View with a Text Input to search recipes by name and below we have chip filters to sort recipes by category (summer, winter, salty or sweet). This screen was developed in classical views and XML style.

--------------------

The recipes information comes from a JSON file loaded in Firebase and I get it using Retrofit and saving the list in the local DB using Room creating an entity locally.

Te recipe main screen has the photo from a link, a rating star (made in views), the ingredients, category and last date the recipe has been cooked. After the steps to cook the recipe I added a button to save in the local DB the actual date the disjh has been cooked. So then we have a History screen where we can find the recipes and the date it has been cooked listed in a Lazy Column in Compose.

----------------------------

Finally the last screen is the What to cook? section. Here the users can input the ingredients they have at home selecting from a sorted list of the ingredients we have captured from the recipes. Once the selected ingredients match a recipes a button will appear and pressing it will open the Bottom Drawer menu from below showing another Lazy Column with the recipes you can actually can cook.

----------------------------







