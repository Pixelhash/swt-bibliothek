package de.fhl.swtlibrary.mvc;

import org.jooby.quartz.Scheduled;

public class ReminderController {

  public ReminderController() {

  }
  // Initialize the cronjob to fire at 12am every day
  @Scheduled("0 0 12 * * ?")
  public void doWork() {
    System.out.println(remind());
  }

  public String remind() {
    return "Reminder hat gefeuert!";
  }
}
