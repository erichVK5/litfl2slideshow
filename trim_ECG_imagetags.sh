#!/bin/sh

# use this to convert image tags into a path followed by the alt label
# we finish buy weeding out images that are not actual ECGs but relate to
# ECG text materials which wrap the ECG pages by excluding
# "width=" and "permalink" tags that get past our sed substitutions
# leaving us with lines of the format:
#
# /path/to/image"the image's alt description text
#

cat $1 | sed 's@\(" data-medium-file="\).*\(data-large-file="https://\)@@g' | sed 's@\(" src=\).*\(alt="\)@"@g' | sed 's@\(" class\).*@@g' | grep -v "permalink" | grep -v "width="
