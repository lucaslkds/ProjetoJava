@echo off

if not exist bin mkdir bin
if exist fontes.txt del fontes.txt

dir /s /b src\*.java > fontes.txt

javac -encoding UTF-8 -d bin @fontes.txt
if errorlevel 1 (
    echo.
    echo Erro ao compilar o projeto.
    pause
    exit /b 1
)

java -cp bin principal.Main
pause
