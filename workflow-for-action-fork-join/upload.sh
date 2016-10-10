#!/usr/bin/env bash

echo "scp into oozie server"
scp -P 2222 -r * oozie@127.0.0.1:/home/oozie/work/oozie-investigate/workflow-for-action-fork-join
