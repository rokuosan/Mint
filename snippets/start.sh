#!/bin/sh

java -Xms4G -Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -jar paper.jar nogui
