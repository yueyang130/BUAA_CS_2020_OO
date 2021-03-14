from generateData import generateData
import subprocess
import re
import time

PRINT = True
makeData = True

def getOutput(instrs) :

    # cmd1 = r'''F: && cd "F:\OO\hw\u2_1\src"  && javac *.java -d "F:\OO\bat_u2\class\currency\yy" -encoding UTF-8'''
    # subprocess.Popen(cmd1, shell = True)
    # cmd2 = r'''java "F:\OO\bat_u2\class\currency\yy\Main" '''
    cmd2 = r''' F: && cd "F:\OO\hw\u2_3\out\production\u2_3"  ''' \
           + "&&" +   \
    r'''java -cp .;elevator-input.jar;timable-output.jar MainClass '''
    javaProc = subprocess.Popen(cmd2, shell = True, stdin=subprocess.PIPE, stdout=subprocess.PIPE)

    #start = time.time()
    p = "\\[(?P<time>.*)\\](?P<instr>.*)"
    p = re.compile(p)
    pretime = 0
    # # input e_num
    # str = instrs[0]
    # if PRINT : print(str, end="")
    # str = str[str.find("]") + 1:]
    # javaProc.stdin.write(str.encode())
    # javaProc.stdin.flush()
    # input instr
    # for str in instrs[1:] :
    for str in instrs :
        m = p.search(str)
        t = float(m.group("time"))
        instr = m.group("instr") + "\n"
        time.sleep(t - pretime)
        pretime = t
        end = time.time()
        if PRINT : print(str, end = "")
        javaProc.stdin.write(instr.encode())
        javaProc.stdin.flush()
    if PRINT : print("\nfinish inputing data\n")
    javaProc.stdin.close()
    out = javaProc.stdout.readlines()
    for i in  range(len(out)) :
        out[i] = out[i].decode()
        out[i] = out[i][:-1]
        if PRINT : print(out[i])
    return out

def main() :

    if makeData :
        instrs = generateData()
    else :
        with open(r"F:\OO\bat_u2\data.txt", "r") as f:
            instrs = f.readlines()
    out = getOutput(instrs)
    return instrs, out

if __name__ == "__main__" :
    main()
