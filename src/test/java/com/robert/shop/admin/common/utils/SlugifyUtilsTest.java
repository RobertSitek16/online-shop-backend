package com.robert.shop.admin.common.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlugifyUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "photo photo.png, photo-photo.png",
            "hello world!.jpg, hello-world.jpg",
            "ąłęóść.png, aleosc.png",
            "Product 1.jpg, product-1.jpg",
            "Product  4.jpg, product-4.jpg",
            "Product___3.jpg, product-3.jpg"
    })
    void shouldSlugifyFileName(String in, String out) {
        String fileName = SlugifyUtils.slugifyFileName(in);
        assertEquals(fileName, out);
    }

}