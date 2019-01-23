import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class KmeansDriver{
    public static void main(String[] args) throws Exception{
        int repeated = 0;

        /*
        �����ύMapReduce��ҵָ���������ε����������ĵľ���С����ֵ�򵽴��趨�ĵ�������
        */
        do {
            Configuration conf = new Configuration();
            String[] otherArgs  = new GenericOptionsParser(conf, args).getRemainingArgs();
            if (otherArgs.length != 6){
                System.err.println("Usage: <int> <out> <oldcenters> <newcenters> <k> <threshold>");
                System.exit(2);
            }
            conf.set("centerpath", otherArgs[2]);
            conf.set("kpath", otherArgs[4]);
            Job job = new Job(conf, "KMeansCluster");//�½�MapReduce��ҵ
            job.setJarByClass(KmeansDriver.class);//������ҵ������

            Path in = new Path(otherArgs[0]);
            Path out = new Path(otherArgs[1]);
            FileSystem fs0 = out.getFileSystem(conf);
            fs0.delete(out,true);
            fs0.close();
            FileInputFormat.addInputPath(job, in);//��������·��
            /*FileSystem fs = FileSystem.get(conf);
            if (fs.exists(out)){//������·�����ڣ�����ɾ��֮
                fs.delete(out, true);
            }*/
           /* FileSystem fs = out.getFileSystem(conf);

            fs.delete(out,true);
            fs.close();*/
            FileOutputFormat.setOutputPath(job, out);//�������·��

            job.setMapperClass(KmeansMapper.class);//����Map��
            job.setReducerClass(KmeansReducer.class);//����Reduce��

            job.setOutputKeyClass(IntWritable.class);//�������������
            job.setOutputValueClass(Text.class);//�������ֵ����

            job.waitForCompletion(true);//������ҵ

            ++repeated;
            System.out.println("We have repeated " + repeated + " times.");
         } while (repeated < 300 && (Assistance.isFinished(args[2], args[3], Integer.parseInt(args[4]), Float.parseFloat(args[5])) == false));
        //�������յõ��ľ������Ķ����ݼ����о���
        Cluster(args);
    }
    public static void Cluster(String[] args)
            throws IOException, InterruptedException, ClassNotFoundException{
        Configuration conf = new Configuration();
        String[] otherArgs  = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 6){
            System.err.println("Usage: <int> <out> <oldcenters> <newcenters> <k> <threshold>");
            System.exit(2);
        }
        conf.set("centerpath", otherArgs[2]);
        conf.set("kpath", otherArgs[4]);
        Job job = new Job(conf, "KMeansCluster");
        job.setJarByClass(KmeansDriver.class);

        Path in = new Path(otherArgs[0]);
        Path out = new Path(otherArgs[1]);
        FileInputFormat.addInputPath(job, in);
       /* FileSystem fs = FileSystem.get(conf);
        if (fs.exists(out)){
            fs.delete(out, true);
        }
        */
        FileSystem fs0 = out.getFileSystem(conf);
        fs0.delete(out,true);
        fs0.close();

        FileOutputFormat.setOutputPath(job, out);

        //����������࣬����Ҫreduce�������ʲ�����Reduce��
        job.setMapperClass(KmeansMapper.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        job.waitForCompletion(true);
    }
}