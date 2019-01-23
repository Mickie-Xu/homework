from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor
import numpy as np
 
from sklearn.datasets import load_iris
iris=load_iris()
#print iris#iris的４个属性是：萼片宽度　萼片长度　花瓣宽度　花瓣长度　标签是花的种类：setosa versicolour virginica
print(iris['target'].shape)
rf=RandomForestRegressor()#这里使用了默认的参数设置
rf.fit(iris.data[:150],iris.target[:150])#进行模型的训练
#  
#随机挑选两个预测不相同的样本
instance=iris.data[[1,150]]
dt_predict = dtc.predict(instance)
dtc.score(instance[0], instance[1])