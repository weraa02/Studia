#!/bin/bash

if [ "$#" -ne 1 ] || [ ! -d $1 ]; then 
	echo "No proper directory"
	exit 1
fi

DIR=$1
#lista nie indexowana (bedzie uzywac slow)
#declare -A

T_FILE=$(mktemp)

for fname in $(find $DIR -type f); do
	#czyta plik, zmienia wyrazy tak by kazdy w swojej lini i zapisuje w onnym pliku
	cat "$fname" | tr -s '[:space:]' '\n' >> "$T_FILE"
done

#sortuje plik i podlicza
sort "$T_FILE" | uniq -c

#cat "$T_FILE"

rm "$T_FILE"
