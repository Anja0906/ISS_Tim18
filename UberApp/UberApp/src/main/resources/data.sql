#users
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', true, 'email', 'name', 'profile_picture', 'surname', 'password', 'telephone_number');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 5', true, 'email1', 'name1', 'profile_picture1', 'surname1', 'password1', 'telephone_number1');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Narodnog fronta 5', true, 'email2', 'name3', 'profile_picture5', 'surname4', 'password2', 'telephone_number2');



# users_activations
INSERT INTO user_activations (id, creation_date, duration, user_id) VALUES ('2', '2022-12-07', 5, '2');
INSERT INTO user_activations (id, creation_date, duration, user_id) VALUES ('3', '2022-12-07', 5, '3');
INSERT INTO user_activations (id, creation_date, duration, user_id) VALUES ('1', '2022-12-07', 5, '1');

# notes
INSERT INTO notes (id, message, user_id) VALUES ('1', 'bzdchjdsgcsd', '1');
INSERT INTO notes (id, message, user_id) VALUES ('2', 'zsjcjshajch', '2');
INSERT INTO notes (id, message, user_id) VALUES ('3', 'kjszdhfhsdjf', '3');

# drivers
INSERT INTO drivers (id, vehicle_id) VALUES ('1', '1');
INSERT INTO drivers (id, vehicle_id) VALUES ('2', '2');
INSERT INTO drivers (id, vehicle_id) VALUES ('3', '3');

# documents
INSERT INTO documents (id, document_image, name, driver_id) VALUES ('1', 'hkdhfshdfj', 'hsgdgadg', '1');
INSERT INTO documents (id, document_image, name, driver_id) VALUES ('2', 'asjhdjhasjcfh', 'ajshdjsahc', '2');
INSERT INTO documents (id, document_image, name, driver_id) VALUES ('3', 'ajshdjsh', 'jashjcshjc', '3');

# --work_time--
INSERT INTO work_time (end, start, driver_id) VALUES ('2022-12-7 01:55:00', '2022-12-7 01:10:00', '1');
INSERT INTO work_time (end, start, driver_id) VALUES ('2022-12-8 01:55:00', '2022-12-8 01:10:00', '2');
INSERT INTO work_time (end, start, driver_id) VALUES ('2022-12-9 01:55:00', '2022-12-9 01:10:00', '3');

# --rides--
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-11 23:59:59', 1, false, '2022-12-10 05:00:00', 0, 500, 0, '1', '1', '1');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 5, false, '2022-12-7 07:40:00', 0, 500, 0, '1', '2', '2');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 15, false, '2022-12-7 07:40:00', 0, 500, 0, '1', '3', '3');

# --vehicle--
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('1', 'NS12312', 1, 'Skoda fabia',  2, 1, 3, true, true);
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('2', 'NS45678',  0, 'Skoda fabia', 3, 2, 3, true, true);
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('3', 'NS 567 ad', 2,'Skoda fabia', 1, 3, 3, true, true);


# --panics--
INSERT INTO panics (time, reason, user_id) VALUES ('2022-12-07', 'jahdjhhefej', '1');
INSERT INTO panics (time, reason, user_id) VALUES ('2022-12-07', 'ajskdjsah', '2');
INSERT INTO panics (time, reason, user_id) VALUES ('2022-12-07', 'ajshdjshaj', '3');

# --messages--
INSERT INTO messages (id, message, message_type, time, receiver_id, ride_id, sender_id)
VALUES ('1', 'sbdfdhjdbvj', 'ajsfhasdfjhj', '2022-07-12 00:12:12', '1', '1', '2');
INSERT INTO messages (id, message, message_type, time, receiver_id, ride_id, sender_id)
VALUES ('2', 'ajshjsah', 'ajshfuaebf', '2022-07-12 00:12:12', '2', '2', '1');
INSERT INTO messages (id, message, message_type, time, receiver_id, ride_id, sender_id)
VALUES ('3', 'asjbfcahwfh', 'afggeasbc', '2022-07-12 00:12:12', '3', '3', '2');

# --locations_for_rides--
INSERT INTO locations_for_rides (id, kilometers, departure_id, destination_id)
VALUES ('1', 25, '1', '2');
INSERT INTO locations_for_rides (id, kilometers, departure_id, destination_id)
VALUES ('2', 5, '2', '1');
INSERT INTO locations_for_rides (id, kilometers, departure_id, destination_id)
VALUES ('3', 2, '3', '2');

