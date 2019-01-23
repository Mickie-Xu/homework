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
        不断提交MapReduce作业指导相邻两次迭代聚类中心的距离小于阈值或到达设定的迭代次数
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
            Job job = new Job(conf, "KMeansCluster");//新建MapReduce作业
            job.setJarByClass(KmeansDriver.class);//设置作业启动类

            Path in = new Path(otherArgs[0]);
            Path out = new Path(otherArgs[1]);
            FileSystem fs0 = out.getFileSystem(conf);
            fs0.delete(out,true);
            fs0.close();
            FileInputFormat.addInputPath(job, in);//设置输入路径
            /*FileSystem fs = FileSystem.get(conf);
            if (fs.exists(out)){//如果输出路径存在，则先删除之
                fs.delete(out, true);
            }*/
           /* FileSystem fs = out.getFileSystem(conf);

            fs.delete(out,true);
            fs.close();*/
            FileOutputFormat.setOutputPath(job, out);//设置输出路径

            job.setMapperClass(KmeansMapper.class);//设置Map类
            job.setReducerClass(KmeansReducer.class);//设置Reduce类

            job.setOutputKeyClass(IntWritable.class);//设置输出键的类
            job.setOutputValueClass(Text.class);//设置输出值的类

            job.waitForCompletion(true);//启动作业

            ++repeated;
            System.out.println("We have repeated " + repeated + " times.");
         } while (repeated < 300 && (Assistance.isFinished(args[2], args[3], Integer.parseInt(args[4]), Float.parseFloat(args[5])) == false));
        //根据最终得到的聚类中心对数据集进行聚类
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

        //将样本点聚类，不需要reduce操作，故不设置Reduce类
        job.setMapperClass(KmeansMapper.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        job.waitForCompletion(true);
    }
}