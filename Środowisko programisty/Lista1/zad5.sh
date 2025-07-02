#!/bin/bash

if [ "$#" -ne 1 ] || [ ! -d $1 ]; then 
	echo "No proper directory"
	exit 1
fi

DIR=$1
    
for fname in $(find $DIR -type f); do
	T_FILE=$(mktemp)
	tr 'a' 'A' <"$fname" > "$T_FILE"
    mv "$T_FILE" "$fname"
done

