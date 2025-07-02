#!/bin/bash

#sprawdza ilość argumentów
if [ "$#" -ne 2 ]; then 
	echo "Not enough arguments"
	echo "Give me number of revision and repository link"
	exit 1
fi

REV=$1
DIR=$2

#tworzy tymczasowy folder o niezajetej nazwie
TNAME="temp"
TDIR="$TNAME"

COUNT=1
while [ -d "./$TDIR" ]; do
	TDIR="$TNAME$COUNT"
	COUNT=$((COUNT+1))
done
mkdir $TDIR
#ls

cd ./$TDIR
svn co -q -r "$1" "$2"

cd ..

#./zad2.sh ./$T_FILE  #~ (zmiana by ignorować folder svn-owy będący "gdzieś w środku")
T_FILE=$(mktemp)
for fname in $(find $TDIR -type f -not -path "*/.svn/*"); do
	#czyta plik, zmienia wyrazy tak by kazdy był w swojej lini i zapisuje w innym pliku
	cat "$fname" | tr -s '[:space:]' '\n' >> "$T_FILE"
done
sort "$T_FILE" | uniq -c
rm "$T_FILE"

#usuwa folder (rekurencyjnie/z zawartością) (i bez dodatkowych zapytań - bo .svn)
rm -rf "$TDIR"

