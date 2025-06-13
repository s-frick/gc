# Prerequisites

- Java 21
- Scalac, sbt

## Install Scala, sbt via Coursier

[Coursier Install Guide](https://get-coursier.io/docs/cli-installation)

## Build

`./build`

## Start Dev

Build first!

Backend:
`mvn spring-boot:run -pl gc-main`

Frontend:

```
cd gc-ui
npm i
npm run dev
```

[Click](http://localhost:5173)

## Upload a workout manually

```

cd http_tests
httpyac -n upload upload_workout.http
```

Or use Intellij or VSCode (via Plugin) to run the http file.
