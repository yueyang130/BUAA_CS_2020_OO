@echo off
F:
cd "F:\OO\bat\java\currency\4"
javac *.java -d "F:\OO\bat\class\currency\4" -encoding UTF-8
cd "F:\OO\bat\class\currency\4"
del "F:\OO\\bat\txt\exp_java4.txt"
setlocal enabledelayedexpansion
for /f   %%i in (F:\OO\\bat\txt\expression.txt)  do (
set target=%%i
echo !target! | java MainClass  >> "F:\OO\\bat\txt\exp_java4.txt"
)

python "F:\OO\bat\bat.py" 4
pause