##  HW01: Hadoop

### Блок 1. Развертывание локального кластера Hadoop
**1) Развернуть локальный кластер в конфигурации 1 NN, 3 DN + NM, 1 RM, 1 History server<br>**
[Исходная сборка](https://github.com/big-data-europe/docker-hadoop) <br>
[Внесенные изменения](https://github.com/AnnaSmelova/ML_BD/tree/main/hw1/docker-hadoop_changes) <br><br>
**2) Изучить настройки и состояние NN и RM в веб-интерфейсе**
* В NN - 3 активные DN
* В RM отображается только 1 активная нода, видимо из-за того, что на 3 DN только один NM: [docker-compose.yml](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/docker-hadoop_changes/docker-compose.yml)

**3) Сделать скриншоты NN и RM, добавить в репозиторий**
![NN_overview](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block1/NN_overview.png)
![NN_datanodes](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block1/NN_datanodes.png)
![RM](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block1/RM.png)

### Блок 2. Написание map reduce на Python
**1. Загрузите датасет по ценам на жилье Airbnb, доступный на [kaggle.com](https://www.kaggle.com/dgomonov/new-york-city-airbnb-open-data)**<br>
**2. Подсчитайте среднее значение и дисперсию по признаку ”price” стандартными способами (”чистый код” или использование библиотек). Не учитывайте пропущенные значения при подсчете статистик.**<br>
[Стандартный способ](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/Preprocessing_and_numpy_stats.ipynb)<br>
**3. Используя Python, реализуйте скрипт mapper.py и reducer.py для расчета каждой из двух величин. В итоге у вас должно получиться 4 скрипта: 2 mapper и 2 reducer для каждой величины.**<br>
[mapper_mean.py](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/mapper_mean.py) <br>
[reducer_mean.py](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/reducer_mean.py) <br>
[mapper_var.py](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/mapper_var.py) <br>
[reducer_var.py](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/reducer_var.py) <br>
**4. Проверьте правильность подсчета статистик методом map-reduce в сравнении со стандартным подходом**<br>
**5. Результаты сравнения (то есть, подсчета двумя разными способами) для среднего значения и дисперсии запишите в файл .txt. В итоге, у вас должно получиться две пары значений (стандартного расчета и map-reduce)- одна пара для среднего, другая - для дисперсии.**<br>
[result.txt](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/result.txt)<br>
**6. Итоговый результат с выполненным заданием должен включать в себя сам код, а также результаты его работы, который необходимо разместить в репозитории.**<br><br>
#### Шаги:
1. Поднять кластер локально:<br>
`docker-compose build`<br>
`docker compose up --force-recreate`<br>

2. Скопировать данные и скрипты на NameNode:<br>
`docker cp ../hw1/part2/data/Price_data.csv namenode:/`<br>
`docker cp ../hw1/part2/mapper_mean.py namenode:/`<br>
`docker cp ../hw1/part2/reducer_mean.py namenode:/`<br>
`docker cp ../hw1/part2/mapper_var.py namenode:/`<br>
`docker cp ../hw1/part2/reducer_var.py namenode:/`<br>

3. Подключиться к контейнеру:<br>
`docker exec -it namenode /bin/bash`<br>

4. Записать данные и скрипты в hdfs:<br>
`hdfs dfs -put Price_data.csv /`<br>
`hdfs dfs -put mapper_mean.py /`<br>
`hdfs dfs -put reducer_mean.py /`<br>
`hdfs dfs -put mapper_var.py /`<br>
`hdfs dfs -put reducer_var.py /`<br>

5. Запустить скрипты MapReduce:<br>
`hadoop jar /opt/hadoop-3.2.1/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar -file /mapper_mean.py -mapper /mapper_mean.py -file /reducer_mean.py -reducer /reducer_mean.py -input /Price_data.csv -output /out_data_mean`<br><br>
`hadoop jar /opt/hadoop-3.2.1/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar -file /mapper_var.py -mapper /mapper_var.py -file /reducer_var.py -reducer /reducer_var.py -input /Price_data.csv -output /out_data_var`<br>

#### Результат:<br>
[out_data_mean](https://github.com/AnnaSmelova/ML_BD/tree/main/hw1/block2/out_data_mean)<br> 
[out_data_var](https://github.com/AnnaSmelova/ML_BD/tree/main/hw1/block2/out_data_var)<br>
![RM_2_applicatiomns.png](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/RM_2_applicatiomns.png)
![Application_1.png](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/Application_1.png)
![Application_2.png](https://github.com/AnnaSmelova/ML_BD/blob/main/hw1/block2/Application_2.png)
 
