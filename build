#!/usr/bin/env bash

mvn clean install -pl gc-core-api

cd gc-core
sbt clean compile publishM2

cd ..
mvn clean install
