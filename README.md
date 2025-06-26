# Data Filter

Библиотека для создания сложных критериев фильтрации, сортировки и пагинации данных через единый интерфейс. Включает лексический анализатор и парсер для обработки строковых выражений фильтрации.

## Особенности

- Унифицированный интерфейс для фильтрации, сортировки и пагинации данных
- Поддержка сложных критериев фильтрации с вложенными группами условий
- Лексический анализатор и парсер для обработки строковых выражений фильтрации
- Интеграция с Spring Boot, Spring Data JPA и Spring Data MongoDB
- Поддержка операторов сравнения: =, !=, <, >, <=, >=, in, not in
- Поддержка логических операторов: & (AND), | (OR)
- Поддержка сортировки по нескольким полям
- Поддержка пагинации результатов

## Установка

### Gradle

```kotlin
// Основной модуль фильтрации
implementation("team.black-hole.data:filter-backend:0.0.1")

// Интеграция со Spring Boot
implementation("team.black-hole.data:filter-spring-integration:0.0.1")
```

### Maven

```xml
<!-- Основной модуль фильтрации -->
<dependency>
    <groupId>team.black-hole.data</groupId>
    <artifactId>filter-backend</artifactId>
    <version>0.0.1</version>
</dependency>

<!-- Интеграция со Spring Boot -->
<dependency>
    <groupId>team.black-hole.data</groupId>
    <artifactId>filter-spring-integration</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Использование

### Включение фильтрации в Spring Boot приложении

```java
@SpringBootApplication
@EnableFiltration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Использование с Spring Data JPA

```java
@Repository
public interface UserRepository extends ExtendedJpaRepository<User, Long> {
    // Все методы ExtendedJpaRepository доступны
}

// Использование в сервисе
@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Page<User> findUsers(Filter filter) {
        return userRepository.findAll(filter);
    }
}
```

### Использование с Spring Data MongoDB

```java
@Repository
public interface ProductRepository extends ExtendedMongoRepository<Product, String> {
    // Все методы ExtendedMongoRepository доступны
}

// Использование в сервисе
@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Page<Product> findProducts(Filter filter) {
        return productRepository.findAll(filter);
    }
    
    // Использование с агрегацией MongoDB
    public Page<Product> findProductsWithAggregation(Filter filter) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("active").is(true))
        );
        return productRepository.findAll(aggregation, filter);
    }
}
```

### Использование в REST контроллере

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public Page<User> getUsers(Filter filter) {
        return userService.findUsers(filter);
    }
}
```

### Примеры строковых выражений фильтрации

#### Пагинация

```
[10, 500]  // Страница 10, размер страницы 500
```

#### Сортировка

```
[^field1, field2]  // Сортировка по field1 (по возрастанию) и field2 (по возрастанию)
```

#### Простые критерии фильтрации

```
[field1 = 'value1']  // Поле field1 равно 'value1'
[field1 != 5]  // Поле field1 не равно 5
[field1 < 5]  // Поле field1 меньше 5
[field1 > 5]  // Поле field1 больше 5
[field1 <= 5]  // Поле field1 меньше или равно 5
[field1 >= 5]  // Поле field1 больше или равно 5
[field1 in {1, 2, 3}]  // Поле field1 содержится в списке {1, 2, 3}
[field1 not in {1, 2, 3}]  // Поле field1 не содержится в списке {1, 2, 3}
```

#### Сложные критерии фильтрации

```
[field1 = 'value1' & field2 = 'value2']  // field1 равно 'value1' И field2 равно 'value2'
[field1 = 'value1' | field2 = 'value2']  // field1 равно 'value1' ИЛИ field2 равно 'value2'
[field1 < 5 & (field2 >= 10 | field3 <= 20)]  // field1 < 5 И (field2 >= 10 ИЛИ field3 <= 20)
```

#### Вложенные критерии фильтрации

```
[field1 = 'value1' & (field2 = 'value2' | field3 = 'value3' & (field4 = 'value4' | field5 = 'value5'))]
```

## Модули

### filter-backend

Основной модуль, содержащий базовую функциональность фильтрации:

- Интерфейсы и реализации для фильтров, критериев и сортировки
- Лексический анализатор для разбора строковых выражений фильтрации
- Парсер для преобразования токенов в объекты фильтрации
- Построители (builders) для программного создания фильтров

### filter-spring-integration

Модуль интеграции со Spring Boot:

- Автоконфигурация для Spring Boot
- Интеграция с Spring Data JPA
- Интеграция с Spring Data MongoDB
- Аргумент-резолверы для автоматического преобразования параметров запросов в объекты фильтрации
- Поддержка аннотаций для настройки фильтрации

## Лицензия

Проект распространяется под лицензией Apache License 2.0. Подробности в файле LICENSE.

# Публикация

    ./gradlew clean jreleaserConfig build publish
    ./gradlew jreleaserFullRelease

## Разработчики

- Aleksey Plekhanov (@AseWhy) - astecoms@gmail.com

## Ссылки

- [GitHub репозиторий](https://github.com/black-hole-team/data-filter)
