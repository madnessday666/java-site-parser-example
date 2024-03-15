# Java Site Parser Example

Пример парсера, написанного на Java для сбора информации с сайта Citilink. 

## Требования

* JDK 17 
* Gradle 8.4 +

## Сборка и запуск

Склонировать репозиторий:

```
git clone https://github.com/madnessday666/java-site-parser-example.git
```

Перейти в директорию с проектом:

```
cd java-site-parser-example
```

Собрать JAR файл при помощи Gradle:

```
./gradlew jar
```

Перейти в директорию с JAR файлом:

```
cd build/libs
```

Запустить приложение:

```
java -jar parser.jar [OPTIONS]
```

## Работа с приложением

Для запуска приложения необходимо задать параметры: 

```
parser.jar [OPTIONS]
```

__Параметры можно комбинировать между собой.__

Список параметров

<table>
<thead>
  <tr>
    <th align="center">Параметр</th>
    <th align="center">Тип</th>
    <th align="center">Пример</th>
    <th align="center">Описание</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td align="center"><pre>-c, --category</pre></td>
    <td align="center">Integer</td>
    <td align="center"><pre>parser.jar -c 1</pre></td>
    <td>Парсит данные категории под номером 1 (включая все подкатегории).</td>
  </tr>
  <tr>
    <td align="center"><pre>-f, --format</pre></td>
    <td align="center">String</td>
    <td align="center"><pre>parser.jar -f html</pre></td>
    <td>Устанавливает формат конечного файла. Варинаты: html, csv.</td>
  </tr>
  <tr>
    <td align="center"><pre>-h, --help</pre></td>
    <td align="center">-</td>
    <td align="center"><pre>parser.jar -h</pre></td>
    <td>Вывести "помощь".</td>
  </tr>  
  <tr>
    <td align="center"><pre>-l, --list</pre></td>
    <td align="center">String</td>
    <td align="center"><pre>parser.jar -l regions</pre></td>
    <td>Вывести список возможных категорий/регионов. Варианты: categories, regions.</td>
  </tr>
  <tr>
    <td align="center"><pre>-p, --pages</pre></td>
    <td align="center">Integer</td>
    <td align="center"><pre>parser.jar -p 3</pre></td>
    <td>Количество страниц категории для парсинга.</td>
  </tr>
  <tr>
    <td align="center"><pre>-t, --timeout</pre></td>
    <td align="center">Integer</td>
    <td align="center"><pre>parser.jar -t 5</pre></td>
    <td>Таймаут загрузки страницы.</td>
  </tr>
  <tr>
    <td align="center"><pre>-o, --output</pre></td>
    <td align="center">String</td>
    <td align="center"><pre>parser.jar -o /home/user/</pre></td>
    <td>Путь до директории для создания файла.</td>
  </tr>
  <tr>
    <td align="center"><pre>-r, --region</pre></td>
    <td align="center">String</td>
    <td align="center"><pre>parser.jar -r Москва</pre></td>
    <td>Регион для парсинга.</td>
  </tr>
  <tr>
    <td align="center"><pre>-u, --url</pre></td>
    <td align="center">String</td>
    <td align="center"><pre>parser.jar -u https://www.citilink.ru/catalog/smartfony/</pre></td>
    <td>Ссылка на категорию.</td>
  </tr>
</tbody>
</table>
