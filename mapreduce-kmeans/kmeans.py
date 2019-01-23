import numpy as np
import pandas as pd
from sklearn.cluster import KMeans

data=pd.read_csv(r"C:\Users\Administrator\Desktop\matlabkmeans\data.csv")
x=np.asarray(data)
y=KMeans(2)
y.fit(x)
print(y.labels_)