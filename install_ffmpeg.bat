@echo off
echo Installing FFmpeg for BiosafeAI...

:: Create temp directory
mkdir temp
cd temp

:: Download FFmpeg
echo Downloading FFmpeg...
powershell -Command "Invoke-WebRequest -Uri 'https://github.com/BtbN/FFmpeg-Builds/releases/download/latest/ffmpeg-master-latest-win64-gpl.zip' -OutFile 'ffmpeg.zip'"

:: Extract FFmpeg
echo Extracting FFmpeg...
powershell -Command "Expand-Archive -Path 'ffmpeg.zip' -DestinationPath '.'"

:: Create FFmpeg directory in Program Files
echo Installing FFmpeg...
mkdir "C:\Program Files\FFmpeg"
xcopy /E /I /Y "ffmpeg-master-latest-win64-gpl\bin\*" "C:\Program Files\FFmpeg"

:: Add to PATH
echo Adding FFmpeg to PATH...
setx PATH "%PATH%;C:\Program Files\FFmpeg" /M

:: Cleanup
cd ..
rmdir /S /Q temp

echo FFmpeg installation completed!
echo Please restart your terminal for the PATH changes to take effect.
pause 