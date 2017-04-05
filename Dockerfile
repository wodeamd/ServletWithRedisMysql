FROM tomcat:8.0
COPY ./target/registration-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/reg.war
CMD ["catalina.sh", "run"]


