package com.cumpatomas.brunosrecipes

import com.cumpatomas.brunosrecipes.core.ex.unaccent
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CharExKtTest : ExampleUnitTest() {

    @Test
    fun unaccentTest() {

        val result = "ésto ás un éjémpló".unaccent()
        assertThat(result).isEqualTo("esto as un ejemplo")
    }
}