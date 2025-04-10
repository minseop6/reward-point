package com.test.rewardpoint.controller;

import com.test.rewardpoint.dto.PointCreationRequest;
import com.test.rewardpoint.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/points")
public class PointController {

    private final PointService pointService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPoint(PointCreationRequest request) {
        pointService.creatPoint(request);
    }
}
