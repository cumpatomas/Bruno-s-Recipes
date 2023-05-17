package com.cumpatomas.brunosrecipes.core.ex


// funcion para quitar acentos de un String en español
fun CharSequence.unaccent(): String {
    var outString = ""
    for(char in this)

        when(char) {
            'á'-> outString += 'a'
            'é'-> outString += 'e'
            'í'-> outString += 'i'
            'ó'-> outString += 'o'
            'ú'-> outString += 'u'
            else -> outString += char
        }
    return outString
}

