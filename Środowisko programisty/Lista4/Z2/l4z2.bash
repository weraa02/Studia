#!/bin/bash

#pobiera parametry
if [ "$#" -ne 3 ]; then 
	echo "Not enough arguments"
	exit 1
fi

REV1=$1
REV2=$2
DIR=$3

if ((REV1 > REV2)); then
	echo "Wrong order of revision's numbers"
	echo "But it they will be replaced for you"
	#zamiana by dobra kolejnosc
	REV2=$REV1
	REV1=$2
fi

T_DIR=$(mktemp -d)
REPO=$(pwd)/$(basename $DIR)

#usun jesli istnieje
if [ -d ./"$REPO" ]; then
	rm -rf "$REPO" 
fi

#repozytorium git
git init "$REPO" -q
#cd "$REPO"
		
for REV in $( seq "$REV1" "$REV2" ); do #miedzy tymi
	#
	rm -rf "$T_DIR"
	rm -rf "$REPO"/*
	svn export -q -r "$REV" "$DIR" $T_DIR/ # to zeby nei miec .svn
	cp -r "$T_DIR"/* "$REPO"/ #przenosi

	
	cd "$REPO"
	git add .
	git reset -q .git/ .svn/ .gitignore #nie te
								
	MESSAGE=$(svn log -r "$REV" "$DIR" )
	git commit  -m "$MESSAGE"		

done


rm -rf "$T_DIR"




