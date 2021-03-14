import random

AP_MAX_NUM = 1115
alphabet = 'abcdefghijklmnopqrstuvwxyz12345676890'

cmds = []
cmds.append('ag 0\n')
for i in range(AP_MAX_NUM) :
    newname = random.sample(alphabet, 5)
    newname = ''.join(newname)
    newid = i
    newcharacter = random.randint(0, 6666)
    newage = random.randint(0, 200)
    cmd = f'ap {newid} {newname} {newcharacter} {newage}\n'
    cmds.append(cmd)
    cmds.append(f'atg {i} 0\n')
    cmds.append('qgps 0\n')

path = r"F:\OO\bat_u3\manualData.txt"
with open(path, "w") as f :
    f.writelines(cmds)
