## Recetas de Bruno
Aplicación de recetas real food que ayudará al usuario a saber qué cocinar según los ingredientes que tiene en casa.

Esta app fue diseñada primeramente en Views y XML pero al descubrir Jetpack Compose he hecho la migración de algunos 
screens por lo que ha resultado en una app híbrida.

En la pantalla principal he implementado un scroll menu horizontal usando Lazy Row y magnificando el ítem central.

Este menú desaparecerá dando lugar a las recetas mejor valoradas. Lo hice para implementar un reactive menú suponiendo que 
el usuario al puntuar una receta ya esta al tanto de cómo funciona la app. En caso de que quite la puntuación
de todas las recetas volverá a aparecer el menú de ayuda.



https://user-images.githubusercontent.com/102058754/231946102-1fb81bfb-30e1-46ed-95da-1e1c2845c86b.mp4



Más abajo en la sección Noticias he utilizado la librería Jsoup para hacer scrapping (en tiempo real) de los títulos de los artículos y el link de un blog público sobre nutrición haciendo el orden aleatorio cada vez que se ingresa a la app.
Para desplegar la noticia utilicé un WebView con un Loading Animation hecho en Compose mientras carga la URL y un Close Button una vez cargada.


https://user-images.githubusercontent.com/102058754/231947753-dc0c6eeb-8e60-464b-8168-ff5230f412e5.mp4




