import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

class ClusterChecker {
    final ArrayList<SingleInstance> initialDataset;
    private ArrayList<Centroid> centroids;

    public ClusterChecker(ArrayList<SingleInstance> dataset, ArrayList<Centroid> centroids) {
        this.initialDataset = dataset;
        this.centroids = centroids;
    }

    /**
     * Find the closet cluster and set the cluster class to that cluster.
     *
     * @param singleInstance SingleInstance
     */
    private boolean findClosetCluster(SingleInstance singleInstance, boolean isFinished) {
        double[] distance = distance(singleInstance);

        for(int i = 0; i < centroids.size(); i++) {
            if(distance[i] < singleInstance.getDistance()) {
                singleInstance.setClusterClass(i);
                singleInstance.setDistance(distance[i]);
                isFinished = true;
            }
        }
        return isFinished;
    }

    /**
     * Loop through each element in the dataset and assign it to a cluster.
     */
    private boolean calculateClosestClustersToData(boolean isFinished) {
        for(SingleInstance s : initialDataset) {
            isFinished = findClosetCluster(s, isFinished);
        }
        return isFinished;
    }

    /**
     * Check the Euclidean distance from a point to each of the centroids.
     *
     * @param singleInstance SingleInstance
     * @return double
     */
    private double[] distance(SingleInstance singleInstance) {
        double[] results = new double[centroids.size()];
        int index = 0;
        for(Centroid centroid : centroids) {
            results[index] = Math.sqrt(Math.pow((singleInstance.getPollution().getNitricOxide() - centroid.getNitricOxide()), 2) + Math.pow((singleInstance.getPollution().getSulphurDioxide() - centroid.getSulphurDioxide()), 2) + Math.pow((singleInstance.getPollution().getOzone() - centroid.getOzone()), 2));
            index++;
        }
        return results;
    }

    /**
     * Updates the centroid with the average values from the points within the cluster.
     *
     * @param clusterClass int
     * @return Centroid
     */
    private Centroid updatedCentroid(int clusterClass) {
        double averageNitricOxide = 0.0;
        double averageSulphurDioxide = 0.0;
        double averageOzone = 0.0;
        int count = 0;

        for(SingleInstance singleInstance : initialDataset) {
            if(singleInstance.getClusterClass() == clusterClass) {
                averageNitricOxide = averageNitricOxide + singleInstance.getPollution().getNitricOxide();
                averageSulphurDioxide = averageSulphurDioxide + singleInstance.getPollution().getSulphurDioxide();
                averageOzone = averageOzone + singleInstance.getPollution().getOzone();
                count++;
            }
        }
        return new Centroid(averageNitricOxide / count, averageSulphurDioxide / count, averageOzone / count);
    }

    /**
     * Checks the elements in each cluster 10 times to ensure higher accuracy.
     */
    public void recheckLoop() {
        boolean isFinished = true;
        while(isFinished) {
            isFinished = false;
            isFinished = calculateClosestClustersToData(isFinished);
            centroids = new ArrayList<>(Arrays.asList(updatedCentroid(0), updatedCentroid(1), updatedCentroid(2)));
        }
        print();
    }

    /**
     * Prints the Pollution values from each cluster
     */
    private void print() {
        try {
            FileWriter clusterOne = new FileWriter("clusterOne.csv");
            FileWriter clusterTwo = new FileWriter("clusterTwo.csv");
            FileWriter clusterThree = new FileWriter("clusterThree.csv");
            FileWriter clusterFour = new FileWriter("clusterFour.csv");

            for(SingleInstance singleInstance : initialDataset) {
                switch(singleInstance.getClusterClass()) {
                    case 0:
                        clusterOne.append(String.valueOf(singleInstance.getPollution().getNitricOxide())).append(",");
                        clusterOne.append(String.valueOf(singleInstance.getPollution().getSulphurDioxide())).append(",");
                        clusterOne.append(String.valueOf(singleInstance.getPollution().getOzone())).append(",");
                        clusterOne.append(String.valueOf(singleInstance.getBikeData().getHireTime())).append("\n");
                        break;
                    case 1:
                        clusterTwo.append(String.valueOf(singleInstance.getPollution().getNitricOxide())).append(",");
                        clusterTwo.append(String.valueOf(singleInstance.getPollution().getSulphurDioxide())).append(",");
                        clusterTwo.append(String.valueOf(singleInstance.getPollution().getOzone())).append(",");
                        clusterTwo.append(String.valueOf(singleInstance.getBikeData().getHireTime())).append("\n");
                        break;
                    case 2:
                        clusterThree.append(String.valueOf(singleInstance.getPollution().getNitricOxide())).append(",");
                        clusterThree.append(String.valueOf(singleInstance.getPollution().getSulphurDioxide())).append(",");
                        clusterThree.append(String.valueOf(singleInstance.getPollution().getOzone())).append(",");
                        clusterThree.append(String.valueOf(singleInstance.getBikeData().getHireTime())).append("\n");
                        break;
                    case 3:
                        clusterFour.append(String.valueOf(singleInstance.getPollution().getNitricOxide())).append(",");
                        clusterFour.append(String.valueOf(singleInstance.getPollution().getSulphurDioxide())).append(",");
                        clusterFour.append(String.valueOf(singleInstance.getPollution().getOzone())).append(",");
                        clusterFour.append(String.valueOf(singleInstance.getBikeData().getHireTime())).append("\n");
                        break;

                    default:
                        System.out.println("Unknown Cluster Class number");
                }
            }

            clusterOne.flush();
            clusterTwo.flush();
            clusterThree.flush();
            clusterFour.flush();

            clusterOne.close();
            clusterTwo.close();
            clusterThree.close();
            clusterFour.close();
        } catch(Exception e) {
            System.out.println("Failure in print() - " + e);
        }
    }
}