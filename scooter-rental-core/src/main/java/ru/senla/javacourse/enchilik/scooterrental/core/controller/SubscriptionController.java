package ru.senla.javacourse.enchilik.scooterrental.core.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.javacourse.enchilik.scooterrental.api.dto.SubscriptionDto;
import ru.senla.javacourse.enchilik.scooterrental.core.exception.SubscriptionNotFoundException;
import ru.senla.javacourse.enchilik.scooterrental.core.model.User;
import ru.senla.javacourse.enchilik.scooterrental.core.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionDto>> getActiveByCurrentUser(@AuthenticationPrincipal User user)
        throws SubscriptionNotFoundException {
        List<SubscriptionDto> byUserId = subscriptionService.findActiveByUserId(user.getId());
        return new ResponseEntity<>(byUserId, HttpStatus.OK);
    }
}
