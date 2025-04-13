package com.test.rewardpoint.controller;

import com.test.rewardpoint.dto.PointCancellationRequest;
import com.test.rewardpoint.dto.PointCreationRequest;
import com.test.rewardpoint.dto.PointUsingRequest;
import com.test.rewardpoint.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public void createPoint(@RequestBody PointCreationRequest request) {
        pointService.creatPoint(request);
    }

    @DeleteMapping("/{pointId}")
    public void withdrawPoint(@PathVariable long pointId) {
        pointService.withdrawPoint(pointId);
    }

    @PostMapping("/use")
    @ResponseStatus(HttpStatus.OK)
    public void usePoint(@RequestBody PointUsingRequest request) {
        pointService.usePoint(request);
    }

    @PutMapping("/cancel")
    public void cancelPoint(@RequestBody PointCancellationRequest request) {
        pointService.cancelPoint(request);
    }

}
