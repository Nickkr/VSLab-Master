@echo off

set MAVEN_OPTIONS=--batch-mode --fail-fast --strict-checksums --define skipTests
echo Compile source files in %CD%
call mvn %MAVEN_OPTIONS% package || exit /B 1

set BUILD_FROM=ExecutableJar

echo Build docker image %*
docker-compose build %* || exit /B 1

echo Startup docker image %*
start "Docker Service" docker-compose up %* || (echo Error && exit /B 1)
