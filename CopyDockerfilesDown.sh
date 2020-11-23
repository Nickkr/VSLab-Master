#!/bin/sh

OriginFiles=".dockerignore *.dockerfile"

for dir in $(find . -mindepth 2 -name pom.xml -printf '%h\n'); do
  cp --verbose --update $OriginFiles $dir
done
