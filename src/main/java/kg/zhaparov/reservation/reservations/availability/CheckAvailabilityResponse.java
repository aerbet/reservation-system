package kg.zhaparov.reservation.reservations.availability;

public record CheckAvailabilityResponse(
        String message,
        AvailabilityStatus status
) {
}
