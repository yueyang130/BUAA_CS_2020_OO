import getOutput
import re

def checkOut(instrs_o, out_o, f) :
    instrs = []
    out = []
    for i in range(len(instrs_o)) :
        instrs.append(instrs_o[i].split("]")[1])
    for i in range(len(out_o)) :
        out.append(out_o[i].split("]")[1])


    pinstr = re.compile("(?P<id>\\d*)-FROM-(?P<start>[-]?\\d*)-TO-(?P<end>[-]?\\d*)")
    addElevator = re.compile("(?P<id>.*)-ADD-ELEVATOR-(?P<type>[ABC])")
    id_from = {}
    id_to = {}
    id_elev = {}

    rangeA = [False] * 23
    rangeB = [False] * 23
    rangeC = [False] * 23
    elevid_range = {"A" : rangeA, "B" : rangeB, "C" : rangeC}
    for i in range(23) :  # 0-22
        rangeA[i] = -3 <= I2F(i) <= -1 or I2F(i) == 1 or I2F(i) >= 15 or I2F(i) <= 20
        rangeB[i] = I2F(i) != 0 and -2 <= I2F(i) <= 2 or 4 <= I2F(i) <= 15
        rangeC[i] = 1 <= I2F(i) <= 15 and I2F(i) % 2 == 1

    for instr in instrs :
        m = pinstr.search(instr)
        if m is not None :
            id = int(m.group("id"))
            start = int(m.group("start"))
            end = int(m.group("end"))
            id_from[id] = start
            id_to[id] = end
        else :
            m = addElevator.search(instr)
            elevaotr_id = m.group('id')
            elevator_type = m.group('type')
            if elevator_type == "A" :
                elevid_range[elevaotr_id] = rangeA
            elif elevator_type == "B" :
                elevid_range[elevaotr_id] = rangeB
            else :
                elevid_range[elevaotr_id] = rangeC


    pa = re.compile("ARRIVE-(?P<floor>[-]?\\d*)-(?P<ele>[ABCX\\d]*)")
    po = re.compile("OPEN-(?P<floor>[-]?\\d*)-(?P<ele>[ABCX\\d]*)")
    pc = re.compile("CLOSE-(?P<floor>[-]?\\d*)-(?P<ele>[ABCX\\d]*)")
    pin = re.compile("IN-(?P<id>\\d*)-(?P<floor>[-]?\\d*)-(?P<ele>[ABCX\\d]*)")
    pout = re.compile("OUT-(?P<id>\\d*)-(?P<floor>[-]?\\d*)-(?P<ele>[ABCX\\d]*)")

    # check floor in range
    # check all people get in and get off
    # check  people get in and get off in the same elevator
    # check all elevators onlt stop in their own range
    for o in out :
        if o[0] == "A" :
            m = pa.search(o)
            floor = int(m.group("floor"))
            if not (-3 <= floor <= -1 or 1 <= floor <= 20) :
                f.write(f"floor is {floor}, out of range")
                raise Exception
        elif o[0:2] == "OP" :
            m = po.search(o)
            elev = m.group("ele")
            floor = int(m.group("floor"))
            if not elevid_range[elev][F2I(floor)] :
                 f.write(f"elevator type is {elev}, but stop at {floor}th floor\n")
                 raise Exception
        elif o[0] == "C" :
            pass
        elif o[0] == "I" :
            m = pin.search(o)
            id = int(m.group("id"))
            floor = int(m.group("floor"))
            elev = m.group("ele")

            # ielev = ord(elev) - ord("A")
            # if ielev < 0 or ielev >= e_num :
            #     f.write(f"elevator id is {elev}, but e_num is {e_num}\n")
            #     raise Exception
            if id_from[id] != floor :
                f.write(f"{id} : from is {id_from[id]}, but in is {floor}\n")
                raise Exception
            else :
                id_from.pop(id)
                id_elev[id] = elev

        elif o[0:2] == "OU" :
            m = pout.search(o)
            id = int(m.group("id"))
            floor = int(m.group("floor"))
            elev = m.group("ele")
            # ielev = ord(elev) - ord("A")
            # if ielev < 0 or ielev >= e_num :
            #     f.write(f"elevator id is {elev}, but e_num is {e_num}\n")
            #     raise Exception
            if id_to[id] != floor :
                # f.write(f"{id} : to is {id_to[id]}, but in is {floor}\n")
                # raise Exception
                id_from[id] = floor

            else :
                id_to.pop(id)
            if id_elev[id] != elev :
                f.write(f"{id} get on elevator {id_elev[id]}, but get off elevator {elev}\n")
                raise Exception

        else:
            f.write(f"illegal instruction {o}\n")
            raise Exception

    # check everyone get on and get off in the correct floor
    if len(id_to) != 0 or len(id_from) != 0 :
        f.write("Not all people get in or get off !\n")
        raise Exception

def I2F(index) :
        assert 0 <= index <= 22
        if index <= 2 :
            return index - 3
        return index - 2

def F2I(floor) :
    assert floor >= -3 and floor <= -1 or floor >= 1 and floor <= 20
    if floor < 0:
        return floor + 3
    return floor + 2


def checkPerform() :
    # wait finishing
    pass

def main() :

    path = r"F:\OO\bat_u2\yyError.txt"

    for i in range(1000) :
        with open(path, "a") as f :
            try :
                instrs, out = getOutput.main()
                checkOut(instrs, out, f)
                #checkPerform(instrs, out, f)
            except Exception :
                print("***********ERROR!!!!!!!!**************")
                f.write("****instrs****\n")
                f.writelines(instrs)
                f.write("****out****\n")
                f.writelines(out)
                f.write("\n"*3)
                raise Exception
            else:
                if i+1 < 10 or (i + 1)%10 == 0 :
                    print(f"successfully check {i+1}th data")


    #checkPerform(instrs, out)

if __name__ == "__main__" :
    main()