#!/bin/sh
javac languageScanner.java
javac parser.java

java languageScanner
java parser