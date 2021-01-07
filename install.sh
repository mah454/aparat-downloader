#!/bin/bash 

INSTALL_PATH="/opt/aparat-dl" 

if [ ! -d $INSTALL_PATH ] ; then 
		mkdir $INSTALL_PATH 
fi

install -m 644 target/aparat-dl.jar /opt/aparat-dl/aparat-dl.jar
install -m 755 aparat-dl /opt/aparat-dl/aparat-dl 


