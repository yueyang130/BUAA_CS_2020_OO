import os
from xeger import  Xeger
import argparse

def generateExp(reg) :
    x = Xeger(6)
    return x.xeger(reg)

def main(reg, num_exp, path) :
    with open(path, "r") as f:
        exps = f.readlines()

    with open(path, "w") as f :
        for exp in exps :
            #if len(exp)  and len(exp) <= 60 :
            if len(exp) <= 60 :
                f.write(exp)
        #for i in range(num_exp) :
        #   f.write(generateExp(reg) + "\n")
        i = 0
        while i < num_exp :
            exp = generateExp(reg)
            if len(exp) <= 60 and len(exp) >= 15:
            #if len(exp) <= 60 :
                f.write(exp + "\n")
                i += 1
            if i+1<=50 and (i+1)%1==0 :
                print(f"generate {i} exp!")

def printOneExp(reg) :
    print(generateExp(reg))

if __name__ == "__main__" :

    #parser = argparse.ArgumentParser()
    #parser.add_argument("id", type=str, )
    #opts = parser.parse_args()
    #id = opts.id
    id = ""

    # int = "[+-]?[1-9]\\d{1,2}"
    # int = "[+-]?[1-9]\\d{1,2}"
    # space = "[ \t]{0,2}"
    # # exp = f"\\*\\*{space}{int}"
    # power = f"x({space}{exp})?"
    # term = f"(({int})|((({int}{space}\\*{space})?{power})|(([+-]{space})?{power})))"
    # reg = f"[+-]?{term}([+-]{term})*"
    signedIndex = "[+]?[123]"
    signedInt = "[+-]?[12]"
    index = "\\*\\*" + signedIndex
    pow = "x(" + index + ")?"
    # triF and expfactor use()
    otherfactor = "((" + signedInt + ")|(" + pow + "))"
    triF = "(sin|cos)\\(" + otherfactor + "\\)(" + index + ")?"
    factor = "((" + pow + ")|(" + triF + "))"
    term = "([+-])?" + factor + "(\\*" + factor + "){0,2}"
    exp = "([+-])?" + term  + "([+-]" + term + "){0,2}"

    expfactor = f"\\({exp}\\)"
    factor = "((" + triF + ")|(" + expfactor + "))"
    triF = "(sin|cos)\\(" + factor + "\\)(" + index + ")?"
    factor = "((" + pow + ")|(" + triF + ")|(" + expfactor + "))"
    term = "([+-])?" + factor + "(\\*" + factor + "){0,2}"
    exp = "([+-])?" + term + "" + "(?:[+-]" + term + "){0,2}"

    expfactor = f"\\({exp}\\)"
    factor = "((" + triF + ")|(" + expfactor + "))"
    triF = "(sin|cos)\\(" + factor + "\\)(" + index + ")?"
    factor = "(" + otherfactor + "|(" + triF + ")|(" + expfactor + "))"
    term = "([+-])?" + factor + "(\\*" + factor + "){0,2}"
    exp = "([+-])?" + term + "" + "(?:[+-]" + term + "){0,2}"

    #x = Xeger(1000)
    #for i in range(10) :A
    #    print("int : " + x.xeger(int))
    #    print("power : " + x.xeger(power))
    #    print("term : " + x.xeger(term))
    #    print("reg : " + x.xeger(reg))

    num_exp = 100
    path = f"F:\OO\\bat\\txt\expression{id}.txt"
    main(exp, num_exp, path)
    #printOneExp(exp)
