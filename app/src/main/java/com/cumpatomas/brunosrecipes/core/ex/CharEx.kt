package com.cumpatomas.brunosrecipes.core.ex


// funcion para quitar acentos de un String en español
fun CharSequence.unaccent(): String {
    when {
        this.contains('á')-> return this.toString().replace('á', 'a')
        this.contains('é')-> return this.toString().replace('é', 'e')
        this.contains('í')-> return this.toString().replace('í', 'i')
        this.contains('ó')-> return this.toString().replace('ó', 'o')
        this.contains('ú')-> return this.toString().replace('ú', 'u')
    }
    return this.toString()
}