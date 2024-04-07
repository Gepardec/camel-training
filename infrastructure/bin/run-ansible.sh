#!/bin/sh
  
RESOURCE_PREFIX=jboss-training

PRG=`readlink -e $0`
BIN_DIR=`dirname $PRG`
TRAINING_HOME=`dirname $BIN_DIR`

CONTAINER_RUN="$1 run --rm -it"
IMAGE=quay.io/ansible/ansible-runner:stable-2.12-latest


if [ "X$2" = "Xdependencies" ]; then
    $CONTAINER_RUN -v $TRAINING_HOME/Schulungsumgebung:/runner $IMAGE ansible-galaxy collection install -p ./collections -r requirements.yml
    exit
fi

$CONTAINER_RUN -v $HOME:/home/runner -v $TRAINING_HOME:/projekt $IMAGE \
  ansible-runner run /projekt/Schulungsumgebung \
  -p playbook.yaml --project-dir /projekt/Schulungsumgebung --inventory /projekt/Schulungsumgebung/$RESOURCE_PREFIX/inventory
