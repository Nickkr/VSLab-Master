@echo off

REM If the first argument is set, attempt it to be an artifactId
set MAVEN_PROJECT=
if NOT ["%~1"]==[""] (
    set MAVEN_PROJECT=--projects :%~1
)

set MAVEN_OPTIONS=--batch-mode --fail-fast --strict-checksums --define skipTests
echo Compile source files in %CD% %MAVEN_PROJECT%
call mvn %MAVEN_OPTIONS% %MAVEN_PROJECT% package || exit /B 1
echo.

set BUILD_FROM=ExecutableJar

echo Build docker image %1
docker-compose build %1 || exit /B 1
echo.

echo Startup docker image %*
start "Docker Service" docker-compose up %* || (echo Error during startup && exit /B 1)
echo.