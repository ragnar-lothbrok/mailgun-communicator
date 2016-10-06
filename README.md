http://localhost:8888/mails/send

mvn clean install -DskipTests

cd target

nohup java -jar mailgun-communicator.jar --spring.config.location=/tmp/application.properties -Xmx2048m -Xms256m > /var/log/mail.log &