CREATE TABLE "user" (
  "id" integer PRIMARY KEY,
  "email" varchar,
  "login" varchar,
  "name" varchar,
  "birthday" date
);

CREATE TABLE "film" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "description" text,
  "releaseDate" date,
  "duration" integer,
  "rating_id" integer
);

CREATE TABLE "genre" (
  "id" integer PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "raring" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "description" text
);

CREATE TABLE "likes" (
  "film_id" integer,
  "user_id" integer
);

CREATE TABLE "friends" (
  "user_id" integer,
  "friend_id" integer
);

CREATE TABLE "film_genre" (
  "film_id" integer,
  "genre_id" integer
);

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");

ALTER TABLE "film" ADD FOREIGN KEY ("rating_id") REFERENCES "raring" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "user" ("id");
