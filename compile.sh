#!/usr/bin/env bash
mvn clean package
rm workflow/lib/SSHSample*jar
cp target/*jar workflow/lib
