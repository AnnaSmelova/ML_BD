##  HW05: Text classification using Spark

### Цель: обучить текстовый классификатор на простых фичах с помощью Spark ML, используя
1) HashingTF и IDF
2) Word2Vec 

Домашнее задание основано на соревновании:  
https://www.kaggle.com/competitions/jigsaw-toxic-comment-classification-challenge/data

Решение оформлено в виде ноутбуков в формате ipynb.<br>
Решение выполнено с использованием pyspark.<br>

Данные для обучения и валидации: <a href='https://www.kaggle.com/competitions/jigsaw-toxic-comment-classification-challenge/data?select=train.csv.zip'>Toxic Comment Classification Challenge</a>.<br>

Во время выполнения домашнего задания не получилось нормально считать датасет в исходном виде через spark из-за наличия в текстах запятых и символов переноса строк. Поэтому выполнила небольшую предобработку данных: <a href='https://github.com/AnnaSmelova/ML_BD/blob/main/hw5/hw5_preprocess_data.ipynb'>Предобработка данных</a><br>

Ноутбук с HashingTF и IDF: <a href='https://github.com/AnnaSmelova/ML_BD/blob/main/hw5/hw5_pyspark_TFIDF.ipynb'>TF-IDF</a><br>
Ноутбук с  Word2Vec: <a href='https://github.com/AnnaSmelova/ML_BD/blob/main/hw5/hw5_pyspark_Word2Vec.ipynb'>Word2Vec</a>

#### Выводы:
1. **Сделать выводы о влиянии параметра numFeatures в HashingTF на качество метрик:** <br>
С увеличением размера вектора, т.е. количества numFeatures, качество модели возрастает, и соответственно, метрики повышаются.

2. **W2V - сравнить метрики качества с предыдущими подходами:** <br>
Word2Vec показывает лучшее качество даже при длине вектора в 100 против TF-IDF при длине вектора в 1024.



 
