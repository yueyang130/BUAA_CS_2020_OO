@echo off
F:
cd "F:\OO\bat\java\currency\2"
javac *.java -d "F:\OO\bat\class\currency\2" -encoding UTF-8
cd "F:\OO\bat\class\currency\2"
del "F:\OO\\bat\txt\exp_java2.txt"
setlocal enabledelayedexpansion
for /f   %%i in (F:\OO\\bat\txt\expression.txt)  do (
set target=%%i
echo !target! | java MainClass  >> "F:\OO\\bat\txt\exp_java2.txt"
)

python "F:\OO\bat\bat.py" 2
pause