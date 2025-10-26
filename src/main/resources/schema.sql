-- Exported from QuickDBD: https://www.quickdatabasediagrams.com/
-- Link to schema: https://app.quickdatabasediagrams.com/#/d/pBjxyx
-- NOTE! If you have used non-SQL datatypes in your design, you will have to change these here.

-- Modify this code to update the DB schema diagram.
-- To reset the sample schema, replace everything with
-- two dots ('..' - without quotes).

CREATE TABLE IF NOT EXISTS Films (
    id int   NOT NULL,
    name varchar(100)   NOT NULL,
    description varchar(100)   NOT NULL,
    releaseDate date   NOT NULL,
    duration int   NOT NULL,
    CONSTRAINT pk_Films PRIMARY KEY (
        id
     )
);

CREATE TABLE IF NOT EXISTS Users (
    id int   NOT NULL,
    email varchar(100)   NOT NULL,
    login varchar(100)   NOT NULL,
    name varchar(100)   NOT NULL,
    birthday date   NOT NULL,
    CONSTRAINT pk_Users PRIMARY KEY (
        id
     )
);

CREATE TABLE IF NOT EXISTS likes (
    id int   NOT NULL,
    film_id int   NOT NULL,
    user_id int   NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (
        id
     )
);

CREATE TABLE IF NOT EXISTS Genres (
    id int   NOT NULL,
    name varchar(100)   NOT NULL,
    CONSTRAINT pk_Genres PRIMARY KEY (
        id
     )
);

CREATE TABLE IF NOT EXISTS Films_Genres_Link (
    id int   NOT NULL,
    film_id int   NOT NULL,
    genre_id int   NOT NULL,
    CONSTRAINT pk_Films_Genres_Link PRIMARY KEY (
        id
     )
);

CREATE TABLE IF NOT EXISTS Friends (
    id int   NOT NULL,
    user_id int   NOT NULL,
    friend_id int   NOT NULL,
    CONSTRAINT pk_Friends PRIMARY KEY (
        id
     )
);

CREATE TABLE IF NOT EXISTS rating_mpa (
    id int   NOT NULL,
    film_id int   NOT NULL,
    name varchar(100)   NOT NULL,
    CONSTRAINT pk_rating_mpa PRIMARY KEY (
        id
     )
);

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_film_id FOREIGN KEY(film_id)
REFERENCES Films (id);

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_user_id FOREIGN KEY(user_id)
REFERENCES Users (id);

ALTER TABLE Films_Genres_Link ADD CONSTRAINT IF NOT EXISTS fk_Films_Genres_Link_film_id FOREIGN KEY(film_id)
REFERENCES Films (id);

ALTER TABLE Films_Genres_Link ADD CONSTRAINT IF NOT EXISTS fk_Films_Genres_Link_genre_id FOREIGN KEY(genre_id)
REFERENCES Genres (id);

ALTER TABLE Friends ADD CONSTRAINT IF NOT EXISTS fk_Friends_user_id FOREIGN KEY(user_id)
REFERENCES Users (id);

ALTER TABLE Friends ADD CONSTRAINT IF NOT EXISTS fk_Friends_friend_id FOREIGN KEY(friend_id)
REFERENCES Users (id);

ALTER TABLE rating_mpa ADD CONSTRAINT IF NOT EXISTS fk_rating_mpa_film_id FOREIGN KEY(film_id)
REFERENCES Films (id);

--DROP TABLE rating_mpa;
--DROP TABLE likes;
--DROP TABLE films_genres_link;
--DROP TABLE genres;
--DROP TABLE friends;
--DROP TABLE films;
--DROP TABLE users;