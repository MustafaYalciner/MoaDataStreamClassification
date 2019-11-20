package runnable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.InstanceExample;
import moa.core.SerializeUtils;
import moa.core.Utils;
import moa.streams.generators.RandomRBFGenerator;

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
        RandomRBFGenerator randomGen = (RandomRBFGenerator) SerializeUtils.readFromFile(new File("SerializedDataSet.txt"));
        randomGen.prepareForUse();

        Classifier hoeffTreeClassifier = new HoeffdingTree();
        hoeffTreeClassifier.setModelContext(randomGen.getHeader());
        hoeffTreeClassifier.prepareForUse();
        double hoeffCorrectCounter = 0;

        Classifier naiveBayesClassifier = new NaiveBayes();
        naiveBayesClassifier.setModelContext(randomGen.getHeader());
        naiveBayesClassifier.prepareForUse();
        double naiveCorrectCounter = 0;

        double majorityVoteCorrectCounter = 0;
        for (int i = 0; i < dataSize; i++) {
            InstanceExample example = randomGen.nextInstance();
            isHoeffCorrect = hoeffTreeClassifier.correctlyClassifies(example.getData());
            hoeffCorrectCounter = isHoeffCorrect ? hoeffCorrectCounter + 1 : hoeffCorrectCounter;
            System.out.println("isHoeffCorrect? " + isHoeffCorrect);
            System.out.println("Prediction: "
                    + Arrays.toString(hoeffTreeClassifier.getPredictionForInstance(example.getData()).getVotes()));

            hoeffTreeClassifier.trainOnInstance(example.getData());
            isNaiveCorrect = naiveBayesClassifier.correctlyClassifies(example.getData());
            naiveCorrectCounter = isNaiveCorrect ? naiveCorrectCounter + 1 : naiveCorrectCounter;

            System.out.println("naiveBayesClassifier? " + isNaiveCorrect);
            System.out.println("Prediction: "
                    + Arrays.toString(naiveBayesClassifier.getPredictionForInstance(example.getData()).getVotes()));
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
    }

    private static int majorityVoteClassifier(double[] arr1, double[] arr2) {
        double[] retArr = new double[arr1.length];
        for (int i = 0; i < retArr.length; i++) {
            retArr[i] = arr1[i] + arr2[i];
        }
        return Utils.maxIndex(retArr);
    }

}
