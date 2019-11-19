package runnable;

import java.io.IOException;
import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.InstanceExample;
import moa.streams.generators.RandomRBFGenerator;

/**
 *
 * @author Mustafa Yalciner
 */
public class HoeffingTreeLearner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        final int dataSize = 10000;

        RandomRBFGenerator randomGen = new RandomRBFGenerator();
        randomGen.prepareForUse();

        Classifier hoeffTreeClassifier = new HoeffdingTree();
        hoeffTreeClassifier.setModelContext(randomGen.getHeader());
        hoeffTreeClassifier.prepareForUse();
        double hoeffCorrectCounter = 0;

        Classifier naiveBayesClassifier = new NaiveBayes();
        naiveBayesClassifier.setModelContext(randomGen.getHeader());
        naiveBayesClassifier.prepareForUse();
        double naiveCorrectCounter = 0;

        for (int i = 0; i < dataSize; i++) {
            InstanceExample example = randomGen.nextInstance();
            boolean isHoeffCorrect = hoeffTreeClassifier.correctlyClassifies(example.getData());
            hoeffCorrectCounter = isHoeffCorrect ? hoeffCorrectCounter + 1 : hoeffCorrectCounter;
            hoeffTreeClassifier.trainOnInstance(example.getData());
            boolean isNaiveCorrect = naiveBayesClassifier.correctlyClassifies(example.getData());
            naiveCorrectCounter = isNaiveCorrect ? naiveCorrectCounter + 1 : naiveCorrectCounter;
            naiveBayesClassifier.trainOnInstance(example.getData());
        }
        System.out.println("Data size: " + dataSize);
        System.out.println("HOEFF Accuracy: " + hoeffCorrectCounter / dataSize);
        System.out.println("Naive Accuracy: " + naiveCorrectCounter / dataSize);
    }
}
