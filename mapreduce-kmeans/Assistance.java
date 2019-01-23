import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;
import java.util.*;

public class Assistance {
    //��ȡ�������ĵ���Ϣ����������ID���������ĵ�
    public static List<ArrayList<Float>> getCenters(String inputpath){
        List<ArrayList<Float>> result = new ArrayList<ArrayList<Float>>();
        Configuration conf = new Configuration();
        try {

            Path in = new Path(inputpath);
            FileSystem hdfs = in.getFileSystem(conf);
            FSDataInputStream fsIn = hdfs.open(in);
            LineReader lineIn = new LineReader(fsIn, conf);
            Text line = new Text();
            ArrayList<Float>  center = null;
            while (lineIn.readLine(line) > 0){
                String record = line.toString();
                center = new ArrayList<Float>();
                /*
                ��ΪHadoop�����ֵ��ʱ���ڼ���ֵ֮������Ʊ����
                �����ÿո����֮��
                */
                String[] fields = record.split("\t");
                //List<Float> tmplist = new ArrayList<Float>();
                for (int i = 0; i < fields.length; ++i){
                    center.add(Float.parseFloat(fields[i]));
                }
                result.add(center);
            }
            fsIn.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    //ɾ����һ��MapReduce��ҵ�Ľ��
    public static void deleteLastResult(String path){
        Configuration conf = new Configuration();
        try {

            Path path1 = new Path(path);
            FileSystem hdfs = path1.getFileSystem(conf);
            hdfs.delete(path1, true);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    //�����������ε�������ľ������ĵľ��룬�ж��Ƿ�������ֹ����
    public static boolean isFinished(String oldpath, String newpath, int k, float threshold)
    throws IOException{
        List<ArrayList<Float>> oldcenters = Assistance.getCenters(oldpath);
        List<ArrayList<Float>> newcenters = Assistance.getCenters(newpath);
        float distance = 0;
        int dimension=oldcenters.get(0).size();
        System.out.println("����:"+k);
        System.out.println("ά����:"+dimension);
        for (int i = 0; i < k; ++i){
            for (int j = 0; j <dimension; ++j){
                float tmp = Math.abs(oldcenters.get(i).get(j) - newcenters.get(i).get(j));
                distance += Math.pow(tmp, 2);
            }
        }
        System.out.println("Distance = " + distance + " Threshold = " + threshold);
        if (distance < threshold)
            return true;
        /*
        �����������ֹ���������ñ��ε����ľ������ĸ��¾�������
        */
        Assistance.deleteLastResult(oldpath);
        Configuration conf = new Configuration();
        //FileSystem hdfs = FileSystem.get(conf);
        Path path0 = new Path(newpath);
        FileSystem hdfs=path0.getFileSystem(conf);
        hdfs.copyToLocalFile(new Path(newpath), new Path("/home/hadoop/hadoop-tmp/oldcenter.data"));
        hdfs.delete(new Path(oldpath), true);
        hdfs.moveFromLocalFile(new Path("/home/hadoop/hadoop-tmp/oldcenter.data"), new Path(oldpath));
        return false;
    }
}