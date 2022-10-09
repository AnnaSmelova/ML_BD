##  HW02: Hive

### Блок 1. Развертывание локального Hive
1) Развернуть локальный Hive в любой конфигурации
[Исходная сборка](https://github.com/tech4242/docker-hadoop-hive-parquet) <br>
Запускаем командой `docker-compose up`<br>
2) Подключиться к развернутому Hive с помощью любого инструмента: Hue, Python
Driver, Zeppelin, любая IDE итд
![Локальный Hive](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block1/Hive_local_1.png)
![Конфигурация Hive](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block1/Hive_local_2.png)
3) Сделать скриншоты поднятого Hive и подключений в выбранными вами
инструментах, добавить в репозиторий<br>
**Подключение через командную строку:**<br>
Подключаемся к контейнеру:<br>
`docker exec -it docker-hadoop-hive-parquet_hive-server_1 /bin/bash`<br>
Запускаем hive:<br>
`hive`<br>
Создала тестовую базу **testdb**, и в ней тестовую таблицу:
![Командная строка 1](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block1/Hive_connect_1.png)
Добавила несколько тестовых записей:
![Командная строка 2](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block1/Hive_connect_2.png)
![Командная строка 3](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block1/Hive_connect_3.png)
**Подключение через Hue:**
Тестовую базу testdb видно:
![Hue 1](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block1/Hive_hue_connect_1.png)
Тестовые записи в таблице отображаются:
![Hue 2](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block1/Hive_hue_connect_2.png)

### Блок 2. Работа с Hive
1. Сделать таблицу artists в Hive и вставить туда значения, используя датасет [Kaggle](https://www.kaggle.com/pieca111/music-artists-popularity)<br>
Из-за того, что Hue очень тормозил. я его погасила, и запросы делала из командной строки в такой конфигурации:
![Hive config](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_connect.png)
#### Шаги:
1. Копируем данные в контейнер:<br>
`docker cp ../data/artists.csv docker-hadoop-hive-parquet_hive-server_1:/`<br>
2. Переходим в корень и убираем первую строку с header:<br>
`docker exec -it docker-hadoop-hive-parquet_hive-server_1 /bin/bash`<br>
`cd ..`<br>
`sed -i '1d' artists.csv`<br>
![Create 1](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_create_db.png)
3. Создаем базу:<br>
`CREATE DATABASE artists;`<br>
`USE artists;`<br>
4. Создаем таблицу:<br>
  ```
  CREATE TABLE artists(mbid STRING, artist_mb STRING, artist_lastfm STRING, country_mb STRING, country_lastfm STRING, tags_mb STRING, tags_lastfm STRING, listeners_lastfm INT, scrobbles_lastfm INT, ambiguous_artist STRING) 
  ROW FORMAT DELIMITED 
  FIELDS TERMINATED BY ',' 
  STORED AS TEXTFILE;
  ```
![Create 2](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_create_table.png)
5. Загружаем данные в таблицу:<br>
`LOAD DATA LOCAL INPATH '/artists.csv' INTO TABLE artists;`<br>
![Load data](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_upload_data.png)

2. Используя Hive найти (команды и результаты записать в файл и добавить в репозиторий):<br>
a) Исполнителя с максимальным числом скробблов:<br>
```
SELECT artist_mb FROM (
  SELECT artist_mb, scrobbles_lastfm 
  FROM artists 
  ORDER BY scrobbles_lastfm DESC 
  LIMIT 1
  ) AS top_scrobbles;
```
![a](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_a_task.png)
b) Самый популярный тэг на ластфм:<br>
```
SELECT tag FROM (
  SELECT tag, COUNT(*) as cnt 
  FROM (
    SELECT EXPLODE(SPLIT(LOWER(tags_lastfm), '; ')) AS tag 
    FROM artists
  ) AS all_tags 
  WHERE tag != '' 
  GROUP BY tag 
  ORDER BY cnt DESC 
  LIMIT 1
) AS top_tag;
```
![b](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_b_task.png)
c) Самые популярные исполнители 10 самых популярных тегов ластфм:<br>
```
WITH
artists_tags AS (
  SELECT artist_lastfm, listeners_lastfm, tag 
  FROM artists
  LATERAL VIEW EXPLODE(SPLIT(LOWER(tags_lastfm), '; ')) tag_table AS tag 
  WHERE tag != ''
),
artists_tags_ok AS (
  SELECT artist_lastfm, listeners_lastfm, TRIM(tag) AS tag 
  FROM artists_tags
),
tags_cnt AS (
  SELECT tag, count(tag) AS cnt 
  FROM artists_tags_ok
  GROUP BY tag
  ORDER BY cnt DESC
  LIMIT 10
),
top_tags_artists AS (
  SELECT DISTINCT artist_lastfm, listeners_lastfm 
  FROM artists_tags_ok
  WHERE tag IN (SELECT tag FROM tags_cnt)
  ORDER BY listeners_lastfm DESC
  LIMIT 10
)
SELECT artist_lastfm FROM top_tags_artists;
```
![c 1](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_c_task.png)
![c 2](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_c_task_end.png)
d) Любой другой инсайт на ваше усмотрение: ТОП-10 исполнителей по числу скробблов из России:<br>
```
SELECT artist_lastfm, country_lastfm 
FROM (
  SELECT artist_lastfm, scrobbles_lastfm, country_lastfm 
  FROM artists 
  WHERE country_lastfm LIKE 'Russia%' 
  ORDER BY scrobbles_lastfm DESC 
  LIMIT 10
) AS top_russia;
```
![d](https://github.com/AnnaSmelova/ML_BD/blob/main/hw2/block2/Hive_d_task.png)

 
