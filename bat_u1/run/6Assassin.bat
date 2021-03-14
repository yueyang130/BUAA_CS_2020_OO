@echo off
F:
cd "F:\OO\bat\java\currency\6"
javac *.java -d "F:\OO\bat\class\currency\6" -encoding UTF-8
cd "F:\OO\bat\class\currency\6"
del "F:\OO\\bat\txt\exp_java6.txt"
setlocal enabledelayedexpansion
for /f   %%i in (F:\OO\\bat\txt\expression.txt)  do (
set target=%%i
echo !target! | java MainClass  >> "F:\OO\\bat\txt\exp_java6.txt"
)

python "F:\OO\bat\bat.py" 6
pause