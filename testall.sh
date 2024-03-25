#!/bin/sh

compile=true
start_containers=true
migrate_db=true
start_camel=true
integrationtests=true
cleanup=true

# TEST="-Dit.test=EggOrderIT -DfailIfNoTests=false"

# setup_only=true
if [ X$setup_only = "Xtrue" ]; then
    compile=false
    start_containers=true
    migrate_db=true
    start_camel=true
    integrationtests=false
    cleanup=false
fi


BASE_DIR=`dirname $0`
BASE_DIR=`readlink -e $BASE_DIR`

###########################
# do_cleanup
###########################
do_cleanup() {
    if [ X$cleanup = "Xtrue" ]; then
        echo Do cleanups
    else
        return
    fi

    if [ X$CAMEL_PID = X ]; then 
        echo "Camel not running."
    else
        echo Stop camel on pid $CAMEL_PID
        kill $CAMEL_PID
    fi

    container_name=activemq-artemis
    if [ X$(docker inspect -f '{{.State.Running}}' $container_name 2>/dev/null) = "Xtrue" ]; then
        echo Stop $container_name
        docker stop $container_name
    fi
    container_name=postgres
    if [ X$(docker inspect -f '{{.State.Running}}' $container_name 2>/dev/null) = "Xtrue" ]; then
        echo Stop $container_name
        docker stop $container_name
    fi
}

###########################
###########################
#         MAIN
###########################
###########################
echo Start in $BASE_DIR
cd $BASE_DIR

if [ X$compile = "Xtrue" ]; then 
    echo Compile project
    cd $BASE_DIR/solutions
    mvn clean install || { do_cleanup; exit 1; }
fi

if [ X$start_containers = "Xtrue" ]; then 
    container_name=activemq-artemis
    if [ X$(docker inspect -f '{{.State.Running}}' $container_name 2>/dev/null) = "Xtrue" ]; then 
        echo Container $container_name already running
    else 
        docker run -d --rm --name $container_name -p 8161:8161 -p 61616:61616 -p 5672:5672 -e ARTEMIS_USERNAME=camel -e ARTEMIS_PASSWORD=camel vromero/activemq-artemis:2.9.0-alpine || { do_cleanup; exit 2; }
    fi

    container_name=postgres
    if [ X$(docker inspect -f '{{.State.Running}}' $container_name 2>/dev/null) = "Xtrue" ]; then 
        echo Container $container_name already running
    else 
        docker run -d --rm --name $container_name -p 5432:5432 -e POSTGRES_DB=camel -e POSTGRES_USER=camel -e POSTGRES_PASSWORD=camel postgres || { do_cleanup; exit 3; }
    fi
fi

if [ X$migrate_db = "Xtrue" ]; then 
    echo Migrate database
    cd $BASE_DIR/solutions/commons
    mvn flyway:migrate || { do_cleanup; exit 4; }
fi

if [ X$start_camel = "Xtrue" ]; then 
    cd $BASE_DIR/solutions/best
    mvn camel:run &
    CAMEL_PID=$!
    echo Camel running on PID $CAMEL_PID
    echo Wait a little
    sleep 10
    if [ X$CAMEL_PID = X ]; then 
        echo "Camel not started."
        { do_cleanup; exit 5; }
    else
        echo Continue
    fi
fi

if [ X$integrationtests = "Xtrue" ]; then
    echo Start integrationtests
    cd $BASE_DIR/solutions/best
    mvn -Dmaven.failsafe.skip=false -DskipTests $TEST verify || { do_cleanup; exit 6; }
fi

do_cleanup

echo OK!
