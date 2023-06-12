# java-filmorate

## Database schema

![Untitled (4)](https://github.com/koalannette/java-filmorate/assets/113180456/e6226858-99c8-48b9-92d7-92eeaa38597b)

1. Сущность users содержит информацию о пользователях. В ней хранятся идентификатор пользователя (id), электронная почта (email), логин (login), имя (name) и дата рождения (birthday).

2. Сущность friends содержит информацию о друзьях пользователей. В ней хранятся идентификатор связи между пользователем и другом (friendship) , идентификатор пользователя (user_id), идентификатор друга (friend_id) и флаг подтверждения дружбы (status).

3. Сущность films содержит информацию о фильмах. В ней хранятся идентификатор фильма (id), название (name), описание (description), дата выхода (release_date), продолжительность (duration).

4. Сущность film_genre содержит связь между фильмами и их жанрами. В ней хранятся идентификатор жанра (genre_id) и идентификатор фильма (film_id).

5. Сущность mpa содержит информацию о возрастных ограничениях фильмов. В ней хранятся идентификатор возрастного ограничения (id), название (type) и описание (description).

6. Сущность genre содержит информацию о жанрах фильмов. В ней хранятся идентификатор жанра (id) и название (name).

7. Сущность likes содержит информацию о лайках фильмов. В ней хранятся идентификатор фильма (film_id) и идентификатор пользователя (user_id).
