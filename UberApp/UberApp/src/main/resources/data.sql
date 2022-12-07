#users
INSERT INTO users (active, address, blocked, email, name, image_link, surname, password, telephone_number)
VALUES (false, 'Strumicka 6', true, 'email', 'name', 'image_link', 'surname', 'password', 'telephone_number');
INSERT INTO users (active, address, blocked, email, name, image_link, surname, password, telephone_number)
VALUES (false, 'Strumicka 5', true, 'email1', 'name1', 'image_link1', 'surname1', 'password1', 'telephone_number1');
INSERT INTO users (active, address, blocked, email, name, image_link, surname, password, telephone_number)
VALUES (false, 'Narodnog fronta 5', true, 'email2', 'name3', 'image_link5', 'surname4', 'password2', 'telephone_number2');



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
INSERT INTO documents (id, image, name, driver_id) VALUES ('1', 'hkdhfshdfj', 'hsgdgadg', '1');
INSERT INTO documents (id, image, name, driver_id) VALUES ('2', 'asjhdjhasjcfh', 'ajshdjsahc', '2');
INSERT INTO documents (id, image, name, driver_id) VALUES ('3', 'ajshdjsh', 'jashjcshjc', '3');

# --work_time--
INSERT INTO work_time (end_time, start_time, driver_id) VALUES ('2022-12-7 01:55:00', '2022-12-7 01:10:00', '1');
INSERT INTO work_time (end_time, start_time, driver_id) VALUES ('2022-12-7 01:55:00', '2022-12-7 01:10:00', '2');
INSERT INTO work_time (end_time, start_time, driver_id) VALUES ('2022-12-7 01:55:00', '2022-12-7 01:10:00', '3');

# --rides--
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 15, false, '2022-12-7 07:40:00', 0, 500, 0, '1', '1', '1');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 15, false, '2022-12-7 07:40:00', 0, 500, 0, '2', '2', '2');
INSERT INTO rides (baby_transport, end_time, estimated_time_in_minutes, pet_transport, start_time, status, total_cost, vehicle_type, driver_id, panic_id, rejection_id)
VALUES (true, '2022-12-7 07:55:00', 15, false, '2022-12-7 07:40:00', 0, 500, 0, '3', '3', '3');

# --vehicle--
INSERT INTO vehicle (id, license_plate_number, vehicle_model, vehicle_type, driver_id)
VALUES ('1', 'NS12312', 'Skoda fabia', 1, '1');
INSERT INTO vehicle (id, license_plate_number, vehicle_model, vehicle_type, driver_id)
VALUES ('2', 'NS45678', 'Skoda fabia', 0, '2');
INSERT INTO vehicle (id, license_plate_number, vehicle_model, vehicle_type, driver_id)
VALUES ('3', 'NS 567 ad', 'Skoda fabia', 2, '3');

# --panics--
INSERT INTO panics (date, reason, user_id) VALUES ('2022-12-07', 'jahdjhhefej', '1');
INSERT INTO panics (date, reason, user_id) VALUES ('2022-12-07', 'ajskdjsah', '2');
INSERT INTO panics (date, reason, user_id) VALUES ('2022-12-07', 'ajshdjshaj', '3');

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
INSERT INTO passenger (id, passenger_id) VALUES ('1', '1');
INSERT INTO passenger (id, passenger_id) VALUES ('2', '2');
INSERT INTO passenger (id, passenger_id) VALUES ('3', '3');

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
VALUES ('jshdjsahdh', '54478412154', '875154444', '1');
INSERT INTO locations (address, latitude, longitude, ride_id)
VALUES ('asbdsabjc', '515555', '897456', '3');

# --reviews--
INSERT INTO reviews (comment, rating, ride_id) VALUES ('jashjfhasfhjsa', 1, '1');
INSERT INTO reviews (comment, rating, ride_id) VALUES ('jashjfhasfhjsa', 5, '2');
INSERT INTO reviews (comment, rating, ride_id) VALUES ('jashjfhasfhjsa', 3, '3');

# --administrators--
INSERT INTO administrators (name, image_link, surname, password, username)
VALUES ('anja', 'asbfhasfj', 'petkovic', 'ajsbsj', 'ajshjfhsafh');
INSERT INTO administrators (name, image_link, surname, password, username)
VALUES ('shdfjhsdjf', 'asbfhasfj', 'petkovic', 'iahscbsjhc', 'ajshcuhawuch');
INSERT INTO administrators (name, image_link, surname, password, username)
VALUES ('VDsgfsjhfka', 'asbfhasfj', 'petkovic', 'scbshhfcjsh', 'jsdchhshc');