package kg.zhaparov.reservation;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap;
    private final AtomicLong idCounter;

    public ReservationService() {
        this.reservationMap = new HashMap<>();
        this.idCounter = new AtomicLong();
    }

    public Reservation getReservationById(
            Long id
    ) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found reservation with id " + id);
        }
        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.id() != null) {
            throw new IllegalArgumentException("ID should be empty");
        }

        if (reservationToCreate.status() != null) {
            throw new IllegalArgumentException("ID should be empty");
        }

        var newReservation = new Reservation(
                idCounter.incrementAndGet(),
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );

        reservationMap.put(newReservation.id(), newReservation);
        return newReservation;
    }

    public Reservation updateReservation(
            Long id,
            Reservation reservationToUpdate
    ) {
        if(!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found reservation with id " + id);
        }
        var reservation = reservationMap.get(id);
        if (reservation.status() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot update reservation with status " + reservation.status());
        }

        var updatedReservation = new Reservation(
                reservation.id(),
                reservationToUpdate.userId(),
                reservationToUpdate.roomId(),
                reservationToUpdate.startDate(),
                reservationToUpdate.endDate(),
                ReservationStatus.PENDING
        );
        reservationMap.put(reservation.id(), updatedReservation);
        return updatedReservation;
    }

    public void deleteReservation(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found reservation with id " + id);
        }
        reservationMap.remove(id);
    }

    public Reservation approveReservation(
            Long id
    ) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found reservation with id " + id);
        }
        var reservation = reservationMap.get(id);
        if (reservation.status() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot approve reservation with status " + reservation.status());
        }
        boolean isConflict = isReserved(reservation);
        if (isConflict) {
            throw new IllegalArgumentException("Cannot approve reservation because of conflict ");
        }

        var approvedReservation = new Reservation(
                reservation.id(),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.APPROVED
        );
        reservationMap.put(reservation.id(), approvedReservation);
        return approvedReservation;
    }

    private boolean isReserved(
            Reservation reservation
    ) {
        for (Reservation existingReservation: reservationMap.values()) {
            if (reservation.id().equals(existingReservation.id())) {
                continue;
            }
            if (!reservation.roomId().equals(existingReservation.roomId())) {
                continue;
            }
            if (!existingReservation.status().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (reservation.startDate().isBefore(existingReservation.endDate())
                && existingReservation.startDate().isBefore(reservation.endDate())) {
                return true;
            }
        }
        return false;
    }
}
