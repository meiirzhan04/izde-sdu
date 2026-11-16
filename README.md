# Ізде SDU

**Ізде SDU** is an interactive SDU University campus guide and step-by-step quest Android app for freshmen and campus visitors.

Users pick a campus destination on the map (academic blocks `D`–`I`, canteen, library, medical center, dormitory, or `SDU Life` gym), follow checkpoints from the main gate, and answer SDU trivia questions along the way to earn points.

---

## Team & Contributions

- **Meiirzhan (`meirzhan`)** — Clean Architecture + MVVM setup, manual DI (`AppContainer`, `IzdeApp`), Type-Safe Navigation (`Routes`, `IzdeNavHost`), route checkpoint and question screens (`RouteScreen`, `QuestionScreen`, `RouteMapPreview`, `RouteViewModel`, `QuestionViewModel`).
- **Yerasyl (`yeeerosh`)** — interactive campus map (`CampusMap`, `MapScreen`, `MapViewModel`), campus location models and data (`Place`, `PlaceRepository`, `PlaceLocalDataSource`), place details screen (`PlaceScreen`, `PlaceViewModel`), `PlaceCard` and `PlacePin` components.
- **Zhanel (`zhanel`)** — reusable UI components (`CheckpointRow`, `AnswerOption`, `QuestionCard`), complete quest routes for all 7 destinations (`RouteLocalDataSource`), step progression and validation logic (`SubmitRouteAnswerUseCase`, `CompleteRouteUseCase`, `RouteProgress`, `CheckpointProgress`), unit tests.

---

## Features

- **Interactive Campus Map:** displays campus blocks, pins for 7 key locations, and the user's live total score.
- **Place Details:** shows the building block, description, nearby landmarks, and question count for the route.
- **Checkpoint Route Overview:** previews the route path and lists checkpoints with live completion statuses.
- **SDU Trivia Checkpoints:** 4-option questions with instant answer checking, automatic/manual hints, `+10` points per checkpoint, duplicate-reward prevention, and step-order enforcement.
- **Route Summary:** displays completed checkpoints, points earned on the route, and mistakes before returning to the map (`popUpTo<MapRoute>`).

---

## Architecture & Tech Stack

The project follows **Clean Architecture** with **MVVM**:

```text
app/src/main/java/com/sdu/izdesdu/
├── IzdeApp.kt                  # Application class holding AppContainer
├── MainActivity.kt             # Compose UI host and IzdeNavHost
├── di/
│   └── AppContainer.kt         # Manual DI wiring DataSource, Repository, and UseCases
├── domain/                     # Pure Kotlin models, repository interfaces, and UseCases
│   ├── model/                  # Place, Route, CheckPoint, Question, UserProgress, RouteProgress
│   ├── repository/             # PlaceRepository, RouteRepository, ProgressRepository
│   └── usecase/                # GetPlacesUseCase, GetRouteByIdUseCase, SubmitRouteAnswerUseCase, etc.
├── data/                       # Repository implementations and local data sources
│   ├── local/                  # PlaceLocalDataSource, RouteLocalDataSource, ProgressLocalDataSource
│   └── repository/             # PlaceRepositoryImpl, RouteRepositoryImpl, ProgressRepositoryImpl
└── presentation/               # Jetpack Compose UI, ViewModels (StateFlow<UiState>), and navigation
    ├── navigation/             # @Serializable Routes and IzdeNavHost
    ├── theme/                  # Color, Type, Theme (Material 3)
    ├── components/             # CampusMap, PlaceCard, PlacePin, CheckpointRow, AnswerOption, QuestionCard
    ├── map/                    # MapScreen, MapViewModel, MapUiState
    ├── place/                  # PlaceScreen, PlaceViewModel, PlaceUiState
    ├── route/                  # RouteScreen, RouteViewModel, RouteUiState, RouteMapPreview
    └── question/               # QuestionScreen, QuestionViewModel, QuestionUiState
```

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, StateFlow + ViewModel, Type-Safe Navigation Compose (`@Serializable`, `toRoute<T>()`), `kotlinx.serialization`, JUnit 4.

---

## Build & Tests

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests (QuestionViewModelTest, RouteProgressTest, RouteLocalDataSourceTest)
./gradlew testDebugUnitTest
```
