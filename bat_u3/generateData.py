import random

INSTR_MAX_NUM = 150
AP_MAX_NUM = 40
AG_MAX_NUM = 5


def generateData() :
    cmds = []
    person_ids = []
    group_ids = []
    alphabet = 'abcdefghijklmnopqrstuvwxyz12345676890'
    first = True
    second = True

    for i in range(INSTR_MAX_NUM) :
        a = random.random()
        if a < AP_MAX_NUM / INSTR_MAX_NUM or first:
            first = False

            newname = random.sample(alphabet, 5)
            newname = ''.join(newname)
            newid = random.randint(-AP_MAX_NUM, AP_MAX_NUM)
            while newid in person_ids :
                 newid = random.randint(-3 * AP_MAX_NUM, 3 * AP_MAX_NUM)
            person_ids.append(newid)
            newcharacter = random.randint(0, 6666)
            newage = random.randint(0, 2000)
            cmd = f'ap {newid} {newname} {newcharacter} {newage}'

        elif a < (AP_MAX_NUM + AG_MAX_NUM) / INSTR_MAX_NUM or second :
            second = False

            newid = random.randint(-AG_MAX_NUM, AG_MAX_NUM)
            while newid in group_ids :
                newid = random.randint(- 3 *AG_MAX_NUM, 3*AG_MAX_NUM)
            group_ids.append(newid)
            cmd = f'ag {newid}'

        elif random.random() < 0.3:

            b = random.random()
            if b < 0.3:
                id1_index = random.randint(0, len(person_ids)-1)
                id2_index = random.randint(0, len(person_ids)-1)
                id1 = person_ids[id1_index]
                id2 = person_ids[id2_index]
                value = random.randint(0,1000)
                cmd = f'ar {id1} {id2} {value}'

            elif b < 0.6:
                p_id_index = random.randint(0, len(person_ids) - 1)
                g_id_index = random.randint(0, len(group_ids) - 1)
                p_id = person_ids[p_id_index]
                g_id = group_ids[g_id_index]
                if random.random() < 0.3:
                    cmd = f'atg {p_id} {g_id}'
                else:
                    cmd = f'dfg {p_id} {g_id}'
            else :
                group_id_index = random.randint(0, len(group_ids)-1)
                group_id = group_ids[group_id_index]
                if b < 0.55:
                    cmd = f'qgrs {group_id}'
                elif b < 0.6 :
                    cmd = f'qgvs {group_id}'
                elif b < 0.65:
                    cmd = f'qgcs {group_id}'
                elif b < 0.7:  # is circle
                    id1_index = random.randint(0, len(person_ids) - 1)
                    id2_index = random.randint(0, len(person_ids) - 1)
                    id1 = person_ids[id1_index]
                    id2 = person_ids[id2_index]
                    cmd = f'qci {id1} {id2}'
                elif b < 0.75:
                    cmd = f'qgam {group_id}'
                elif b < 0.8:
                    cmd = f'qgav {group_id}'
                elif b < 0.85:
                    cmd = f'qasu {random.randint(0,2000)} {random.randint(0,2000)}'
                elif b < 0.9:
                    id1_index = random.randint(0, len(person_ids) - 1)
                    id2_index = random.randint(0, len(person_ids) - 1)
                    id1 = person_ids[id1_index]
                    id2 = person_ids[id2_index]
                    cmd = f'bf {id1} {id2} {random.randint(-100,100)}'
                elif b < 0.95:
                    id1_index = random.randint(0, len(person_ids) - 1)
                    id1 = person_ids[id1_index]
                    cmd = f'qm {id1}'
                elif b < 0.98 :
                    cmd = f'qgs'
                else:
                    cmd = f'qgps {group_id}'
        else:
            id1_index = random.randint(0, len(person_ids) - 1)
            id2_index = random.randint(0, len(person_ids) - 1)
            id1 = person_ids[id1_index]
            id2 = person_ids[id2_index]
            p = random.random()
            if p < 0.33:
                cmd = f'qmp {id1} {id2}'
            elif p < 0.66:
                cmd = f'qsl {id2} {id1}'
            else:
                cmd = f'qbs'


        cmd += '\n'
        cmds.append(cmd)

    path = r"F:\OO\bat_u3\temp.txt"
    with open(path, "w") as f :
        f.writelines(cmds)

    return cmds

if __name__ == '__main__':
    cmds = generateData()
    for i in range(100) :
        print(cmds[i], end='')




