import subprocess
import os
import datamaker
import random

class Comparator:
    def __init__(self, jar_paths, out_dir, jarNames, SAVE_OUT = False):
        self.jar_paths = jar_paths
        self.run_cmds = [f'java -jar "{jar_path}"' for jar_path in jar_paths]
        self.jarNames = jarNames
        self.outdir = out_dir
        self.PRINT_IN = False
        self.PRINT_OUT = False
        self.SAVE_OUT = SAVE_OUT


    def runJava(self, cmd, f) :
        javaProc = subprocess.Popen(cmd, shell=True, bufsize=500000,
            stdin=f.fileno(), stdout=subprocess.PIPE)
        if self.PRINT_IN : print("\nfinish inputing data\n")
        out = javaProc.stdout.readlines()
        for i in range(len(out)) :
            out[i] = out[i].decode()
            #out[i] = out[i][:-1]
            # if PRINT_OUT : print(out[i])
        return out

    def saveIO(self, myInput, outs, i):
        casePath = os.path.join(self.outdir,f'case{i}')
        if not os.path.exists(casePath):
            os.mkdir(casePath)
        outpaths = [os.path.join(casePath, jarName+'.txt') for jarName in self.jarNames]
        with open(os.path.join(casePath,'in.txt'), 'w') as f:
            f.writelines(myInput)
        for i in range(len(outs)):
            with open(outpaths[i], 'w') as f:
                f.writelines(outs[i])

    def getOuts(self, fin):
        outs = []

        for cmd in self.run_cmds :
            fin.seek(0, 0)
            o = self.runJava(cmd, fin)
            outs.append(o)
        return outs

    def camapre(self, fin, i):
        fin.seek(0, 0)
        myInput = fin.readlines()
        outs = self.getOuts(fin)
        # check len
        for j in range(1, len(outs)):
            if len(outs[0]) != len(outs[j]) :
                self.saveIO(myInput, outs, i)
                print(f"---Error in case{i}!---\n")
                print(f"{self.jarNames[0]} has {len(outs[0])} lines, {self.jarNames[j]} has {len(outs[j])} lines.")
                raise Exception

        # check content
        for jarIndex in range(1, len(outs)):
            for j in range(len(outs[0])):
                if outs[0][j] != outs[jarIndex][j]:
                    self.saveIO(myInput, outs, i)
                    print(f"---diff in case{i} line{j+1}!---\n")
                    raise Exception

        if self.SAVE_OUT:
            self.saveIO(myInput, outs, i)

        if self.PRINT_OUT:
            for oIndex in range(len(outs)):
                print(f"\n\n--------{self.jarNames[oIndex]} output----------")
                for x in outs[oIndex]:
                    print(x)
        if i + 1 < 10 or (i + 1) % 10 == 0 :
            print(f"successfully check {i + 1}th data")

def getOutput():
    jar_paths = [
        r"F:\OO\hw\u4_3\out\artifacts\u4_3_jar\u4_3.jar",
        r"F:\OO\bat_u4\jar\hjw.jar",
        #r"F:\OO\bat_u4\jar\cty.jar"
    ]
    out_dir = r"F:\OO\bat_u4\output"
    jarNames = ['yy', 'hjw', 'cty']
    dataPath = r'F:\OO\bat_u4\output\case2\in.txt'
    myComp = Comparator(jar_paths, out_dir, jarNames, SAVE_OUT=True)
    with open(dataPath, 'r') as rf :
        myComp.camapre(rf, 0)


def main():
    jar_paths = [
        r"F:\OO\hw\u4_3\out\artifacts\u4_3_jar\u4_3.jar",
        r"F:\OO\bat_u4\jar\hjw.jar",
        #r"F:\OO\bat_u4\jar\cty.jar",
        r"F:\OO\bat_u4\jar\hzx.jar"

    ]
    out_dir = r"F:\OO\bat_u4\output"
    jarNames = [
        'yy',
        'hjw',
        #'cty',
        'hzx'
        ]
    dataPath = r'F:\OO\bat_u4\data.txt'
    myComp = Comparator(jar_paths, out_dir, jarNames)

    for i in range(1000):
        with open(dataPath, 'w') as wf :
            myDataMaker = datamaker.DataMaker()
            mode = random.randint(0,6)
            #mode = 3
            myDataMaker.randomSetParam(mode)
            inputModel = myDataMaker.getNewData(wf)
        with open(dataPath, 'r') as rf:
            myComp.camapre(rf, i)


if __name__ == '__main__' :
    main()



