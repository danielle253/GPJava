package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import manager.BookingManager;
import manager.NotificationManager;
import model.Booking;
import repository.FirebaseRepository;
import repository.Repository;

public class BookingManagmentService extends Thread{

	@Autowired 
	private Repository repository;

	private final int NOTIFICATION_LIMIT = 900; // Limit to send a push notification
	private final int WAIT_LIMIT = 900;
	private final int ARRIVAL_LIMIT = 30;

	public BookingManagmentService(){
		setDaemon(true);
	}

	@Override
	public void run() {
		while(true){
			
			bookingScan();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {System.out.println(e);}
		}
	}

	private void bookingScan() {
		List<Booking> bookings = repository.getObjectList(FirebaseRepository.BOOKING_REF);
		BookingManager.updateDistanceDuration(bookings);

		bookings.forEach(booking -> {
			if(booking.getStage().equals("REQUEST")) {
				request(booking);
			}else if(booking.getStage().equals("CONFIRMED")) {
				confirmed(booking);
			}else if(booking.getStage().equals("IN_PROGRESS")) {
				inProgress(booking);
			}
		});
	}

	private void request(Booking booking) {
		if(BookingManager.confirmation(booking)) 
			NotificationManager.sendPushMessage(booking, NotificationManager.CONFIRMATION_TEXT);
		else if(!booking.isNotificationSent()){
			NotificationManager.sendPushMessage(booking, NotificationManager.BUSY_TEXT);
			BookingManager.notified(booking);
		}
	}

	private void confirmed(Booking booking) {
		if(booking.isNotificationSent() && booking.getDuration().inSeconds <= ARRIVAL_LIMIT) {
			NotificationManager.sendPushMessage(booking, NotificationManager.ARRIVAL_TEXT);
			BookingManager.arrival(booking);
		}else if(booking.getDuration().inSeconds <= NOTIFICATION_LIMIT)
			NotificationManager.sendPushMessage(booking, NotificationManager.CONFIRMED_TEXT);
	}

	private void inProgress(Booking booking) {
		//Could be simplified here!!!!
		if(booking.getState().equals("COMPLETED") || booking.getState().equals("ABORTED") || booking.getState().equals("CANCELED")) 
			BookingManager.log(booking);
		else if(booking.getState().equals("WAIT") && (System.currentTimeMillis() - booking.getHold())/1000 > WAIT_LIMIT) 
			BookingManager.abortation(booking);
		else if(booking.getState().equals("WORK") && !booking.isNotificationSent() && booking.getDuration().inSeconds < NOTIFICATION_LIMIT) 
			NotificationManager.sendPushMessage(booking, NotificationManager.IN_PROGRESS_TEXT);
		else if(booking.getState().equals("WORK") && booking.getDuration().inSeconds < ARRIVAL_LIMIT) 
			BookingManager.wait(booking);
	}	
}
