package runnable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import moa.core.InstanceExample;
import moa.streams.generators.RandomRBFGenerator;
import weka.classifiers.trees.HoeffdingTree;

/**
 *
 * @author Mustafa Yalciner
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        final int dataSize = 10000;
        RandomRBFGenerator randomGen = new RandomRBFGenerator();
        randomGen.prepareForUse();

        File fout = new File("RBFdataset.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < dataSize; i++) {
            InstanceExample example = randomGen.nextInstance();
            double[] row = example.getData().toDoubleArray();
            for (int j = 0; j < row.length; j++) {
                bw.write(String.valueOf(row[j]));
                if (j < row.length - 1) {
                    bw.write(",");
                }
            }
            if (i < dataSize - 1) {
                bw.newLine();
            }
        }
        bw.close();

        // TODO code application logic here
    }

}
