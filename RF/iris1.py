from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.datasets import load_iris
import matplotlib.pyplot as plt

iris = load_iris()
x = iris.data[:, :2]
y = iris.target
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.33, random_state=42)

# n_estimators:森林中树的个数（默认为10），建议为奇数
# n_jobs:并行执行任务的个数（包括模型训练和预测），默认值为-1，表示根据核数
rnd_clf = RandomForestClassifier(n_estimators=15, max_leaf_nodes=16, n_jobs=1)
rnd_clf.fit(x_train, y_train)

y_predict_rf = rnd_clf.predict(x_test)

print(accuracy_score(y_test, y_predict_rf))

for name, score in zip(iris['feature_names'], rnd_clf.feature_importances_):
    print(name, score)
    
# 可视化
plt.plot(x_test[:, 0], y_test, 'r.', label='real')
plt.plot(x_test[:, 0], y_predict_rf, 'b.', label='predict')
plt.xlabel('sepal-length', fontsize=15)
plt.ylabel('type', fontsize=15)
plt.legend(loc="upper left")
plt.show()

plt.plot(x_test[:, 1], y_test, 'r.', label='real')
plt.plot(x_test[:, 1], y_predict_rf, 'b.', label='predict')
plt.xlabel('sepal-width', fontsize=15)
plt.ylabel('type', fontsize=15)
plt.legend(loc="upper right")
plt.show()
