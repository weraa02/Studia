#!/bin/bash

if [ "$#" -ne 1 ] || [ ! -d "$1" ]; then 
	echo "No proper directory"
	exit 1
fi

DIR=$1

T_FILE=$(mktemp)

for fname in $(find "$DIR" -type f); do
	i=1 #liczy linie
	while read line; do 
		#wybiera tylko duplikaty
		repeated=$(echo "$line" | tr -s '[:space:]' '\n' | sort | uniq -d) 
		for rep in $repeated; do
			text="$rep :$fname :$i :$line"
			echo "$text">> "$T_FILE"
		done					  
		((i++))
	done <"$fname"	
done

sort "$T_FILE"
#cat "$T_FILE"

rm "$T_FILE"
    


