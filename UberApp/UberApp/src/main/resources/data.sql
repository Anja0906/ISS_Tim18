#users
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', false, 'anja@gmail.com', 'Anja', 'profile_picture', 'Petkovic', 'password', '0614843070');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 5', false, 'branislac@gmail.com', 'Branislav', 'profile_picture1', 'Stojkovic', 'password1', '0654315454');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Narodnog fronta 5', false, 'kristina@gmail.com', 'Kristina', 'profile_picture5', 'Andrijin', 'password2', '6521545154');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', true, 'pera@gmail.com', 'Pera', 'profile_picture', 'Peric', 'password', '45421545151');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 5', true, 'mika@gmail.com', 'Mika', 'profile_picture1', 'Mikic', 'password1', '852741963');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Narodnog fronta 5', false, 'zika@gmail.com', 'Zika', 'profile_picture5', 'Zikic', 'password2', '987456321');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', false, 'jovan@gmail.com', 'Jovan', 'profile_picture', 'Jovic', 'password', '3265987414');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', true, 'marko@gmail', 'Marko', 'profile_picture', 'Markovic', 'password', '369258147');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 5', true, 'janko@gmail.com', 'Janko', 'profile_picture1', 'Jankovic', 'password1', '12345678');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Narodnog fronta 5', false, 'ema@gmail.com', 'Emilija', 'profile_picture5', 'Markovic', 'password2', '321654987');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', true, 'anita@gmail.com', 'Anita', 'profile_picture', 'Pajic', 'password', '852741963');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 5', false, 'petar@gmail.com', 'Petar', 'profile_picture1', 'Petrovic', 'password1', '545841454');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Narodnog fronta 5', true, 'steva@gmail.com', 'Stevan', 'profile_picture5', 'Stevanovic', 'password2', '534646874515');
INSERT INTO users (active, address, blocked, email, name, profile_picture, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', false, 'nenad@gmail.com', 'Nenad', 'profile_picture', 'Nenadovic', 'password', '96325414');


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
INSERT INTO drivers (id, vehicle_id) VALUES ('7', '4');
INSERT INTO drivers (id, vehicle_id) VALUES ('8', '5');
INSERT INTO drivers (id, vehicle_id) VALUES ('9', '6');

# documents
INSERT INTO documents (id, document_image, name, driver_id) VALUES ('1', 'hkdhfshdfj', 'hsgdgadg', '1');
INSERT INTO documents (id, document_image, name, driver_id) VALUES ('2', 'asjhdjhasjcfh', 'ajshdjsahc', '5');
INSERT INTO documents (id, document_image, name, driver_id) VALUES ('3', 'ajshdjsh', 'jashjcshjc', '3');

# --work_time--
INSERT INTO work_time (end, start, driver_id) VALUES ('2022-12-7 01:55:00', '2022-12-7 01:10:00', '1');
INSERT INTO work_time (end, start, driver_id) VALUES ('2022-12-7 01:55:00', '2022-12-7 01:10:00', '2');
INSERT INTO work_time (end, start, driver_id) VALUES ('2022-12-7 01:55:00', '2022-12-7 01:10:00', '3');

# --rides--
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-11 23:59:59', 1, false, '2022-12-10 05:00:00', 0, 500, 0, '2', '1', '1');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 5, false, '2022-12-7 07:40:00', 0, 500, 0, '1', '2', '2');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 15, false, '2022-12-7 07:40:00', 0, 500, 0, '1', '3', '3');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-11 23:59:59', 1, false, '2022-12-10 05:00:00', 0, 500, 0, '3', '4', '4');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-11 22:55:00', 5, false, '2022-12-11 20:40:00', 0, 500, 0, '1', '5', '5');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 15, false, '2022-12-7 07:40:00', 0, 500, 0, '1', '6', '6');

# --vehicle--
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('1', 'NS12312', 1, 'Skoda fabia',  2, 1, 3, true, true);
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('2', 'NS45678',  0, 'sjfcdsjfjewfja', 3, 2, 3, true, true);
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('3', 'NS 567 ad', 2,'asnfklsdfvksdj', 1, 3, 4, true, true);
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('4', 'NS11111', 1, 'sjbcjbejehcvewj',  2, 7, 4, true, true);
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('5', 'NS45678',  0, 'Reno megan', 3, 8, 4, true, true);
INSERT INTO vehicle (id, license_number, vehicle_type, model,  current_location_id, driver_id, passenger_seats, baby_transport, pet_transport)
VALUES ('6', 'NS 567 ad', 2,'ksncejfewfls', 1, 9, 2, true, true);

# --panics--
INSERT INTO panics (time, reason, user_id) VALUES ('2022-12-07', 'jahdjhhefej', '1');
INSERT INTO panics (time, reason, user_id) VALUES ('2022-12-07', 'ajskdjsah', '2');
INSERT INTO panics (time, reason, user_id) VALUES ('2022-12-07', 'ajshdjshaj', '3');
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
INSERT INTO rejections (id, reason, time_of_rejection) VALUES ('1', 'zhscgs', '2022-07-12 00:15:45');
INSERT INTO rejections (id, reason, time_of_rejection) VALUES ('2', 'sbcjs', '2022-07-12 00:15:45');
INSERT INTO rejections (id, reason, time_of_rejection) VALUES ('3', 'sjchjshc', '2022-07-12 00:15:45');
INSERT INTO rejections (id, reason, time_of_rejection) VALUES ('4', 'zhscgs', '2022-07-12 00:15:45');
INSERT INTO rejections (id, reason, time_of_rejection) VALUES ('5', 'sbcjs', '2022-07-12 00:15:45');
INSERT INTO rejections (id, reason, time_of_rejection) VALUES ('6', 'sjchjshc', '2022-07-12 00:15:45');

# --passenger--
INSERT INTO passenger (id, passenger_id) VALUES ('4', '1');
INSERT INTO passenger (id, passenger_id) VALUES ('5', '2');
INSERT INTO passenger (id, passenger_id) VALUES ('6', '3');

# --locations--
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('jshdjsahdh', '54478412154', '875154444', '1');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '2');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('jshdjsahdh', '54478412154', '875154444', '3');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '2');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('Bulevar oslobodjenja 46', '45.267136', '19.833549', '1');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '3');

# --reviews--
INSERT INTO reviews (comment, rating, ride_id) VALUES ('jashjfhasfhjsa', 1, '1');
INSERT INTO reviews (comment, rating, ride_id) VALUES ('jashjfhasfhjsa', 5, '2');
INSERT INTO reviews (comment, rating, ride_id) VALUES ('jashjfhasfhjsa', 3, '3');

# -- vehicle review --
# -- driver review --

# --administrators--
INSERT INTO administrators (name, profile_picture, surname, password, username)
VALUES ('anja', 'asbfhasfj', 'petkovic', 'ajsbsj', 'ajshjfhsafh');
INSERT INTO administrators (name, profile_picture, surname, password, username)
VALUES ('shdfjhsdjf', 'asbfhasfj', 'petkovic', 'iahscbsjhc', 'ajshcuhawuch');
INSERT INTO administrators (name, profile_picture, surname, password, username)
VALUES ('VDsgfsjhfka', 'asbfhasfj', 'petkovic', 'scbshhfcjsh', 'jsdchhshc');

# -- passenger_rides --
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (1, 1);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (4, 2);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (4, 3);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (1, 4);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (1, 5);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (1, 6);
INSERT INTO passenger_rides (passenger_id, ride_id) VALUES (4, 5);

# passenger_rides(treba da dobavim passengere)
#