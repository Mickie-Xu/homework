import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


data = pd.read_excel(r"C:\Users\Administrator\Desktop\data.xlsx")
data = data.values
print(data)

columns =["15plot1", "15plot2", "15plot3", "15plot4", "16plot1", "16plot2", "16plot3", "16plot4", "17plot1", "17plot2", "17plot3", "17plot4"] # 横坐标数值
rows = ['species %d' % x for x in range(38)] # 表格第一列名称

values = np.arange(0, 1, 0.1) # 纵坐标数值

# 选取几种颜色
colors = plt.cm.BuPu(np.linspace(0, 0.5, len(rows)))
n_rows = len(data)

index = np.arange(len(columns)) + 0.3

y_offset = np.array([0.0] * len(columns))

# 绘制条形图
cell_text = []
for row in range(n_rows):
    plt.bar(index, data[row], bottom=y_offset, color=colors[row])
    y_offset = y_offset + data[row]
    cell_text.append(['%1.1f' % (x) for x in y_offset])
# 反转颜色和标签
colors = colors[::-1]
cell_text.reverse()

# 在x轴底部添加一个表
the_table = plt.table(cellText=data,
                      rowLabels=rows,
                      rowColours=colors,
                      colLabels=columns,
                      loc='bottom')

plt.subplots_adjust(left=0.2, bottom=0.2)

plt.ylabel("species-ra")
plt.yticks(values, ['%d' % val for val in values])
plt.xticks([])

plt.show()