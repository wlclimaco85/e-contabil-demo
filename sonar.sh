set COVERAGE_EXCLUSIONS=**/controller/**,**/config/**

mvn sonar:sonar \
    -Dsonar.projectKey=api-modelo \
    -Djava.sources=src/main/java/ \
    -Dsonar.host.url=http://sonar.sankhya.com.br:9000 \
    -Dsonar.login=$TOKEN_SONAR \
    -Dsonar.coverage.exclusions=$COVERAGE_EXCLUSIONS \
    -Dsonar.java.binaries=target/classes \
    -Dsonar.qualitygate.wait=true \
    -Dsonar.java.libraries=$HOME/.m2/**/*.jar
