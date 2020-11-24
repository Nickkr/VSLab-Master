#!/bin/sh

ScriptDir="$(dirname "$0")"
RootDir="$(realpath $ScriptDir/./)"
OriginFiles="$(realpath $ScriptDir/.dockerignore $ScriptDir/*.dockerfile)"

for dir in $(find $RootDir -mindepth 2 -name pom.xml -printf '%h\n'); do
  cp --verbose --update $OriginFiles $dir
done

read -p "Press enter to continue"