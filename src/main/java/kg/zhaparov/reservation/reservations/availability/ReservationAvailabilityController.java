package kg.zhaparov.reservation.reservations.availability;

import jakarta.validation.Valid;
import kg.zhaparov.reservation.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation/availability")
public class ReservationAvailabilityController {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationAvailabilityService service;

    public ReservationAvailabilityController(ReservationAvailabilityService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<CheckAvailabilityResponse> checkAvailability(
            @Valid CheckAvailabilityRequest request
    ) {
        log.info("Called checkAvailability method: request {}", request);
        boolean isAvailable = service.isReservationAvailable(
                request.roomId(), request.startDate(), request.endDate()
        );

        var message = isAvailable
                ? "Room available for reservation"
                : "Room not available for reservation";
        var status = isAvailable
                ? AvailabilityStatus.AVAILABLE
                : AvailabilityStatus.RESERVED;

        return ResponseEntity.status(200)
                .body(new CheckAvailabilityResponse(message, status));
    }
}
