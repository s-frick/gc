# Prerequisites

- Java 21
- Scalac, sbt

## Install Scala, sbt via Coursier

[Coursier Install Guide](https://get-coursier.io/docs/cli-installation)

## Build
```
mvn install:install-file \
   -Dfile=gc-core/lib/FitSdk/fit.jar \
   -DgroupId=com.garmin \
   -DartifactId=fit \
   -Dversion=21.124.0 \
   -Dpackaging=jar \
   -DgeneratePom=true
```
`./build.sh`

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
