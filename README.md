# Alation Talend
Talend connector for Alation

# Installing Jar dependencies
# 1. sdk-3.10.0-jar-with-dependencies.jar 
mvn install:install-file -Dfile=sdk-4.1.4-jar-with-dependencies.jar -DgroupId=rdbms -DartifactId=sdk -Dversion=4.1.4 -Dpackaging=jar
# 2. Install sdk_tests-3.1.2-jar-with-dependencies.jar
mvn install:install-file -Dfile=sdk_tests-3.1.2-jar-with-dependencies.jar -DgroupId=rdbms -DartifactId=sdk_tests -Dversion=4.1.4 -Dpackaging=jar


# Compile with maven windows os 
mvn compile exec:java -D"exec.mainClass"="alation.talend.Main" -D"exec.args"="START_SERVER"

# Compile with maven linux or mac os
mvn compile exec:java -D"exec.mainClass"="alation.talend.Main" -D"exec.args"="START_SERVER"

# Verifiy Talend datasource
java -cp sdk-4.1.4-jar-with-dependencies.jar alation.sdk.rdbms.grpc.client.AbstractRDBMSClient --conf configuration-talend.json -o testResult CONFIGURATION_VERIFICATION



# Packaging the Connector

# 1.Make manifest
mvn compile exec:java -D"exec.mainClass"="alation.talend.Main" -D"exec.args"="MAKE_MANIFEST"

In Windows:
mvn compile exec:java -Dexec.mainClass=alation.talend.Main -Dexec.args=MAKE_MANIFEST

# 2.Execute the Maven assembly command to build the JAR file for the connector
mvn clean compile assembly:single 

# 3.Run the package_connector.sh script to create the connector zip file
bash package_connector.sh -m talend-connector -j talend-connector-1.0-SNAPSHOT-jar-with-dependencies.jar -f Dockerfile -d sdk-3.8.2-jar-with-dependencies.jar

# Running sonar qube
mvn clean verify sonar:sonar -D"sonar.projectKey=TalendSonarProject" -D"sonar.sources=." -D"sonar.host.url=http://localhost:9000" -D"sonar.login=sqp_e6f48030b6bff2e4b3c6a271a628e2dc0f7e7702"


