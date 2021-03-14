from random import randint, random

def generateData() :
    ids = []
    instrs = []
    # e_num = randint(1, 5)
    # instrs.append(f"[0.0]{e_num}\n")
    limit_instr = 30
    limit_elevator = 0
    if random() < 0.8 :  # 80% 短数据
        cnt = randint(1, 10)
    else :
        cnt = randint(limit_instr//2, limit_instr)
    time = round(random()  + 1, 1)

    for i in range(cnt) :
        make = False
        if random() <= 0.85 :
            make = True
            # make id
            while True :
                id = randint(1, 100)
                if id not in ids :
                    ids.append(id)
                    break
            # make start floor
            while True :
                start = randint(-3, 20)
                if start != 0: break
            # make end floor
            while True:
                end = randint(-3, 20)
                if end != start and end != 0 : break
            instr = f"[{time}]{id}-FROM-{start}-TO-{end}\n"
        else :
            if limit_elevator < 3:
                make = True
                elevator_id = "X" + str(limit_elevator + 1)
                limit_elevator += 1
                if random() < 0.33 :
                    type = "A"
                elif random() < 0.66:
                    type = "B"
                else:
                    type = "C"
                instr = f"[{time}]{elevator_id}-ADD-ELEVATOR-{type}\n"
        if make:
            instrs.append(instr)
            # make interval
            a = random()
            if a < 0.75 :  # [0.2, 2)  75%
                interval = random() * 1.8 + 0.2
            elif a < 0.95 :   # 0  20%
                interval = 0
            else :           # [2, 20) 5%
                interval = random() * 19 + 1
            interval = round(interval, 1)
            time += interval
            time = round(time, 1)
            if (time > 40) :
                break
    return instrs

if __name__ == "__main__" :
    instrs = generateData()
    for i in instrs :
        print(i, end = '')