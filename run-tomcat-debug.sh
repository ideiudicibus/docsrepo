#!/bin/bash
REMOTE_PORT=$1
MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$REMOTE_PORT,server=y,suspend=n -Xms256m -Xmx512m -XX:PermSize=128m $MAVEN_OPTS"  mvn clean tomcat:run  -Dmvn.test.skip=true
