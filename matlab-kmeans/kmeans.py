import pandas as pd
import matplotlib as plt
from pandas.tools.plotting import radviz

data=pd.read_csv(r"C:\Users\Administrator\Desktop\matlabkmeans\data.csv")
#plt.figure()
print(data)
radviz(data, '1')
plt.show()