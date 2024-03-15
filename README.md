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
    <td align="center"><code>parser.jar -c 1</code></td>
    <td>Парсит данные категории под номером 1 (включая все подкатегории).</td>
  </tr>
  <tr>
    <td align="center"><pre>-f, --format</pre></td>
    <td align="center">String</td>
    <td align="center"><code>parser.jar -f html</code></td>
    <td>Устанавливает формат конечного файла. Варинаты: html, csv.</td>
  </tr>
  <tr>
    <td align="center"><pre>-h, --help</pre></td>
    <td align="center">-</td>
    <td align="center"><code>parser.jar -h</code></td>
    <td>Вывести "помощь".</td>
  </tr>  
    <tr>
    <td align="center"><pre>-l, --list</pre></td>
    <td align="center">String</td>
    <td align="center"><code>parser.jar -l regions</code></td>
    <td>Вывести список возможных категорий/регионов. Варианты: categories, regions.</td>
  </tr>
    <tr>
    <td align="center"><pre>-p, --pages</pre></td>
    <td align="center">Integer</td>
    <td align="center"><code>parser.jar -p 3</code></td>
    <td>Количество страниц категории для парсинга.</td>
  </tr>
    <tr>
    <td align="center"><pre>-o, --output</pre></td>
    <td align="center">String</td>
    <td align="center"><code>parser.jar -o /home/user/</code></td>
    <td>Путь до директории для создания файла.</td>
  </tr>
    <tr>
    <td align="center"><pre>-r, --region</pre></td>
    <td align="center">String</td>
    <td align="center"><code>parser.jar -r Москва</code></td>
    <td>Регион для парсинга.</td>
  </tr>
    <tr>
    <td align="center"><pre>-u, --url</pre></td>
    <td align="center">String</td>
    <td align="center"><code>parser.jar -u $URL</code></td>
    <td>Ссылка на категорию.</td>
  </tr>
</tbody>
</table>
