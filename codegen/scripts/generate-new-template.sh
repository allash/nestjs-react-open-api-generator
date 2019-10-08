#!/bin/bash

[[ -z "$1" ]] && echo "Please provide custom template name as an argument" && exit 1

CUSTOM_TEMPLATE_NAME=$1

java -jar ../lib/openapi-generator-cli.jar meta -o ../generators/$CUSTOM_TEMPLATE_NAME -n CUSTOM_TEMPLATE_NAME -p com.my.company.codegen