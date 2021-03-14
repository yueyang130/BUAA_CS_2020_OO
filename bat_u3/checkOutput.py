from getOutput import getIO

class person :
    def __init__(self, id, name, character, age):
        self.id = id
        self.name = name
        self.character = character
        self.age = age
        #self.acquaintance = []
        self.value = {}   # id-value

    def addAcquaitance(self, person_id, value):
        # self.acquaintance.append(person_id)
        # self.value.append(value)
        self.value[person_id] = value

    def isRelated(self, p):
        if self.id == p.id:
            return True
        if p.id in self.value.keys():
            return True
        return False

    def getValue(self, id):
        if self.id == id:
            return 0
        return self.value[id]

    def getAcquaintance(self):
        return self.value.keys()

def check(cmds, out, f):
    people = {}
    groups = {}

    for i in range(len(cmds)):
        out[i] = out[i][:-1]
        paras = cmds[i].split(' ')

        if cmds[i].startswith('ap'):
            # paras = cmds[i].split(' ')
            paras[1] = int(paras[1])
            paras[3] = int(paras[3])
            paras[4] = int(paras[4])
            assert paras[1] not in people.keys()
            p = person(paras[1], paras[2], paras[3], paras[4])
            people[p.id] = p
            assert out[i] == 'Ok'

        elif cmds[i].startswith('ar'):
            # paras = cmds[i].split(' ')
            p_id1 = int(paras[1])
            p_id2 = int(paras[2])
            value = int(paras[3])
            assert p_id1 in people.keys() and p_id2 in people.keys()
            if p_id2 != p_id1 :
                p1 = people[p_id1]
                p2 = people[p_id2]
                if not p1.isRelated(p2):
                    p1.addAcquaitance(p_id2, value)
                    p2.addAcquaitance(p_id1, value)
                    assert out[i] == 'Ok'
                else :
                    assert out[i] == 'er'
            else :
                assert out[i] == 'Ok'


        elif cmds[i].startswith('ag'):
            # paras = cmds[i].split(' ')
            g_id = int(paras[1])
            assert g_id not in groups.keys()
            groups[g_id] = []
            assert out[i] == 'Ok'

        elif cmds[i].startswith('atg') :
            # paras = cmds[i].split(' ')
            p_id = int(paras[1])
            g_id = int(paras[2])
            if p_id in groups[g_id] :
                assert out[i] == 'epi'
            else:
                try:
                    groups[g_id].append(p_id)
                except KeyError:
                    print(f'g_id is {g_id}')
                    print(groups.keys())
                    raise Exception
                assert out[i] == 'Ok'

        elif cmds[i].startswith('qci') :
            p1_id = int(paras[1])
            p2_id = int(paras[2])
            assert p1_id in people.keys() and p2_id in people.keys()
            if p1_id == p2_id :
                assert 1 == int(out[i])
                continue

            queue = []
            visited = {}
            for p_id in people.keys():
                visited[p_id] = False
            queue.append(p1_id)
            visited[p1_id] = True
            iscircle = 0
            while len(queue) > 0:
                curr = queue.pop(0)
                curr = people[curr]
                for p_id in curr.getAcquaintance():
                    if not visited[p_id]:
                        queue.append(p_id)
                        visited[p_id] = True
                        if p_id == p2_id:
                            iscircle = 1
                            break
            assert iscircle == int(out[i])


        elif cmds[i].startswith('qgs') :
            assert int(out[i]) == len(groups)

        elif cmds[i].startswith('qgps') :
            g_id = int(paras[1])
            assert int(out[i]) == len(groups[g_id])

        elif cmds[i].startswith('qgrs') :
            g_id = int(paras[1])
            g = groups[g_id]
            relationSum = 0
            for p1_id in g:
                for p2_id in g:
                    if people[p1_id].isRelated(people[p2_id]):
                        relationSum += 1
            assert relationSum == int(out[i])

        elif cmds[i].startswith('qgvs') :
            g_id = int(paras[1])
            g = groups[g_id]
            valueSum = 0
            for p1_id in g :
                for p2_id in g :
                    if people[p1_id].isRelated(people[p2_id]) :
                        valueSum += people[p1_id].getValue(p2_id)
            assert valueSum == int(out[i])

        elif cmds[i].startswith('qgcs') :
            #TODO
            pass

        elif cmds[i].startswith('qgam') :
            g_id = int(paras[1])
            g = groups[g_id]
            ageSum = 0
            for p_id in g:
                ageSum += people[p_id].age
            if len(g) > 0:
                ageMean = int(ageSum / len(g))
            else :
                ageMean = 0
            assert ageMean == int(out[i])

        elif cmds[i].startswith('qgav') :
            g_id = int(paras[1])
            g = groups[g_id]
            ageSum = 0
            if len(g) > 0 :
                for p_id in g :
                    ageSum += people[p_id].age
                ageMean = int(ageSum / len(g))
                sum = 0
                for p_id in g :
                    sum += (people[p_id].age - ageMean)**2
                var = int(sum / len(g))
            else:
                var = 0
            if var != int(out[i]) :
                f.write(f'i={i}, cmd= {cmds[i]}\n')
                f.write(f'python: var = {var}, java: var = {out[i]}\n')
                raise Exception
        else:
            raise Exception('no match cmd\n')





def main():
    path = r"F:\OO\bat_u3\yyError.txt"

    for i in range(1000) :
        instrs, out = getIO()
        assert len(instrs) == len(out)
        with open(path, "a") as f :
            try :
                instrs, out = getIO()
                check(instrs, out, f)
                # checkPerform(instrs, out, f)
            except Exception :
                print("***********ERROR!!!!!!!!**************")
                f.write("****instrs****\n")
                f.writelines(instrs)
                f.write("****out****\n")
                f.writelines(out)
                f.write("\n" * 3)
                raise Exception
            else :
                if i + 1 < 10 or (i + 1) % 10 == 0 :
                    print(f"successfully check {i + 1}th data")


if __name__ == '__main__' :
    main()