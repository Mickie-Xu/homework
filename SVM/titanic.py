import csv
import os
from sklearn import svm

#通过read_csv读取数据
def readData(fileName):
    result = {}
    with open(fileName,'rb') as f:
        rows = csv.reader(f)
        for row in rows:
            if result.has_key('attr_list'):
                for i in range(len(result['attr_list'])):
                    key = result['attr_list'][i]
                    if not result.has_key(key):
                        result[key] = []
                    result[key].append(row[i])
            else:
                result['attr_list'] = row
    return result

#将数据录入csv格式文件
def writeData(fileName, data):
    csvFile = open(fileName, 'w')
    writer = csv.writer(csvFile)
    n = len(data)
    for i in range(n):
        writer.writerow(data[i])
    csvFile.close()

#将数据进行向量化，这里用到hash算法
def convertData(dataList):
    hashTable = {}
    count = 0.0
    for i in range(len(dataList)):
        if not hashTable.has_key(dataList[i]):
            hashTable[dataList[i]] = count
            count += 1
        dataList[i] = hashTable[dataList[i]]


def convertValueData(dataList):
    sumValue = 0.0
    count = 0
    for i in range(len(dataList)):
        if dataList[i] == "":
            continue
        sumValue += float(dataList[i])
        count += 1
        dataList[i] = float(dataList[i])
    avg = sumValue / count
    for i in range(len(dataList)):
        if dataList[i] == "":
            dataList[i] = avg

#标注数据特征
def dataPredeal(data):
    convertValueData(data["Age"])
    convertData(data["Fare"])
    convertData(data["Pclass"])
    convertData(data["Sex"])
    convertData(data["SibSp"])
    convertData(data["Parch"])
    convertData(data["Embarked"])
  
#把数据放入ignores中并返回
def getX(data): 
    x = []
    ignores = {"PassengerId":1, "Survived":1, "Name":1,"Ticket":1, "Cabin":1, "Fare":1, "Embarked":1}    
    for i in range(len(data["PassengerId"])):
        x.append([])
        for j in range(len(data["attr_list"])):
            key = data["attr_list"][j]
            if not ignores.has_key(key):
                x[i].append(data[key][i])
    return x

#同上，这里把标签对应起来
def getLabel(data):
    label = []
    for i in range(len(data["PassengerId"])):
        label.append(int(data["Survived"][i]))
    return label

#随即构造SVC的分类器，命名为SVMCAl
def calResult(x,label, input_x):
    svmcal = svm.SVC(kernel='linear').fit(x, label)
    return svmcal.predict(input_x)

#将数据录入并开始调用分类器开始跑数据
def run():
    dataRoot = '../../kaggledata/titanic/'
    data = readData(dataRoot + 'train.csv')
    test_data = readData(dataRoot + 'test.csv')
    dataPredeal(data)
    dataPredeal(test_data)
    x = getX(data)
    label = getLabel(data)
    input_x = getX(test_data)
    x_result = calResult(x, label,input_x)
    res = [[test_data["PassengerId"][i], x_result[i]] for i in range(len(x_result))]
    res.insert(0, ["PassengerId", "Survived"])
    writeData(dataRoot + 'result.csv', res) 

#相当于main函数，调用方法开始分类
run()