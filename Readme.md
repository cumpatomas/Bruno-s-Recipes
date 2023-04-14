## Recetas de Bruno
Aplicación de recetas real food que te ayudará a saber qué cocinar según los ingredientes que tienes en casa.

Esta app fue diseñada primeramente en Views y XML pero al descubrir Jetpack Compose he hecho la migración de algunos 
screens por lo que ha resultado en una app híbrida.

En la pantalla principal he implementado un scroll menu horizontal usando Lazy Row y magnificando el ítem central.

Este menú desaparecerá dando lugar a las recetas mejor valoradas. Lo hice para implementar un reactive menú suponiendo que 
el usuario al puntuar una receta ya esta al tanto de cómo funciona la app. En caso de que quite la puntuación
de todas las recetas volverá a aparecer el menú de ayuda.





