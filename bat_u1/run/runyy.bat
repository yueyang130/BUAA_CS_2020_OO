@echo off
F:
cd "F:\OO\hw\u1_3\src"
javac *.java -d "F:\OO\bat\class\yy" -encoding UTF-8

cd "F:\OO\bat\class\yy"
java MainClass < "F:\OO\\bat\txt\expression.txt" > "F:\OO\\bat\txt\exp_javayy.txt"
python "F:\OO\bat\bat.py" yy
pause