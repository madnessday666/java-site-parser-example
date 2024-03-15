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
    <td align="center"><pre><code>-c, --category</code></pre></td>
    <td align="center"><pre><code>Integer</code></pre></td>
    <td align="center"><pre><code>parser.jar -c 1</code></pre></td>
    <td>Парсит данные категории под номером 1 (включая все подкатегории).</td>
  </tr>
  <tr>
    <td align="center"><pre>-f, --format</pre></td>
    <td align="center"><pre><code>String</code></pre></td>
    <td align="center"><pre><code>parser.jar -f html</code></pre></td>
    <td>Устанавливает формат конечного файла. Варинаты: html, csv.</td>
  </tr>
  <tr>
    <td align="center"><pre>-h, --help</pre></td>
    <td align="center"><pre><code>-</code></pre></td>
    <td align="center"><pre><code>parser.jar -h</code></pre></td>
    <td>Вывести "помощь".</td>
  </tr>  
    <tr>
    <td align="center"><pre>-l, --list</pre></td>
    <td align="center"><pre><code>String</code></pre></td>
    <td align="center"><pre><code>parser.jar -l regions</code></pre></td>
    <td>Вывести список возможных категорий/регионов. Варианты: categories, regions.</td>
  </tr>
    <tr>
    <td align="center"><pre>-p, --pages</pre></td>
    <td align="center"><pre><code>Integer</code></pre></td>
    <td align="center"><pre><code>parser.jar -p 3</code></pre></td>
    <td>Количество страниц категории для парсинга.</td>
  </tr>
    <tr>
    <td align="center"><pre>-o, --output</pre></td>
    <td align="center"><pre><code>String</code></pre></td>
    <td align="center"><pre><code>parser.jar -o /home/user/</code></pre></td>
    <td>Путь до директории для создания файла.</td>
  </tr>
    <tr>
    <td align="center"><pre>-r, --region</pre></td>
    <td align="center"><pre><code>String</code></pre></td>
    <td align="center"><pre><code>parser.jar -r Москва</code></pre></td>
    <td>Регион для парсинга</td>
  </tr>
    <tr>
    <td align="center"><pre>-u, --url</pre></td>
    <td align="center"><pre><code>String</code></pre></td>
    <td align="center"><pre><code>parser.jar -u $URL</code></pre></td>
    <td>Ссылка на категорию</td>
  </tr>
</tbody>
</table>
