package de.fhl.swtlibrary.jobs;

import com.google.inject.Inject;
import de.fhl.swtlibrary.model.Reservation;
import io.requery.EntityStore;
import io.requery.Persistable;
import org.jooby.quartz.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationJob {

  private EntityStore<Persistable, Reservation> reservationEntityStore;

  @Inject
  public ReservationJob(EntityStore<Persistable, Reservation> reservationEntityStore) {
    this.reservationEntityStore = reservationEntityStore;
  }

  // Initialize the cronjob to fire at 8am every day
  @Scheduled("0 0 8am * * ?")
  public void deleteExpiredReservations() {
    System.out.println("Deleting expired reservations...");
    LocalDateTime now = LocalDateTime.now();
    List<Reservation> reservations = reservationEntityStore.select(Reservation.class)
      .get()
      .toList();

    reservations = reservations.stream().filter(reservation -> now.isAfter(reservation.getReservedUntil())).collect(Collectors.toList());
    System.out.println(reservations.size() + " reservations are expired!");
    reservationEntityStore.delete(reservations);
  }

}
