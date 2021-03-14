from generateData import generateData
import subprocess

newData = True
PRINT = False

def runJava(cmd, instrs) :
    datapath = r"F:\OO\bat_u3\temp.txt"
    with open(datapath, 'r') as f:
        javaProc = subprocess.Popen(cmd, shell=True, bufsize=500000,
        stdin=f.fileno(), stdout=subprocess.PIPE)

        if PRINT : print("\nfinish inputing data\n")
        out = javaProc.stdout.readlines()
        for i in range(len(out)) :
            out[i] = out[i].decode()
            out[i] = out[i][:-1]
            if PRINT : print(out[i])
    return instrs, out

def getIO(cmd) :
    if newData :
        instrs = generateData()
    else:
        with open(r"F:\OO\bat_u3\temp.txt", "r") as f :
            instrs = f.readlines()
    return runJava(instrs, cmd)

if __name__ == '__main__' :
    cmds = generateData()
    runJava(cmds)