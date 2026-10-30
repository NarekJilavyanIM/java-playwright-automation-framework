#!/usr/bin/env bash
set -euo pipefail

if ! command -v mvn >/dev/null 2>&1; then
  sudo DEBIAN_FRONTEND=noninteractive apt-get update
  sudo DEBIAN_FRONTEND=noninteractive apt-get install -y maven
fi

mvn -B dependency:resolve
mvn -B compile test-compile
mvn -B exec:java -Dexec.args="install-deps"
mvn -B exec:java -Dexec.args="install firefox chromium"
