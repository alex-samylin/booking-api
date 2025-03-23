# Booking API
A simple Java + Spring Boot REST API for managing bookings, guests, and blocks for rental properties.

# Terminology
A booking is when a guest selects a start and end date and submits a reservation on a property. A block
is when the property owner or manager selects a range of days during which no guest can make a
booking (e.g. the owner wants to use the property for themselves, or the property manager needs to
schedule the repainting of a few rooms).

## Functionality
- BOOKINGS – create, update dates, cancel, rebook, delete, and retrieve a booking
- BLOCKS – create, update, delete, and retrieve availability blocks for a property
- GUESTS – create, update, and retrieve guest information

## Validation and Testing
Implemented proper validation to ensure data integrity.  
Added integration tests to verify that bookings do not overlap (in terms of dates and property) with non-canceled bookings or blocks.

See test implementations in:
- [BookingControllerIntegrationTest.java](https://github.com/alex-samylin/booking-api/blob/main/src/test/java/com/hostfully/booking/controller/BookingControllerIntegrationTest.java)
- [BlockControllerIntegrationTest.java](https://github.com/alex-samylin/booking-api/blob/main/src/test/java/com/hostfully/booking/controller/BlockControllerIntegrationTest.java)
