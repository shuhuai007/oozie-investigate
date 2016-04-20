#!/usr/bin/env bash


username=oozie
projectname=ssh-sample
project_dir=/user/${username}/apps/${projectname}/workflow
hadoop fs -rmr ${project_dir}

hadoop fs -mkdir -p ${project_dir}
hadoop fs -put *.sh ${project_dir}
hadoop fs -put lib ${project_dir}
hadoop fs -put *.xml ${project_dir}

oozie job -config job.properties -run
