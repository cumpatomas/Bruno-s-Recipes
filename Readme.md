## Recetas de Bruno

This is a Real Food recipes app that will help the user to decide what to cook according to the ingredients he has at home.

This app was designed first in classical Views and XML style but later, after descovering the great framework JetPack Compose
I migrated some of the screens to this wonderful new tool and ended up been a hybrid app.

In the Home Screen I implemented a horizontal Help scroll menu using a Lazy Row and zooming the central main item to help the user understand the app main functions.

This reactive menu will be replaced by the best rated recipes once the user puts rating stars to a recipe, suposing the user has already understood the app flow.

https://user-images.githubusercontent.com/102058754/231946102-1fb81bfb-30e1-46ed-95da-1e1c2845c86b.mp4

In the lower part of the Home Screen in the News Section I've used the Jsoup library to scrap in real time the articles titles and links. This content is from a public blog about nutrition and healthy food and the order of the titles is in a random mode.

For display the news I embebed a WebView surface with a loading animation and a close button to collapse the webview.

https://user-images.githubusercontent.com/102058754/231947753-dc0c6eeb-8e60-464b-8168-ff5230f412e5.mp4




