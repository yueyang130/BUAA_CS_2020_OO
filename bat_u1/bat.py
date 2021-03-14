from sympy import  sympify, diff, symbols
import xeger
import os
from random import random
import argparse
import time
from math import isnan

def compare(exp_py, dexp_java, f) :


    if dexp_java == "WRONG FORMAT!\n" :
        f.write(f"origin expression : {exp_py}\n")
        f.write(f"error java diff expression : {dexp_java}\n")
        print("There is some ERROR!\n")
        return
    try:
        for i in [-2,2] :  # twice
            x0 = i
            #print(x0,'\n')
            x = symbols('x')
            f2 = sympify(exp_py)
            f1 = sympify(dexp_java)
            f2 = diff(f2, x)
            a = f1.evalf(subs={x : x0})
            b = f2.evalf(subs={x : x0})
            d1 = abs(a - b)/max(1, a)
            d2 = abs(a - b)/max(1, a)
            if (not isnan(d1) and not isnan(d2)):
                if d1 >= 10**(-8) or d2 >= 10**(-8) :
                    f.write(f"origin expression : {exp_py}\n")
                    f.write(f"error java diff expression : {dexp_java}\n")
                    print("There is some ERROR!\n")
    except TypeError  as e :
        f.write(f"origin expression : {exp_py}\n")
        f.write(f"error java diff expression : {dexp_java}\n")
        print("There is some ERROR!\n")



def main(path_py, path_java,  f_error, id) :
    #exps_py = []
    #exps_java = []
    with open(path_py, "r") as f :
        exps_py = f.readlines()
    with open(path_java, "r") as f :
        exps_java = f.readlines()
    if len(exps_py) != len(exps_java) :
        print(f"Error! python generate {len(exps_py)} expressions, \
              but java generates {len(exps_java)} diff expressions")


    for i in range(min(len(exps_py), len(exps_java))) :
        if (i+1) % 1000 == 0 or ((i+1)%10==0 and i+1 <= 100):
            print(f"the {id}'s code: num of testing expression : {i+1}")
        #t1  =time.time()
        print(i+1)
        compare(exps_py[i], exps_java[i], f_error)
        #t2 = time.time()
        #print("compare one expression needs %f seconds!"%(t2 - t1))


if __name__ == "__main__" :
        parser = argparse.ArgumentParser()
        parser.add_argument("id", type=str)
        opts = parser.parse_args()
        id = opts.id

        # 此文件运行需要两种输入，因此重定向和管道的方法不太好用
        # 生成多个exp文件也是为了方便对拍
        path_py = f"F:\OO\\bat\\txt\expression.txt"
        path_java = f"F:\OO\\bat\\txt\exp_java{id}.txt"
        path_error = f"F:\OO\\bat\\txt\ErrorInfor{id}.txt"
        with open(path_error, "w") as f :
            main(path_py, path_java,  f, id)
        print(f"success compare {id}th file!")
