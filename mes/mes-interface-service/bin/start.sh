#!/bin/bash
cd `dirname $0`
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
LOGS_DIR=$DEPLOY_DIR/logs
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log
LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

PIDS=`ps -f | grep java | grep "$CONF_DIR" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The server already started!"
    echo "PID: $PIDS"
    exit 1
fi

echo -e "Starting  ...\c"
nohup java -server -Xms64m -Xmx1024m -classpath $CONF_DIR:$LIB_JARS cn.emay.mis.core.Main  >> $STDOUT_FILE 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1 
    PIDS=`ps -f | grep java | grep "$CONF_DIR" |awk '{print $2}'`
	if [ -n "$PIDS" ]; then
    	COUNT=1
    	break
	fi
done

echo "OK!"
PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"