# _*_coding:utf-8_*_
# Author      : Yue Yang
# Create Time : 2020/5/30 8:21
# Project Name: oo u4_1 test machine
# File        : dataMaker.py
# --------------------------------------------------

import random

from CommandGen import CommandGen
import sys

class DataMaker:
    def __init__(self):
        self.visibilityList = ['public','private','protected','package']
        self.typeList = ['int', 'float', 'bool','Object']
        self.alphabet = 'abcdefghijklmnopqrstuvwxyz12345676890'


    def setParam(self, probs) :
        self.idList = []
        self.idCLassDict = {}
        self.idInterfDict = {}

        self.CLASS_NUM = 10 #random.randint(12,21)
        self.INTERF_NUM = 10 #random.randint(10,20)
        self.ATTR_NUM_PER_CLASS = 3 #random.randint(2,5)
        self.OP_NUM_PER_CLASS = 3 #random.randint(2,5)
        self.PARAM_IN_MAX = 2
        self.PAEAM_OUT_MAX = 1

        self.PROB_R001 = probs[0]#0.0001
        self.PROB_R002_CLASS = probs[1] #1/10 #类的循环继承
        self.PROB_R002_INTERF = probs[2] #2/10 #接口的循环继承
        self.PROB_R003 = probs[3] #类或接口的重复继承
        self.PROB_R004 = probs[4] #类重复实现接口
        self.PROB_R005 = probs[5] #类图元素名不为null
        self.PROB_R006 = probs[6] #接口的attr为public

        self.CLASS_GENERALIZAITON_NUM = int(self.CLASS_NUM*3//4 * self.PROB_R002_CLASS)
        self.INTERF_GENERALIZATION_NUM = int((self.INTERF_NUM * self.INTERF_NUM//5) *self.PROB_R002_INTERF)
        self.REALIZATION_NUM = int((self.CLASS_NUM * self.INTERF_NUM) * self.PROB_R004)
        self.ASSOCIATION_NUM = self.CLASS_NUM + self.INTERF_NUM
        self.ELEM_NUM_IN_CLASSDIAGRAM = (self.CLASS_NUM + self.INTERF_NUM) *  (1 + self.ATTR_NUM_PER_CLASS ) \
                        + self.CLASS_NUM * self.OP_NUM_PER_CLASS * (1 + int((self.PAEAM_OUT_MAX+self.PARAM_IN_MAX)/2))

        self.classInfo = {} # classid : (classname, attrlist, oplist)

        for j in range(self.CLASS_NUM) :
            _id = self.makeId()
            self.idCLassDict[_id] = self.getOneElem([f'class{x}' for x in range(0, self.CLASS_NUM)])
        for j in range(self.INTERF_NUM) :
            _id = self.makeId()
            self.idInterfDict[_id] = self.getOneElem([f'interf{x}' for x in range(0, self.INTERF_NUM)])

    def randomSetParam(self, i = None):
        if i == None:
            i = random.randint(0,6)
        if i==0:
            self.setParam([1,0,0,0,0,0,0])
        elif i == 1:
            self.setParam([1/1000,1,0,0,0,0,0])
        elif i == 2:
            self.setParam([1/1000,0,1/2,0,0,0,0])
        elif i == 3:
            self.setParam([1/1000,1/4,1/6,0,0,0,0])
        elif i == 4:
            self.setParam([1/1000,1/8,1/8,0,1/2,0,0])
        elif i == 5:
            self.setParam([1/1000,0,0,0,0,1,0])
        elif i == 6:
            self.setParam([1/1000,0,0,0,0,0,1])

    @staticmethod
    def getOneElem(ls):
        index = random.randint(0, len(ls)-1)
        return ls[index]
    
    @staticmethod
    def getName(name, prob):
        return '\"' + name + '\"' if random.random() > prob else 'null'


    def makeId(self):
        Id = ''.join(random.sample(self.alphabet, 8))
        while Id in self.idList :
            Id = ''.join(random.sample(self.alphabet, 8))
        self.idList.append(Id)
        return Id



    def makeGeneralizaitonClassTuple(self):
        generalizaitonClassDict = []
        sons = []
        classList = list(self.idCLassDict.keys())   # id list
        for i in range(self.CLASS_GENERALIZAITON_NUM):
            sonClass = self.getOneElem(classList)
            while sonClass in sons:
                sonClass = self.getOneElem(classList)
            sons.append(sonClass)
            fatherClass = self.getOneElem(classList)
            generalizaitonClassDict.append((sonClass,fatherClass))
        return generalizaitonClassDict


    def makeGeneralizationInterfTuple(self):
        #接口允许多继承
         # sonInterfs = []
         interfList = list(self.idInterfDict.keys())
         generalizaitonInterfTuple = []
         for i in range(self.INTERF_GENERALIZATION_NUM) :
            son = self.getOneElem(interfList)
            # sonInterfs.append(son)
            father = self.getOneElem(interfList)
            # while father in sonInterfs:
            #     father = getOneElem(interfList)
            generalizaitonInterfTuple.append((son, father))
         return generalizaitonInterfTuple


    def makeRealizationTuple(self):
        realizationTuple = []
        classList = list(self.idCLassDict.keys())
        interfList = list(self.idInterfDict.keys())
        for i in range(self.REALIZATION_NUM):
            cla = self.getOneElem(classList)
            interf = self.getOneElem(interfList)
            realizationTuple.append((cla, interf))
        return realizationTuple

    def makeassocTuple(self):
        assocTuple = []
        assocItems = list(self.idCLassDict.keys()) + list(self.idInterfDict.keys())
        for i in range(self.ASSOCIATION_NUM):
            source = self.getOneElem(assocItems)
            target = self.getOneElem(assocItems)
            assocTuple.append((source, target))
        return assocTuple


    def makeModel(self):
        model = []

        generalizaitonClassTuple = self.makeGeneralizaitonClassTuple()
        generalizaitonInterfTuple = self.makeGeneralizationInterfTuple()
        realizationTuple = self.makeRealizationTuple()
        assocTuple      = self.makeassocTuple()
        #opParamlistDict = {}
        #UMLClass
        for classid, name in self.idCLassDict.items():
            name = self.getName(name, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)
            classModel = f'''{{"_parent":"","visibility":"{self.getOneElem(self.visibilityList)}","name":{name},"_type" \
    :"UMLClass","_id":"{classid}"}}'''
            self.classInfo[classid] = (name,[],[])
            model.append(classModel)
        # UMLInterf
        for interfId, name in self.idInterfDict.items():
            name = self.getName(name, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)
            interfModel = f'''{{"_parent":"","visibility":"{self.getOneElem(self.visibilityList)}","name":{name},"_type" \
    :"UMLInterface","_id":"{interfId}"}}'''
            model.append(interfModel)
        #UMLOp
        opIdList = []
        opList = [f'op{x}' for x in range(1,2*self.OP_NUM_PER_CLASS)]
        for clssId in self.idCLassDict.keys():
            for i in range(self.OP_NUM_PER_CLASS):
                opId = self.makeId()
                opIdList.append(opId)
                opName = self.getOneElem(opList)
                opName = self.getName(opName, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)

                opModel = f'''{{"_parent":"{clssId}","visibility":"{self.getOneElem(self.visibilityList)}","name":{opName} \
    ,"_type":"UMLOperation","_id":"{opId}"}}'''
                self.classInfo[clssId][2].append(opName)
                model.append(opModel)

        #Class UMLAttr
        #attrList = [f'attr{getOneElem([x for x in range(1,5*self.ATTR_NUM_PER_CLASS)])}' for i in range(self.ATTR_NUM_PER_CLASS)]
        attrNameList = [x for x in range(1, int(self.ATTR_NUM_PER_CLASS * self.CLASS_NUM / (self.PROB_R001 / 3)))]
        for classid in list(self.idCLassDict.keys()):
            # 同一个类中，属性不能重复
            #for attrName in random.sample(attrList, 5):
            for i in range(self.ATTR_NUM_PER_CLASS):
                attrId = self.makeId()
                attrName = f'attr{self.getOneElem(attrNameList)}'
                attrName = self.getName(attrName, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)
                attrModel = f'''{{"_parent":"{classid}","visibility":"{self.getOneElem(self.visibilityList)}","name": \
    {attrName},"_type":"UMLAttribute","_id":"{attrId}","type":"{self.getOneElem(self.typeList)}"}}'''
                self.classInfo[classid][1].append(attrName)
                model.append(attrModel)

        #Interface Attr
        for classid in list(self.idInterfDict.keys()) :
            # 同一个类中，属性不能重复
            # for attrName in random.sample(attrList, 5):
            for i in range(self.ATTR_NUM_PER_CLASS) :
                attrId = self.makeId()
                attrName = f'attr{self.getOneElem(attrNameList)}'
                attrName = self.getName(attrName, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)
                visibility = self.visibilityList[0] if random.random() > self.PROB_R006/self.ATTR_NUM_PER_CLASS else self.getOneElem(self.visibilityList[1 :])
                attrModel = f'''{{"_parent":"{classid}","visibility":"{visibility}","name": \
    {attrName},"_type":"UMLAttribute","_id":"{attrId}","type":"{self.getOneElem(self.typeList)}"}}'''

                model.append(attrModel)

        #UMLAssocEnd and UMLassoc
        for classid1, classid2 in assocTuple:
            endid1 = self.makeId()
            endid2 = self.makeId()
            associd = self.makeId()
            endName1 = f'attr{self.getOneElem(attrNameList)}'
            endName2 = f'attr{self.getOneElem(attrNameList)}'
            endName1 = self.getName(endName1, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)
            endName2 = self.getName(endName2, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)

            assocEnd1 = f'''{{"reference":"{classid1}","multiplicity":"","_parent":"{associd}", \
    "visibility":"{self.getOneElem(self.visibilityList)}","name":{endName1},"_type":"UMLAssociationEnd" \
    ,"aggregation":"none","_id":"{endid1}"}}'''
            assocEnd2 = f'''{{"reference":"{classid2}","multiplicity":"","_parent":"{associd}", \
    "visibility":"{self.getOneElem(self.visibilityList)}","name":{endName2},"_type":"UMLAssociationEnd" \
    ,"aggregation":"none","_id":"{endid2}"}}'''
            assoc = f'''{{"_parent":"","name":"","_type": \
    "UMLAssociation","end2":"{endid2}","end1":"{endid1}","_id":"{associd}"}}'''
            model.append(assocEnd1)
            model.append(assocEnd2)
            model.append(assoc)
        #UMLGeneral
        generaitonDict = generalizaitonInterfTuple + generalizaitonClassTuple
        for son, father in generaitonDict:
            Id = self.makeId()
            generModel = f'''{{"_parent":"","name":null,"_type":"UMLGeneralization", \
    "_id":"{Id}","source":"{son}","target":"{father}"}}'''
            model.append(generModel)

        #UMLRealizaiton
        for cla, interf in realizationTuple:
            realizModel = f'''{{"_parent":"","name":null,"_type":"UMLInterfaceRealization" \
    ,"_id":"{self.makeId()}","source":"{cla}","target":\
    "{interf}"}}'''
            model.append(realizModel)

        #UMLParam
        # paramList = [f'param{x}' for x in range(1,5)]
        for opId in opIdList:
            inNum = random.randint(0, self.PARAM_IN_MAX)
            outNum = random.randint(0, self.PAEAM_OUT_MAX)
            for i in range(inNum):
                name = f'param{i}'
                name = self.getName(name, self.PROB_R005 / self.ELEM_NUM_IN_CLASSDIAGRAM)
                paramModel = f'''{{"_parent":"{opId}","name":{name},"_type":"UMLParameter","_id":"{self.makeId()}", \
    "type":"{self.getOneElem(self.typeList)}","direction":"in"}}'''
                model.append(paramModel)
            for i in range(outNum):
                paramModel = f'''{{"_parent":"{opId}","name":"returnValue{i}","_type":"UMLParameter","_id":"{self.makeId()}", \
    "type":"{self.getOneElem(self.typeList)}","direction":"return"}}'''
                model.append(paramModel)
        #乱序输出
        random.shuffle(model)
        for i in range(len(model)):
            model[i] += '\n'
        return model

    def getNewData(self, f):
        model = self.makeModel()
        #ClassInfoList = list(self.classInfo.values())

        #writelines不自动换行
        for s in model:
            f.write(s)
        model.append('END_OF_MODEL\n')
        f.write('END_OF_MODEL\n')
        f.flush()
        return model
        #commandGen = CommandGen(classInfo, fp=f)
        #commandGen.gen()

if __name__ == '__main__' :
    #with open(r'F:\OO\bat_u4\handTest\data.txt', 'w') as f :
    pass

