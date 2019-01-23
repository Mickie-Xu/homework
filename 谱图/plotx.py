import numpy as np
import pandas as pd
import matplotlib as plt

data = pd.read_excel(r"C:\Users\Administrator\Desktop\data.xlsx")
data = data.values
print(data)

columns=["15plot1","15plot2","15plot3","15plot4","16plot1","16plot2","16plot3","16plot4","17plot1","17plot2","17plot3","17plot4"]# 横坐标数值
rows=['species %d' %x for x in range(38)]# 表格第一列名称
                       
values = np.arange(0, 1, 0.1) # 纵坐标数值
n_rows = len(data)

index = np.arange(len(columns)) + 0.3
bar_width = 0.4

color = plt.cm.paired(np.linspace(0, 0.5, len(rows)))
#color = ["red","limegreen","darkorange","black","gold","blue","c","yellow","tan","silver","g","plum","pink","cyan","slategray","violet","wheat","lightcyan","biege","steelblue","tomato","peru","lawngreen","darkcyan","palegreen","indigo","skyblue","teal","navy","hotpink","crimson","cornsilk","darkseagreen","darkkhaki","brown","lightcoral","burlywood","darkslategray"]
y_offset = np.array([0.0] * len(columns))

# 绘制条形图
cell_text = []
for row in range(n_rows):
    plt.bar(index, data[row], bar_width, bottom=y_offset, color = plt.cm.coolwarm(range(38)))
    y_offset = y_offset + data[row]

fmt='%.2f%%'
#yticks = plt.ticker.FormatStrFormatter(fmt)

plt.ylabel("ra")
plt.xlabel(columns)
plt.yticks()
plt.xticks([])
plt.legend(loc=0)

plt.show()