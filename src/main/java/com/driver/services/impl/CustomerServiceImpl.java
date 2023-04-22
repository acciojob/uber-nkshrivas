package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception {
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> drivers = driverRepository2.findAll();
		Comparator<Driver> compareById = new Comparator<Driver>() {
			@Override
			public int compare(Driver o1, Driver o2) {
				if (o1.getDriverId() > o2.getDriverId())
					return 1;
				if (o1.getDriverId() == o2.getDriverId())
					return 0;
				else
					return -1;
			}
		};

		Collections.sort(drivers, compareById);


		TripBooking tripBooking = new TripBooking();

		for (Driver d : drivers){
			if(d.getCab().isAvailable()){
				tripBooking.setDriver(d);
				break;
			}
		}
		if (tripBooking.getDriver()==null)
			throw new Exception("No cab available!");

		tripBooking.setCustomer(customerRepository2.findById(customerId).get());
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setBill(tripBooking.getDriver().getCab().getPerKmRate()*distanceInKm);

		tripBookingRepository2.save(tripBooking);

		return tripBooking;


	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		tripBookingRepository2.findById(tripId).get().setStatus(TripStatus.CANCELED);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		tripBookingRepository2.findById(tripId).get().setStatus(TripStatus.COMPLETED);


	}
}
