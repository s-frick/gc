#!/usr/bin/env bash

httpyac -a -o short upload_workout.http && cat test.json | jq -c  | openssl dgst -sha256 | grep -q "25d7b5b8307f236b895134809c76ed3e0eb089b82bb9eb8ff97d83c2d1c29fa8" && echo -e "\e[32mHash OK\e[0m" || echo "\e[31mHash Not OK\e[0m"
