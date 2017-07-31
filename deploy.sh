#!/bin/bash
set -e

mvn clean

set -o allexport
source .env
set +o allexport
rm .env

mvn -s settings.xml -Drtr.release=true -P release

git config --global user.email "me@ronjenkins.info"
git config --global user.name "Ronald Jack Jenkins Jr."

POM_VERSION=$(cat .version | xargs)

git add pom.xml
git commit -m "Committing release $POM_VERSION"

git tag "$POM_VERSION"
git push origin "$POM_VERSION"
