#!/bin/sh

# we use this to extract the image tags from the html files listed in $1
# and aim to get anything with a large image, to do with ECGs, with an alt
# text label, but specifically exclude the QUIZ material which has no alt
# text label attached to the ECG image and lacks a meaningful ECG file name 

while read html_line
do
	cat $html_line | grep "data-large-file=\"" | grep "ECG" | grep -v "QUIZ" | grep "alt=\""
done < $1	
