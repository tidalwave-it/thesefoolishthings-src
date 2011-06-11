#!/bin/sh

set -e -x

mvn archetype:generate \
    -DarchetypeCatalog=local \
    -DarchetypeGroupId=it.tidalwave.thesefoolishthings \
    -DarchetypeArtifactId=project-archetype 
