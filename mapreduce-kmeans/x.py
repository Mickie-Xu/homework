import numpy as np

x=np.random.randint(1,30,(12,3))
y=np.random.randint(20,50,(10,3))
z=np.random.randint(45,90,(15,3))

np.savetxt(r'C:\Users\Administrator\Desktop\kmeans71.txt',x,fmt='%0.8f')
np.savetxt(r'C:\Users\Administrator\Desktop\kmeans72.txt',y,fmt='%0.8f')
np.savetxt(r'C:\Users\Administrator\Desktop\kmeans7.txt',y,fmt='%0.8f')