package kg.zhaparov.reservation;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap;

    public ReservationService() {
        this.reservationMap = Map.of(
                1L, new Reservation(
                        1L,
                        100L,
                        40L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(5),
                        ReservationStatus.APPROVED
                ),
                2L, new Reservation(
                        2L,
                        111L,
                        44L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(15),
                        ReservationStatus.APPROVED
                ),
                3L, new Reservation(
                        3L,
                        122L,
                        42L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(25),
                        ReservationStatus.APPROVED
                )
        );
    }

    public Reservation getReservationById(
            Long id
    ) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Reservation with id " + id + " does not exist");
        }
        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    }
}
