import pandas as pd
from sklearn.svm import SVC
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

iris = pd.read_csv('iris.csv')
x, y, z = iris[0], iris[1], iris[2]
ax = plt.subplot(111, projection='3d')  
ax.scatter(x[:50], y[:50], z[:50], c='y') 
ax.scatter(x[51:100], y[51:100], z[51:100], c='r')
ax.scatter(x[101:150], y[101:150], z[101:150], c='g')

ax.set_zlabel('Z') 
ax.set_ylabel('Y')
ax.set_xlabel('X')
plt.show()

svc = SVC()
svc.fit(iris[0,1,2],iris[4])