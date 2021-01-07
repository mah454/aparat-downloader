#!/bin/bash 

if [ $UID -ne 0 ] ; then 
		echo "Please execute command as root"
		exit 1
fi

INSTALL_PATH="/opt/aparat-dl" 

if [ ! -d $INSTALL_PATH ] ; then 
		mkdir $INSTALL_PATH 
fi

install -m 644 target/aparat-dl.jar $INSTALL_PATH/aparat-dl.jar
install -m 755 aparat-dl $INSTALL_PATH/aparat-dl
update-alternatives --install /usr/local/bin/aparat-dl aparat-dl $INSTALL_PATH/aparat-dl 100


