#!/bin/sh

LANG=ko_KR.eucKR
export LANG
cd /usr/local/tomcat/webapps/VMS_MAIL/bin

# JVM_ARGS for VM
##########################
JVM_ARGS="-Du=WEATHER_SYNC"
JVM_ARGS="$JVM_ARGS -Xss512k -Xms128m -Xmx256m"
JVM_ARGS="$JVM_ARGS -cp ../lib/VMS_MAIL.jar:../lib/jdbcpool-0.99.jar:../lib/log4j-1.2.15.jar:../lib/mail.jar:../lib/mysql-connector-java-5.1.13-bin.jar"
/usr/lib/jvm/jre-1.6.0-openjdk/bin/java $JVM_ARGS skycom.vms.execute.MailDaemon 
