#!/bin/bash
set -e

mvn clean

source .env
sed -i -e "s/GMSP_OAUTHTOKEN/${GMSP_OAUTHTOKEN}/" settings.xml

mvn -s ./settings.xml -Drtr.release=true -P release

git config --global user.email "me@ronjenkins.info"
git config --global user.name "Ronald Jack Jenkins Jr."

POM_VERSION=$(cat .version | xargs)

git add pom.xml README.md
git commit -m "Committing release $POM_VERSION [skip ci]"
git push

git tag "$POM_VERSION"
git push origin "$POM_VERSION"
