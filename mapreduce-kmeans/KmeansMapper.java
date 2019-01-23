import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KmeansMapper extends Mapper<Object, Text, IntWritable, Text> {
    public void map(Object key, Text value, Context context)
    throws IOException, InterruptedException{
        String line = value.toString();
        String[] fields = line.split("\t");
        List<ArrayList<Float>> centers = Assistance.getCenters(context.getConfiguration().get("centerpath"));
        int k = Integer.parseInt(context.getConfiguration().get("kpath"));
        float minDist = Float.MAX_VALUE;
        int centerIndex = 0;
        //���������㵽�������ĵľ��룬�����������ൽ������������ĵ���������
        for (int i = 0; i < k; ++i){
            float currentDist = 0;
            for (int j = 0; j < fields.length; ++j){
                float tmp = Math.abs(centers.get(i).get(j) - Float.parseFloat(fields[j]));
                currentDist += Math.pow(tmp, 2);
            }
            if (currentDist<minDist ){
                minDist = currentDist;
                centerIndex = i;
            }
        }
        System.out.println("Mapper����ļ�ֵ�ԣ�"+centerIndex+"����>"+value.toString());
        context.write(new IntWritable(centerIndex), new Text(value));
    }
}