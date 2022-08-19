#!/bin/bash
echo $date >> banner.adoc
git add banner.adoc
git commit -m"message" --date $1
# git push