# --rejections--
INSERT INTO rejections (id, reason, time) VALUES ('1', 'zhscgs', '2022-07-12 00:15:45');
INSERT INTO rejections (id, reason, time) VALUES ('2', 'sbcjs', '2022-07-12 00:15:45');
INSERT INTO rejections (id, reason, time) VALUES ('3', 'sjchjshc', '2022-07-12 00:15:45');

# --passenger--
<<<<<<< Updated upstream
INSERT INTO passenger (id, passenger_id) VALUES ('1', '1');
INSERT INTO passenger (id, passenger_id) VALUES ('2', '2');
INSERT INTO passenger (id, passenger_id) VALUES ('3', '3');
=======
INSERT INTO passenger (id) VALUES ('4');
INSERT INTO passenger (id) VALUES ('5');
INSERT INTO passenger (id) VALUES ('6');
>>>>>>> Stashed changes

# --locations--
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('bane1', '54478412154', '875154444', '1');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('bane', '515555', '897456', '2');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('bane3', '54478412154', '875154444', '3');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '2');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('Bulevar oslobodjenja 46', '45.267136', '19.833549', '1');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '3');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '4');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('Bulevar oslobodjenja 46', '45.267136', '19.833549', '4');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '5');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('Bulevar oslobodjenja 46', '45.267136', '19.833549', '5');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '6');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('Bulevar oslobodjenja 46', '45.267136', '19.833549', '6');
# --reviews--
INSERT INTO reviews (comment, rating, ride_id,passenger_id, is_driver) VALUES ('jashjfhasfhjsa', 1, '1',4,0);
INSERT INTO reviews (comment, rating, ride_id,passenger_id, is_driver) VALUES ('golman', 1, '1',4,1);
INSERT INTO reviews (comment, rating, ride_id,passenger_id, is_driver) VALUES ('bane', 5, '1',5,0);
INSERT INTO reviews (comment, rating, ride_id,passenger_id, is_driver) VALUES ('bane1', 5, '1',5,1);
INSERT INTO reviews (comment, rating, ride_id,passenger_id, is_driver) VALUES ('kris', 3, '1',6,0);
# INSERT INTO reviews (comment, rating, ride_id,passenger_id, is_driver) VALUES ('kris1', 3, '1',6,1);

# --administrators--
INSERT INTO administrators (name, profile_picture, surname, password, username)
VALUES ('anja', 'asbfhasfj', 'petkovic', 'ajsbsj', 'ajshjfhsafh');
INSERT INTO administrators (name, profile_picture, surname, password, username)
VALUES ('shdfjhsdjf', 'asbfhasfj', 'petkovic', 'iahscbsjhc', 'ajshcuhawuch');
INSERT INTO administrators (name, profile_picture, surname, password, username)
VALUES ('VDsgfsjhfka', 'asbfhasfj', 'petkovic', 'scbshhfcjsh', 'jsdchhshc');

# -- passenger_rides --
<<<<<<< Updated upstream
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (1, 1);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (1, 2);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (1, 3);

# SELECT * FROM passenger_rides pr NATURAL JOIN rides rid where rid.driver_id = 1 OR pr.passenger_id = 1;
#  SELECT * FROM rides rid natural join passenger p where rid.driver_id = 1 OR p.id = 1;

# SELECT * FROM messages m where m.sender_id = 1 OR m.receiver_id = 1;

# SELECT * FROM rides r NATURAL JOIN passenger p WHERE p.id = 1 or r.driver_id = 1;
=======
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (5, 1);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (4, 2);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (4, 3);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (5, 4);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (6, 5);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (6, 6);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (4, 5);

#roles
INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_PASSENGER');
INSERT INTO role (name) VALUES ('ROLE_DRIVER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');


#user_role
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO user_role (user_id, role_id) VALUES (3, 1);
INSERT INTO user_role (user_id, role_id) VALUES (4, 1);
INSERT INTO user_role (user_id, role_id) VALUES (5, 1);
INSERT INTO user_role (user_id, role_id) VALUES (6, 1);
INSERT INTO user_role (user_id, role_id) VALUES (7, 1);
INSERT INTO user_role (user_id, role_id) VALUES (8, 1);
INSERT INTO user_role (user_id, role_id) VALUES (9, 1);
INSERT INTO user_role (user_id, role_id) VALUES (10, 1);
INSERT INTO user_role (user_id, role_id) VALUES (11, 1);
INSERT INTO user_role (user_id, role_id) VALUES (12, 1);
INSERT INTO user_role (user_id, role_id) VALUES (13, 1);
INSERT INTO user_role (user_id, role_id) VALUES (14, 1);

# passenger_rides(treba da dobavim passengere)
#

>>>>>>> Stashed changes
