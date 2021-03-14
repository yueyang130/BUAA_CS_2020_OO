@echo off
F:
cd "F:\OO\bat\java\currency\3"
javac *.java -d "F:\OO\bat\class\currency\3" -encoding UTF-8
cd "F:\OO\bat\class\currency\3"
del "F:\OO\\bat\txt\exp_java3.txt"
setlocal enabledelayedexpansion
for /f   %%i in (F:\OO\\bat\txt\expression.txt)  do (
set target=%%i
echo !target! | java MainClass  >> "F:\OO\\bat\txt\exp_java3.txt"
)

python "F:\OO\bat\bat.py" 3
pause