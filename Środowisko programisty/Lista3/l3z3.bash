#!/bin/bash

#pobiera parametry
if [ "$#" -ne 2 ]; then 
	echo "Not enough arguments"
	exit 1
fi

REV=$1
DIR=$2

#tymczasowy folder 
T_DIR=$(mktemp -d)
cd $T_DIR

#q - zeby nie wysyłało informacji co pobiera
svn co -q -r "$1" "$2"

#ZAD3 L1
T_FILE=$(mktemp)
for fname in $(find . -type f -not -path "*/.svn/*"); do #omija folder svn
	#czyta plik, wyrazy w nowej linii, posortowane, usuwa duplikaty
	cat "$fname" | tr -s '[:space:]' '\n' | sort | uniq | sort -nr >> "$T_FILE"
done
sort "$T_FILE" -n | uniq -c
rm "$T_FILE"

rm -rf "$T_DIR"

