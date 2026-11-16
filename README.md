# Ізде SDU

**Ізде SDU** — мобильное приложение-гид для первокурсников и гостей кампуса SDU University в формате интерактивного пошагового квеста.

Пользователь выбирает место на карте кампуса (блоки `D`–`I`, столовая, библиотека, медпункт, общежитие, спортзал `SDU Life`), проходит путь от главного входа через контрольные точки и на каждой точке отвечает на вопрос про университет, получая очки и подсказки.

---

## Участники команды и вклад

- **Meirzhan (`meirzhan`)** — архитектура (`Clean Architecture + MVVM`), ручной DI (`AppContainer`, `IzdeApp`), Type-Safe Navigation (`Routes`, `IzdeNavHost`), экраны прохождения маршрута и вопросов (`RouteScreen`, `QuestionScreen`, `RouteMapPreview`, `RouteViewModel`, `QuestionViewModel`).
- **Yersultan (`yeeerosh`)** — интерактивная карта кампуса (`CampusMap`, `MapScreen`, `MapViewModel`), модели и данные локаций (`Place`, `PlaceRepository`, `PlaceLocalDataSource`), экран подробностей места (`PlaceScreen`, `PlaceViewModel`), UI-компоненты `PlaceCard` и `PlacePin`.
- **Zhanel (`zhanel`)** — переиспользуемые UI-компоненты (`CheckpointRow`, `AnswerOption`, `QuestionCard`), квест-маршруты по всем 7 локациям (`RouteLocalDataSource`), логика прогресса и валидации шагов (`SubmitRouteAnswerUseCase`, `CompleteRouteUseCase`, `RouteProgress`, `CheckpointProgress`), unit-тесты.

---

## Основные функции

- **Интерактивная карта кампуса:** отображение плана кампуса с подписями блоков, пинами 7 локаций и текущим счётом игрока.
- **Карточка места:** блок, описание, ближайшие ориентиры и количество вопросов по пути.
- **Путь по контрольным точкам:** визуализация маршрута на мини-карте и список точек со статусами прохождения.
- **Квест-вопросы на точках:** 4 варианта ответа, мгновенная проверка, автоматическая и ручная подсказка, начисление `+10` очков за точку, защита от повторного начисления баллов и пропуска шагов.
- **Итог маршрута:** статистика пройденных точек, набранных очков и ошибок с возвратом на главный экран карты (`popUpTo<MapRoute>`).

---

## Архитектура и технологии

Проект разделён по **Clean Architecture** и **MVVM**:

```text
app/src/main/java/com/sdu/izdesdu/
├── IzdeApp.kt                  # Application с единым AppContainer
├── MainActivity.kt             # Хост Compose UI и IzdeNavHost
├── di/
│   └── AppContainer.kt         # Ручной DI для DataSource, Repository и UseCases
├── domain/                     # Чистый Kotlin: модели, интерфейсы репозиториев и UseCases
│   ├── model/                  # Place, Route, CheckPoint, Question, UserProgress, RouteProgress
│   ├── repository/             # PlaceRepository, RouteRepository, ProgressRepository
│   └── usecase/                # GetPlacesUseCase, GetRouteByIdUseCase, SubmitRouteAnswerUseCase и др.
├── data/                       # Реализации репозиториев и локальные источники данных
│   ├── local/                  # PlaceLocalDataSource, RouteLocalDataSource, ProgressLocalDataSource
│   └── repository/             # PlaceRepositoryImpl, RouteRepositoryImpl, ProgressRepositoryImpl
└── presentation/               # Compose UI, ViewModel (StateFlow<UiState>) и навигация
    ├── navigation/             # Routes (@Serializable) и IzdeNavHost
    ├── theme/                  # Color, Type, Theme (Material 3)
    ├── components/             # CampusMap, PlaceCard, PlacePin, CheckpointRow, AnswerOption, QuestionCard
    ├── map/                    # MapScreen, MapViewModel, MapUiState
    ├── place/                  # PlaceScreen, PlaceViewModel, PlaceUiState
    ├── route/                  # RouteScreen, RouteViewModel, RouteUiState, RouteMapPreview
    └── question/               # QuestionScreen, QuestionViewModel, QuestionUiState
```

**Стек:** Kotlin, Jetpack Compose, Material 3, StateFlow + ViewModel, Type-Safe Navigation Compose (`@Serializable`, `toRoute<T>()`), `kotlinx.serialization`, JUnit 4.

---

## Сборка и тесты

```bash
# Сборка debug APK
./gradlew assembleDebug

# Запуск unit-тестов (QuestionViewModelTest, RouteProgressTest, RouteLocalDataSourceTest)
./gradlew testDebugUnitTest
```
