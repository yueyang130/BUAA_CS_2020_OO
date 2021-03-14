#!/usr/bin/env python3

with open("outputA.txt","r") as fA :
    A = fA.readlines()

with open("outputB.txt","r") as fB :
    B = fB.readlines()

same = True
if (len(A)!=len(B)) :
    same = False
    print("Error - Length Not Equal")
    print("     A length: %d" % (len(A)))
    print("     B length: %d" % (len(B)))
    print("Please save the file 'input.txt', 'outputA.txt', 'outputB.txt' and press any button to continue")
    y = input()

for i in range(len(A)) :
    if (A[i] != B[i]) :
        same = False
        print("Error - Difference in line %d" % (i+1))
        print("     A line: %s" % (A[i]))
        print("     B line: %s" % (B[i]))
        print("Please save the file 'input.txt', 'outputA.txt', 'outputB.txt' and press any button to continue")
        y = input()
        break

if (same) :
    print("Success")
    print("\n")
