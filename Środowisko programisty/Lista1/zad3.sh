#!/bin/bash

if [ "$#" -ne 1 ] || [ ! -d $1 ]; then 
	echo "No proper directory"
	exit 1
fi

DIR=$1

T_FILE=$(mktemp)

for fname in $(find $DIR -type f); do
	#czyta plik, wyrazy w nowej lini, posortowane, usuwa duplikaty
	#cat "$fname" | tr -s '[:space:]' '\n' | sort | uniq -u >> "$T_FILE"
	#cat "$fname" | tr -s '[:space:]' '\n' | sort | uniq -u | sort -nr >> "$T_FILE"
	#echo '/n' >> "$T_FILE"
	cat "$fname" | tr -s '[:space:]' '\n' | sort | uniq | sort -nr >> "$T_FILE"
done

#sortuje plik i podlicza - kazdy wyraz po razie na plik
sort "$T_FILE" -n | uniq -c

#cat "$T_FILE"

rm "$T_FILE"
