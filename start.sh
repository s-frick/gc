#!/usr/bin/env bash

case "$1" in
"main")
  echo "Starte GC Main..."
  mvn spring-boot:run -pl gc-main
  ;;
"ui")
  echo "Starte GC UI..."
  npm run dev --prefix gc-ui
  ;;
*)
  echo "Verwendung: $0 {main|ui}"
  exit 1
  ;;
esac
