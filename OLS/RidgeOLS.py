import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn import linear_model
from mpl_toolkits.mplot3d import Axes3D
 
df = pd.read_csv('http://www-bcf.usc.edu/~gareth/ISL/Advertising.csv', index_col=0)
X = np.asarray(df[['TV', 'radio']])
y = np.asarray(df['sales'])
 
clf = linear_model.RidgeCV(alphas=[i+1 for i in np.arange(200.0)]).fit(X, y)
y_pred = clf.predict(X)
df['sales_pred'] = y_pred
print(df)
print("alpha=%s, 常数=%.2f, 系数=%s" % (clf.alpha_ ,clf.intercept_,clf.coef_))
 
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.scatter(df['TV'], df['radio'], y, c='b', marker='o')
ax.scatter(df['TV'], df['radio'], y_pred, c='r', marker='+')
ax.set_xlabel('TV')
ax.set_ylabel('radio')
ax.set_zlabel('sales')
plt.show()
