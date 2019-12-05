package runnable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.InstanceExample;
import moa.core.SerializeUtils;
import moa.core.Utils;
import moa.streams.generators.RandomRBFGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Mustafa Yalciner
 */
public class HoeffingTreeLearner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final int dataSize = 10000;
        boolean isNaiveCorrect;
        boolean isHoeffCorrect;
        boolean isMajorityVoteCorrect;
        RandomRBFGenerator randomGen
                = (RandomRBFGenerator) SerializeUtils.readFromFile(new File("SerializedDataSet.txt"));
        randomGen.prepareForUse();

        Classifier hoeffTreeClassifier = new HoeffdingTree();
        hoeffTreeClassifier.setModelContext(randomGen.getHeader());
        hoeffTreeClassifier.prepareForUse();
        double hoeffCorrectCounter = 0;

        Classifier naiveBayesClassifier = new NaiveBayes();
        naiveBayesClassifier.setModelContext(randomGen.getHeader());
        naiveBayesClassifier.prepareForUse();
        double naiveCorrectCounter = 0;

        final XYSeries seriesHoeff = new XYSeries("Hoeffding Classification");
        final XYSeries seriesBayes = new XYSeries("Bayes Classification");

        double majorityVoteCorrectCounter = 0;
        for (int i = 0; i < dataSize; i++) {
            InstanceExample example = randomGen.nextInstance();
            isHoeffCorrect = hoeffTreeClassifier.correctlyClassifies(example.getData());
            hoeffCorrectCounter = isHoeffCorrect ? hoeffCorrectCounter + 1 : hoeffCorrectCounter;
            System.out.println("isHoeffCorrect? " + isHoeffCorrect);
            seriesHoeff.add(i, hoeffCorrectCounter / i);
            hoeffTreeClassifier.trainOnInstance(example.getData());

            isNaiveCorrect = naiveBayesClassifier.correctlyClassifies(example.getData());
            naiveCorrectCounter = isNaiveCorrect ? naiveCorrectCounter + 1 : naiveCorrectCounter;
            System.out.println("naiveBayesClassifier? " + isNaiveCorrect);
            seriesBayes.add(i, naiveCorrectCounter / i);
            naiveBayesClassifier.trainOnInstance(example.getData());

            isMajorityVoteCorrect = majorityVoteClassifier(naiveBayesClassifier.getPredictionForInstance(example.getData()).getVotes(),
                    hoeffTreeClassifier.getPredictionForInstance(example.getData()).getVotes())
                    == (int) example.getData().classValue();
            majorityVoteCorrectCounter = isMajorityVoteCorrect
                    ? majorityVoteCorrectCounter + 1
                    : majorityVoteCorrectCounter;
        }
        System.out.println("Data size: " + dataSize);
        System.out.println("HOEFF Accuracy: " + hoeffCorrectCounter / dataSize);
        System.out.println("Naive Accuracy: " + naiveCorrectCounter / dataSize);
        System.out.println("Majority vote Accuracy: " + majorityVoteCorrectCounter / dataSize);
        // Uncomment the following code if it is a plot of the accuracy over time is desired.
/**
        final XYSeriesCollection dataHoeff = new XYSeriesCollection(seriesHoeff);
        final XYSeriesCollection dataBayes = new XYSeriesCollection(seriesBayes);

        final JFreeChart chart = ChartFactory.createScatterPlot(
                "Accuracy Improvement for dataset size",
                "Number of processed data instances",
                "Accuracy",
                dataBayes,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.getXYPlot().setDataset(0, dataHoeff);
        chart.getXYPlot().setDataset(1, dataBayes);
        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        chart.getXYPlot().setRenderer(0, renderer0);
        chart.getXYPlot().setRenderer(1, renderer1);

        chart.getXYPlot().getRendererForDataset(dataHoeff).setSeriesPaint(0, Color.GREEN);
        chart.getXYPlot().getRendererForDataset(dataBayes).setSeriesPaint(1, Color.BLUE);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        **/
    }

    private static int majorityVoteClassifier(double[] arr1, double[] arr2) {
        double[] retArr = new double[arr1.length];
        for (int i = 0; i < retArr.length; i++) {
            retArr[i] = arr1[i] + arr2[i];
        }
        return Utils.maxIndex(retArr);
    }

}
