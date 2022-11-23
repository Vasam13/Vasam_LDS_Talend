#!/usr/bin/env bash

MODULE='talend'

echo "Building the ${MODULE} connector..." &&
echo 'Step 1: Cleaning...' &&
rm -rf ${MODULE}-connector-1.0-SNAPSHOT-jar-with-dependencies.jar &&
rm -rf alation-${MODULE}.zip &&
rm -rf MANIFEST.MF &&
echo 'Step 2: Compiling the code...' &&
mvn compile exec:java -Dexec.mainClass="alation.${MODULE}.Main" -Dexec.args="MAKE_MANIFEST" &&
echo 'Step 3: building the code...' &&
mvn clean compile assembly:single &&
echo "Step 4: ${MODULE}-connector-1.0-SNAPSHOT-jar-with-dependencies.jar is successfully generated!" &&
cp target/${MODULE}-connector-1.0-SNAPSHOT-jar-with-dependencies.jar . &&
echo 'Step 5: Packaging the connector' &&
sudo ./package_connector.sh -m ${MODULE} -j ${MODULE}-connector-1.0-SNAPSHOT-jar-with-dependencies.jar -f Dockerfile -d sdk-3.10.0-jar-with-dependencies.jar &&
rm -rf ${MODULE}-connector-1.0-SNAPSHOT-jar-with-dependencies.jar &&
rm -rf MANIFEST.MF &&
echo "${MODULE} connector is successfully created as alation-${MODULE}.zip"