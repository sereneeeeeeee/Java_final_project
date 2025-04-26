@echo off
echo Compile Java File...

javac -cp ".;movieSaveToDatabase\libs\gson-2.10.1.jar;movieSaveToDatabase\libs\mysql-connector-j-9.2.0.jar" movieSaveToDatabase\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compile Fail...
    pause
    exit /b
)

echo Compile successfully, running the program...
echo --------------------------------------

java -cp ".;movieSaveToDatabase\libs\gson-2.10.1.jar;movieSaveToDatabase\libs\mysql-connector-j-9.2.0.jar" movieSaveToDatabase.Main

echo --------------------------------------
echo end
pause