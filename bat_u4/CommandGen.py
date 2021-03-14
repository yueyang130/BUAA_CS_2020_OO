# _*_coding:utf-8_*_
# Author      : JacquesdeH
# Create Time : 2020/5/30 9:51
# Project Name: testbench
# File        : CommandGen.py
# --------------------------------------------------
import random
import sys


class CommandGenConfig:
    def __init__(self):
        self.cmdlist = ['CLASS_COUNT', 'CLASS_OPERATION_COUNT', 'CLASS_ATTR_COUNT', 'CLASS_ASSO_COUNT',
                        'CLASS_ASSO_CLASS_LIST', 'CLASS_OPERATION_VISIBILITY', 'CLASS_ATTR_VISIBILITY',
                        'CLASS_TOP_BASE', 'CLASS_IMPLEMENT_INTERFACE_LIST', 'CLASS_INFO_HIDDEN']
        self.max_count = 200
        self.min_count = 2
        self.characters = [chr(ch) for ch in range(ord('a'), ord('z')+1)] + [chr(ch) for ch in range(ord('A'), ord('Z')+1)]
        self.opmodes = ['NON_RETURN', 'RETURN', 'NON_PARAM', 'PARAM']
        self.attrmodes = ['ALL', 'SELF_ONLY']

    def randString(self, len=10):
        return ''.join(random.choices(self.characters, k=len))

    def randOpMode(self):
        return random.choice(self.opmodes)

    def randAttrMode(self):
        return random.choice(self.attrmodes)


class CommandGen:
    def __init__(self, info: list, fp: sys, config=CommandGenConfig()):
        self.config = config
        self.info = info
        self.fp = fp

    def genClassName(self, beta=0.1):
        if random.random() < beta:
            return self.config.randString()
        return random.choice(self.info)[0]

    def genAttrName(self, className: str, alpha=0.3, beta=0.1):
        factor = random.random()
        if factor < alpha:
            return random.choice(random.choice(self.info)[1])
        elif alpha <= factor < alpha+beta:
            return self.config.randString()
        else:
            for info in self.info:
                if info[0] == className:
                    return random.choice(info[1])
            if random.random() < 0.5:
                return random.choice(random.choice(self.info)[1])
            else:
                return self.config.randString()

    def genOpName(self, className: str, alpha=0.3, beta=0.1):
        factor = random.random()
        if factor < alpha:
            return random.choice(random.choice(self.info)[2])
        elif alpha <= factor < alpha + beta:
            return self.config.randString()
        else:
            for info in self.info:
                if info[0] == className:
                    return random.choice(info[2])
            if random.random() < 0.5:
                return random.choice(random.choice(self.info)[2])
            else:
                return self.config.randString()

    def class_count(self):
        self.fp.write('CLASS_COUNT'+'\n')

    def class_operation_count(self):
        className = self.genClassName()
        modes = [self.config.randOpMode() for _ in range(random.randint(0, 6))]
        modes = ' '.join(modes)
        self.fp.write('CLASS_OPERATION_COUNT '+className+' '+modes+'\n')

    def class_attr_count(self):
        className = self.genClassName()
        mode = self.config.randAttrMode()
        self.fp.write('CLASS_ATTR_COUNT ' + className + ' ' + mode + '\n')

    def class_asso_count(self):
        className = self.genClassName()
        self.fp.write('CLASS_ASSO_COUNT ' + className + '\n')

    def class_asso_class_list(self):
        className = self.genClassName()
        self.fp.write('CLASS_ASSO_CLASS_LIST ' + className + '\n')

    def class_operation_visibility(self):
        className = self.genClassName()
        opName = self.genOpName(className)
        self.fp.write('CLASS_OPERATION_VISIBILITY ' + className + ' ' + opName + '\n')

    def class_attr_visibility(self):
        className = self.genClassName()
        attrName = self.genAttrName(className)
        self.fp.write('CLASS_ATTR_VISIBILITY ' + className + ' ' + attrName + '\n')

    def class_top_base(self):
        className = self.genClassName()
        self.fp.write('CLASS_TOP_BASE ' + className + '\n')

    def class_implement_interface_list(self):
        className = self.genClassName()
        self.fp.write('CLASS_IMPLEMENT_INTERFACE_LIST ' + className + '\n')

    def class_info_hidden(self):
        className = self.genClassName()
        self.fp.write('CLASS_INFO_HIDDEN ' + className + '\n')

    def gen(self):
        count = random.randint(self.config.min_count, self.config.max_count)
        for _ in range(count):
            cmd = random.choice(self.config.cmdlist)
            if cmd == 'CLASS_COUNT':
                self.class_count()
            elif cmd == 'CLASS_OPERATION_COUNT':
                self.class_operation_count()
            elif cmd == 'CLASS_ATTR_COUNT':
                self.class_attr_count()
            elif cmd == 'CLASS_ASSO_COUNT':
                self.class_asso_count()
            elif cmd == 'CLASS_ASSO_CLASS_LIST':
                self.class_asso_class_list()
            elif cmd == 'CLASS_OPERATION_VISIBILITY':
                self.class_operation_visibility()
            elif cmd == 'CLASS_ATTR_VISIBILITY':
                self.class_attr_visibility()
            elif cmd == 'CLASS_TOP_BASE':
                self.class_top_base()
            elif cmd == 'CLASS_IMPLEMENT_INTERFACE_LIST':
                self.class_implement_interface_list()
            elif cmd == 'CLASS_INFO_HIDDEN':
                self.class_info_hidden()
            else:
                print('Panic!', file=sys.stderr)


if __name__ == '__main__':
    commandGen = CommandGen([('class1', ['attr1'], ['op1'])], fp=sys.stdout)
    commandGen.gen()
