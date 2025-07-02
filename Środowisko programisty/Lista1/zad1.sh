#!/bin/bash

#sprawdza ilość argumentów
if [ "$#" -ne 1 ]; then 
	echo "Not given directory"
	exit 1
fi

DIR=$1

#sprawdza czy istnieje ścieżka
if [ ! -d $DIR ]; then
	echo "The directory does not exist"
	exit 1
fi

for fname in $(find $DIR -type f); do
	echo $(basename $fname) #tylko nazwa
done
