import java.sql.SQLException;
import java.util.*;

public class Lottor {

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        Lottor lottor = new Lottor();
        WebScrape webScrape = new WebScrape();
        DbConnect dbConnect = new DbConnect();

        List<List<Integer>> numResults = webScrape.webResults();

        System.out.println("OPTIONS :  \n1. BET\n2. Check if won or lost \n3. check if the new three number" +
                " results are not in the database");
        int choice = scan.nextInt();

        if(choice == 1) {

            System.out.println("do you want to insert number results from the internet manually e.g 1,2,3,4,5");
            String firstAnswer = scan.next();

            System.out.println("How many numbers do you want to play: ");
            int numbers = scan.nextInt();

            if(firstAnswer.equalsIgnoreCase("yes")){
                List<List<Integer>> inputNum = lottor.insertInternetResults();
                List<List<Integer>> totalDbResults = dbConnect.displayInternetResults();
                checkNumbers(totalDbResults,inputNum,numbers);
                lottor.dbEvaluate(inputNum);
            }

            List<List<Integer>> totalNumResults = dbConnect.displayInternetResults();
            if(numResults.size() > 0) {
                checkNumbers(totalNumResults, numResults,numbers);
            }
            lottor.dbEvaluate(numResults);
            System.out.println(totalNumResults);
            List<List<Integer>> totalResults = new ArrayList<>();

            for(List anotherList : totalNumResults) {
                List<List<Integer>> totalResults1 = combinations(anotherList,numbers);

                for(List r : totalResults1){
                    totalResults.add(r);
                }
            }


            for(List<Integer> tr : totalResults){
                Collections.sort(tr);
            }

            List<List<Integer>> newResult = unwanted(totalResults,new ArrayList<>(),numbers);

            System.out.println("The unwanted in the list are ");
            System.out.println(newResult);
            System.out.println("The size is " + newResult.size());
            System.out.println("What is the range number of the bet: ");
            int rangeBet = scan.nextInt();
            System.out.println("How many row do you want to generate: ");
            int rowNum = scan.nextInt();

            List<List<Integer>> playNumber = generator(newResult, numbers, rangeBet, rowNum);
            System.out.println(playNumber.toString().replace("], [", "],\n ["));

            System.out.println("Do you have a table in the dataBase: ");
            String respond = scan.next();
            if (respond.equalsIgnoreCase("no")) {
                dbConnect.deleteBetTable();
                dbConnect.createResultsTable();

            }
            System.out.println("Do you want to save the numbers ?");
            String answer = scan.next();

            if (answer.equalsIgnoreCase("yes")) {
                System.out.println("Enter today's date: ");
                int day = scan.nextInt();
                try {
                    dbConnect.deleteBetTable();
                    dbConnect.createGenResultsTable();
                    dbConnect.insertResultsData(day, playNumber);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (choice == 2) {
            System.out.println("How many numbers do you want to play: ");
            int numbers = scan.nextInt();
            checkWinningNumbers(numbers);
        }else if (choice == 3) {
            System.out.println("How many numbers do you want to play: ");
            int numbers = scan.nextInt();
            checkNumbersInDb(numbers);
        }

    }


    public static List<List<Integer>> combinations(List<Integer> input, int numbersToPlay) {
        return step(input, numbersToPlay, new ArrayList<>());
    }


    public static List<List<Integer>> step(List<Integer> input,
                                           int k,
                                           List<List<Integer>> result) {

        // We're done
        if (k == 0) {
            return result;
        }

        // Start with [[1], [2], [3]] in result
        if (result.size() == 0) {
            for (Integer i : input) {
                ArrayList<Integer> subList = new ArrayList<>();
                subList.add(i);

                result.add(subList);
            }

            // Around we go again.
            return step(input, k - 1, result);
        }

        // Cross result with input.  Taking us to 2 entries per sub list.  Then 3. Then...
        List<List<Integer>> newResult = new ArrayList<>();
        for (List<Integer> subList : result) {
            for(Integer i : input) {
                List<Integer> newSubList = new ArrayList<>();
                newSubList.addAll(subList);
                newSubList.add(i);
                newResult.add(newSubList);
            }
        }

        // Around we go again.
        return step(input, k - 1, newResult);
    }


    public static List<List<Integer>> unwanted(List<List<Integer>> totalResults,List<List<Integer>> unWantedList, int number) {
        for (int i = 0; i< totalResults.size(); i++){
            List secondList = new ArrayList<>();
            List firstList = totalResults.get(i);
            for (int k = 0; k< totalResults.get(i).size(); k++){
                if(!secondList.contains(firstList.get(k))){
                    secondList.add(firstList.get(k));
                }
            }

            if(secondList.size() == number){

                if(unWantedList.size() > 1) {
                    for (int t = 0; t < unWantedList.size(); t++) {
                        int count = 0;
                        for (int h = 0; h < unWantedList.get(t).size(); h++) {
                            for (int m = 0; m < secondList.size(); m++) {

                                if (secondList.get(m).equals(unWantedList.get(t).get(h))) {
                                    count++;
                                }

                            }
                        }

                        if (count == number) {
                            secondList.clear();
                        }
                    }
                    if (!secondList.isEmpty()) {
                        unWantedList.add(secondList);
                    }
                }else {
                    unWantedList.add(secondList);
                }

            }


        }
        return unWantedList;
    }

    /*
    generate random numbers
    pram : list of numbers
     */
    public static List<List<Integer>> generator(List<List<Integer>> listsOfUnWanted, int number, int rangeNum, int rowNum){

        List<List<Integer>> newList = new ArrayList<>();
        Random ram = new Random();
        for(int z = 0; z< 10000; z++) {

            List genNumbers = new ArrayList<>();
            while(genNumbers.size() != number) {
                int num = ram.nextInt(rangeNum) + 1;
                if(!genNumbers.contains(num)){
                    genNumbers.add(num);
                }
            }

            for (int t = 0; t < listsOfUnWanted.size(); t++) {
                int count = 0;
                for (int h = 0; h < listsOfUnWanted.get(t).size(); h++) {
                    for (int m = 0; m < genNumbers.size(); m++) {

                        if (genNumbers.get(m).equals(listsOfUnWanted.get(t).get(h))) {
                            count++;
                        }

                    }
                }

                if (count >= 2) {
                    genNumbers.clear();
                }
            }

            if(newList.size() > 0) {
                for (int t = 0; t < newList.size(); t++) {
                    int count2 = 0;
                    for (int h = 0; h < newList.get(t).size(); h++) {
                        for (int m = 0; m < genNumbers.size(); m++) {

                            if (genNumbers.get(m).equals(newList.get(t).get(h))) {
                                count2++;
                            }

                        }
                    }

                    if (count2 == number) {
                        genNumbers.clear();
                    }
                }

            }
            if (!genNumbers.isEmpty()) {
                Collections.sort(genNumbers);
                newList.add(genNumbers);
            }

            if(newList.size() == rowNum){
                return newList;
            }

        }
        System.out.println(newList.size());
        return newList;
    }


    public void dbEvaluate(List<List<Integer>> webScrapeList){
        DbConnect dbConnect = new DbConnect();
        List<List<Integer>> dbResults = dbConnect.displayInternetResults();
        List<List<Integer>> rangeList = new ArrayList<>();

        if(dbResults.size() == 0){
            for(List wbList : webScrapeList){
                dbResults.add(wbList);
            }

        }else {
            for (int r = 0; r < dbResults.size(); r++) {
                for (int z = 0; z < webScrapeList.size(); z++) {
                    int count = 0;
                    for (int x = 0; x < webScrapeList.get(z).size(); x++) {

                        if (dbResults.get(r).get(x).equals(webScrapeList.get(z).get(x))) {
                            count++;
                        }
                        if (count == webScrapeList.get(0).size()) {
                            webScrapeList.get(z).clear();

                        }
                    }

                }

            }
            for (List l : webScrapeList) {
                if(!l.isEmpty()) {
                    dbResults.add(l);
                }
            }

        }

        if(dbResults.size() > 250){
            for(int x= dbResults.size() - 250; x <dbResults.size(); x++){
                rangeList.add(dbResults.get(x));
            }
            dbResults = rangeList;
        }

        System.out.println("webScrap list");
        System.out.println(webScrapeList);

        if(webScrapeList.size() > 0) {
            try {
                dbConnect.deleteInternetTable();
                dbConnect.createResultsTable();
                dbConnect.insertInternetData(dbResults);

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    public List<List<Integer>> insertInternetResults(){

        Scanner scan = new Scanner(System.in);
        System.out.println("How many results row do you want to enter: ");

        int rowResults = scan.nextInt();
        int count = 0;

        List<List<Integer>> totalResults = new ArrayList<>();
        while(count != rowResults) {
            count++;

            System.out.println("Enter results: ");
            String resultsNum1 = scan.next();
            String[] resultsNum = resultsNum1.split(",");

            List<Integer> anotherList = new ArrayList<>();
            for (int b = 0; b < resultsNum.length; b++) {
                try {
                    anotherList.add(Integer.parseInt(resultsNum[b]));
                } catch (Exception e) {
                    System.out.println("its not working");
                }

            }
            totalResults.add(anotherList);
            if (count == rowResults){
                return totalResults;
            }
        }

        return totalResults;
    }


    public static void  checkNumbers(List<List<Integer>> db, List<List<Integer>> webScrapeList,int number){
        List<List<Integer>> dbCombo = new ArrayList<>();
        List<List<Integer>> webScrapCombo = new ArrayList<>();
        List<List<Integer>> webScrap = new ArrayList<>();

        for (int r = 0; r < db.size(); r++) {
            for (int z = 0; z < webScrapeList.size(); z++) {
                int count = 0;
                for (int x = 0; x < webScrapeList.get(z).size(); x++) {

                    if (db.get(r).get(x).equals(webScrapeList.get(z).get(x))) {
                        count++;
                    }
                    if (count == 5) {
                        webScrapeList.get(z).clear();

                    }
                }

            }

        }
        for (List l : webScrapeList) {
            if(!l.isEmpty()) {
                webScrap.add(l);
            }
        }

        for(List anotherList : db) {
            List<List<Integer>> totalResults1 = combinations(anotherList,number);
            for(List r : totalResults1){
                List checkCom = new ArrayList<>();
                for(int g=0; g< r.size(); g++){
                    if(!checkCom.contains(r.get(g))){
                        checkCom.add(r.get(g));
                    }
                }
                if(checkCom.size() == r.size()){
                    dbCombo.add(checkCom);
                }
            }
        }

        for(List anotherList : webScrap) {
            if(anotherList.size() > 1) {
                List<List<Integer>> totalResults1 = combinations(anotherList,number);
                for (List r : totalResults1) {
                    List checkCom = new ArrayList<>();
                    for (int g = 0; g < r.size(); g++) {
                        if (!checkCom.contains(r.get(g))) {
                            checkCom.add(r.get(g));
                        }
                    }
                    if (checkCom.size() == r.size()) {
                        webScrapCombo.add(checkCom);
                    }
                }
            }
        }

        for(List numbers : webScrapCombo) {
            for (int t = 0; t < dbCombo.size(); t++) {
                int count2 = 0;
                for (int m = 0; m < numbers.size(); m++) {

                    if (dbCombo.get(t).contains(numbers.get(m))) {
                        count2++;
                    }

                }

                if (count2 == numbers.size()) {
                    System.out.println(numbers);
                    System.out.println("The numbers are already in the dataBase");
                }
            }
        }

        System.out.println("The numbers are not in the dataBase");
    }


    public static void checkWinningNumbers(int number){

        Scanner scan = new Scanner(System.in);
        DbConnect dbConnect = new DbConnect();

        System.out.println("Enter results: ");
        String resultsNum1 = scan.next();
        String[] resultsNum = resultsNum1.split(",");

        List<Integer> anotherList = new ArrayList<>();
        for (int b = 0; b < resultsNum.length; b++) {
            try {
                anotherList.add(Integer.parseInt(resultsNum[b]));
            } catch (Exception e) {
                System.out.println("its not working");
            }
        }

        Collections.sort(anotherList);
        System.out.println("Enter the day primary key");
        int day = scan.nextInt();
        List<List<Integer>> dbResults = dbConnect.displayNumbers(day);
        List<List<Integer>> checkNum = combinations(anotherList, number);
        List<List<Integer>> newCheckNum = new ArrayList<>();

        for(List lis : checkNum){
            List num = new ArrayList<>();
            for(int u=0; u<checkNum.get(0).size(); u++){
                if(!num.contains(lis.get(u))){
                    num.add(lis.get(u));
                }
            }
            if(num.size() == 3) {
                Collections.sort(num);
                newCheckNum.add(num);
            }
        }

        for(List numbers : newCheckNum) {
            for (int t = 0; t < dbResults.size(); t++) {
                int count2 = 0;
                for (int m = 0; m < numbers.size(); m++) {

                    if (dbResults.get(t).contains(numbers.get(m))) {
                        count2++;
                    }

                }

                if (count2 == 3) {
                    System.out.println(numbers);
                    System.out.println("you won the bet");
                }
            }
        }

        System.out.println("you lose the bet");
    }


    public static void checkNumbersInDb(int number){

        Scanner scan = new Scanner(System.in);
        DbConnect dbConnect = new DbConnect();

        System.out.println("Enter results: ");
        String resultsNum1 = scan.next();
        String[] resultsNum = resultsNum1.split(",");

        List<Integer> anotherList = new ArrayList<>();
        for (int b = 0; b < resultsNum.length; b++) {
            try {
                anotherList.add(Integer.parseInt(resultsNum[b]));
            } catch (Exception e) {
                System.out.println("its not working");
            }
        }

        Collections.sort(anotherList);
        List<List<Integer>> dbResult = dbConnect.displayInternetResults();
        List<List<Integer>> checkNum = combinations(anotherList,number);
        List<List<Integer>> newCheckNum = new ArrayList<>();
        List<List<Integer>> dbCombo = new ArrayList<>();
        List<List<Integer>> dbResults = new ArrayList<>();

        for(List anotherList2 : dbResult) {
            List<List<Integer>> totalResults1 = combinations(anotherList2,number);
            for(List r : totalResults1){
                List checkCom = new ArrayList<>();
                for(int g=0; g< r.size(); g++){
                    if(!checkCom.contains(r.get(g))){
                        checkCom.add(r.get(g));
                    }
                }
                if(checkCom.size() == number){
                    dbCombo.add(checkCom);
                }
            }
        }

        for(List dbc : dbCombo){
            List newDb = new ArrayList<>();
            for(int h=0; h< dbc.size(); h++){
                if(!newDb.contains(dbc.get(h))){
                    newDb.add(dbc.get(h));
                }
            }
            if(newDb.size() == number){
                dbResults.add(newDb);
            }
        }


        for(List lis : checkNum){
            List num = new ArrayList<>();
            for(int u=0; u<checkNum.get(0).size(); u++){
                if(!num.contains(lis.get(u))){
                    num.add(lis.get(u));
                }
            }
            if(num.size() == number) {
                Collections.sort(num);
                newCheckNum.add(num);
            }
        }

        for(List numbers : newCheckNum) {
            for (int t = 0; t < dbResults.size(); t++) {
                int count2 = 0;
                for (int m = 0; m < numbers.size(); m++) {

                    if (dbResults.get(t).contains(numbers.get(m))) {
                        count2++;
                    }

                }

                if (count2 == number) {
                    System.out.println(numbers);
                    System.out.println("The three numbers are in the database");
                }
            }
        }

        System.out.println("The three numbers are not in the database");
    }

}
