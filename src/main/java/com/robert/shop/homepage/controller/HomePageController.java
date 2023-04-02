package com.robert.shop.homepage.controller;

import com.robert.shop.homepage.dto.HomePageDto;
import com.robert.shop.homepage.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomePageController {

    private final HomePageService homePageService;

    @GetMapping("/homePage")
    public HomePageDto getHomePage() {
        return new HomePageDto(homePageService.getSaleProducts());
    }

}
