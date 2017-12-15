echo off
title Compile creation + run

javac languageScanner.java
javac parser.java

java languageScanner
java parser