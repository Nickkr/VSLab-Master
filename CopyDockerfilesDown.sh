#!/bin/sh

ScriptDir="$(dirname "$0")"
RootDir="$(realpath "$ScriptDir"/./)"
OriginFiles="$(realpath "$ScriptDir"/.dockerignore "$ScriptDir"/*.dockerfile)"

for dir in $(find "$RootDir" -mindepth 2 -name pom.xml -printf '%h\n'); do
  echo cp --verbose --update $OriginFiles "$dir"
  cp --verbose --update $OriginFiles "$dir"
  echo
done

read -p "Press enter to continue"
