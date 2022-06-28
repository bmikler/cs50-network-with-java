INSERT INTO
    user(id, enabled, locked, password, username)
VALUES
    (1, true, false, '$2a$10$XOJcStNly3Ecsr49VJjp8uYj7xzhkTqE5Gtue6Ic3M7sxg0YmJpDu', 'user1'),
    (2, true, false, '$2a$10$XOJcStNly3Ecsr49VJjp8uYj7xzhkTqE5Gtue6Ic3M7sxg0YmJpDu', 'user2'),
    (3, true, false, '$2a$10$XOJcStNly3Ecsr49VJjp8uYj7xzhkTqE5Gtue6Ic3M7sxg0YmJpDu', 'user3'),
    (4, true, false, '$2a$10$XOJcStNly3Ecsr49VJjp8uYj7xzhkTqE5Gtue6Ic3M7sxg0YmJpDu', 'user4'),
    (5, true, false, '$2a$10$XOJcStNly3Ecsr49VJjp8uYj7xzhkTqE5Gtue6Ic3M7sxg0YmJpDu', 'user5');

INSERT INTO
    location(id, city, country)
VALUES
    (1, 'New York', 'USA'),
    (2, 'London', 'England'),
    (3, 'Tokio', 'Japan'),
    (4, 'Kraków', 'Poland'),
    (5, 'Paris', 'France');

INSERT INTO
    post(id, text, timestamp, author_id, location_id)
VALUES
    (1, 'post1', '2022-06-28 13:33:20', 1, 1),
    (2, 'post2', '2022-06-28 14:33:20', 1, 2),
    (3, 'post3', '2022-06-28 15:33:20', 1, 3),
    (4, 'post4', '2022-06-28 16:33:20', 2, 5),
    (5, 'post5', '2022-06-28 17:33:20', 2, 4),
    (6, 'post6', '2022-06-28 18:33:20', 3, 2),
    (7, 'post7', '2022-05-28 16:33:20', 4, 1),
    (8, 'post8', '2022-04-28 16:33:20', 5, 3),
    (9, 'post9', '2022-06-28 11:33:20', 5, 4),
    (10, 'post10', '2022-06-28 10:33:20', 5, 1);


INSERT INTO
    likes(post_id, user_id)
VALUES
    (10, 1),
    (9, 1),
    (8, 1),
    (7, 1),
    (1, 2),
    (2, 2),
    (1, 3),
    (1, 4),
    (5, 4),
    (5, 3),
    (5, 1);

INSERT INTO
    user_followers(user_id, followers_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 1),
    (3, 1),
    (3, 2),
    (3, 5),
    (4, 1);

INSERT INTO
    user_followings(user_id, followings_id)
VALUES
    (2, 1),
    (3, 1),
    (1, 2),
    (1, 3),
    (2, 3),
    (5, 3),
    (1, 4);
