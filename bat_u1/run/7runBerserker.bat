@echo off
F:
cd "F:\OO\bat\java\currency\7"
javac *.java -d "F:\OO\bat\class\currency\7" -encoding UTF-8
cd "F:\OO\bat\class\currency\7"
del "F:\OO\\bat\txt\exp_java7.txt"
setlocal enabledelayedexpansion
for /f   %%i in (F:\OO\\bat\txt\expression.txt)  do (
set target=%%i
echo !target! | java MainClass  >> "F:\OO\\bat\txt\exp_java7.txt"
)

python "F:\OO\bat\bat.py" 7
pause