data=xlsread('data.xlsx');
[idx,Centers]=kmeans(data,5)
[COEFF,SCORE,latent] = pca(data);
SCORE = SCORE(:,1:3);
mappedX = tsne(SCORE,'Algorithm','exact','NumDimensions',3);
c=zeros(30,3);
scatter3(mappedX(:,1),mappedX(:,2),mappedX(:,3),3,'fill')