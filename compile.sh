#!/usr/bin/env bash
mvn clean package
rm workflow/lib/SSHSample*jar
cp target/*jar workflow/lib


echo "scp into oozie server"
scp -P 2222 -r workflow oozie@127.0.0.1:/home/oozie/work/oozie-investigate
