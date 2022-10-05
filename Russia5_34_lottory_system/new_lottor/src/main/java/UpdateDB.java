import java.time.LocalTime;
import java.util.List;


public class UpdateDB {
    public static void main(String args[]) {

        System.out.println();
        System.out.println("Load russia 5/36 results in database");
        System.out.println("Starting......");
        LocalTime newTime = LocalTime.now().plusMinutes(1);
        Lottor lottor = new Lottor();

        while (true) {

            LocalTime presentTime = LocalTime.now();

            if (presentTime.compareTo(newTime) == 0) {
                System.out.println(presentTime);
                WebScrape webScrape = new WebScrape();

                List<List<Integer>> numResults = webScrape.webResults();

                lottor.dbEvaluate(numResults);
                newTime = LocalTime.now().plusMinutes(30);
            }
        }
    }
}
