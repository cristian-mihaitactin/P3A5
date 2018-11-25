package com.uaic.ai.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segmentation")
public class SegmentationController {



    @GetMapping("/somethingGET")
    public String getSomething() {
        return "somethingggggggggg";
    }

    @PostMapping("/somethingPOST/{postedByMe}")
    public String postSomething(@PathVariable String postedByMe) {
        return "you posted " + postedByMe;
    }


}
