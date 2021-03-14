from getOutput import runJava
from generateData import generateData

newData = True

def main():
    path = r"F:\OO\bat_u3\yyError.txt"
    cmd1 = r''' F: && cd "F:\OO\bat_u3\hzx"  ''' \
           + "&&" + \
           r'''java -jar hzx.jar'''

    cmd2 = r''' F: && cd "F:\OO\hw\u3_3\out\production\u3_3"  ''' \
           + "&&" + \
           r'''java hwthree.MainClass'''



    for i in range(1000) :
        if newData :
            instrs = generateData()
        else :
            with open(r"F:\OO\bat_u3\temp.txt", "r") as f :
                instrs = f.readlines()
        _, out1 = runJava(cmd1, instrs)
        _, out2 = runJava(cmd2, instrs)
        #assert len(instrs) == len(out), f"former is {len(instrs)}, latter is {len(out)}"
        assert len(out1) == len(out2)
        for j in range(len(out1)):
            if out1[j] != out2[j]:
                print(f"---Error in case{i} line{j+1}!---\n")
                path1 = f"F:\OO\\bat_u3\case\hzx_out{i}.txt"
                path2 = f"F:\OO\\bat_u3\case\yy_out{i}.txt"
                path3 = f"F:\OO\\bat_u3\case\in{i}.txt"
                fp1 = open(path1, "w")
                fp2 = open(path2, "w")
                fp3 = open(path3, "w")
                fp1.writelines(out1)
                fp2.writelines(out2)
                fp3.writelines(instrs)
                raise Exception
        if i + 1 < 10 or (i + 1) % 10 == 0 :
            print(f"successfully check {i + 1}th data")





if __name__ == '__main__' :
    main()