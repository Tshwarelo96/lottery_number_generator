
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Shane - shanelee.co.uk
 */


public class WebScrape {

    public List<List<Integer>> webResults() {
        ArrayList<String> results = new ArrayList<>();

        List<List<Integer>> finalResults = new ArrayList<>();

        final String url =
                "https://www.lotteryextreme.com/russia/gosloto_5x36-results";

        try {
            final Document document = Jsoup.connect(url).get();
            for (Element row : document.select(
                    "table.results2 tr")) {

                if (row.select("tr:nth-of-type(1)").text().equals("")) {
                    continue;
                }
                else {
                    final String ticker =
                            row.select("tr:nth-of-type(1)").text();
                    results.add(ticker);
                }
            }

            for(String numString : results){
                ArrayList<Integer> newResults = new ArrayList<>();
                String[] resul = numString.split(" ");
                for(int t= 0; t<resul.length-1; t++){
                    newResults.add(Integer.parseInt(resul[t]));
                }
                finalResults.add(newResults);
            }
            System.out.println("The results from internet ");
            System.out.println(finalResults);
            System.out.println("The size is : "+finalResults.size());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return finalResults;
    }

}