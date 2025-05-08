@echo off
echo Creating required directories for BiosafeAI...

:: Create upload directories
mkdir "uploads"
mkdir "uploads\images"
mkdir "uploads\videos"
mkdir "uploads\documents"
mkdir "uploads\audio"

:: Create backup directory
mkdir "backup"

:: Create temp directory
mkdir "temp"

echo Directories created successfully!
pause 