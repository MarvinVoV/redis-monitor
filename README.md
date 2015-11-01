# redis-monitor
Redis Visual Monitor

## Install required
* JDK 6+
* MySQL5
* Redis
## How to config
Mysql is required before using redis-monitor. First create a table named '**redis-monitor**'.
You also can config the table name in *redis-monitor.properties* alternatively. Then alter the
config in the *portable/product.xml* or you can config portable file by yourself in the pom.xml.

    create table redis_monitor
    (
        info varchar(1000),
        time timestamp
    )

The **monitor.rate** in the *redis-monitor.properties* file indicator that it will gather information
from redis using the INFO command every period specified by **monitor.rate** in milliseconds.

## How to package
Package a war file after the configuration. you can use the command as follow:

    mvn clean package -DportableConfig="src/main/portable/product.xml"

Or you can config *pom.xml*

     <plugin>
        <groupId>com.juvenxu.portable-config-maven-plugin</groupId>
        <artifactId>portable-config-maven-plugin</artifactId>
        <version>1.1.5</version>
        <executions>
            <execution>
                <goals>
                    <goal>replace-package</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <portableConfig>src/main/portable/product.xml</portableConfig>
        </configuration>
     </plugin>

then execute the command as follow:

    mvn clean package

## How to view
If you deploy the war on you localhost. Then visit *http://localhost:8080/index*