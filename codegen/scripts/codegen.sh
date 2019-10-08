#!/bin/bash

CUSTOM_TEMPLATE_NAME=$1
INPUT_FILE_LOCATION=$2
OUTPUT_LOCATION=$3

[[ -z "$CUSTOM_TEMPLATE_NAME" ]] && echo "Please provide custom template name as an argument" && exit 1
[[ -z "$INPUT_FILE_LOCATION" ]] && echo "Please provide input file location as an argument" && exit 1
[[ -z "$OUTPUT_LOCATION" ]] && echo "Please provide output location as an argument" && exit 1

cd ../generators/$CUSTOM_TEMPLATE_NAME 

echo "Building jar file..."

mvn clean 
mvn package

cd ../../scripts

java -cp ../generators/$CUSTOM_TEMPLATE_NAME/target/$CUSTOM_TEMPLATE_NAME-openapi-generator-1.0.0.jar:../lib/openapi-generator-cli.jar org.openapitools.codegen.OpenAPIGenerator generate -g $CUSTOM_TEMPLATE_NAME -i $INPUT_FILE_LOCATION -o ../$OUTPUT_LOCATION 




