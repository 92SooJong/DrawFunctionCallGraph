import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  Singleton
 */
public class FunctionCallGraph {

    private static FunctionCallGraph single_instance = null;

    private FunctionCallGraph(){

    }

    public static FunctionCallGraph getInstance() {
        if (single_instance == null)
            single_instance = new FunctionCallGraph();
        return single_instance;
    }

    public void drawGraph(String filePath){

        String allText = getFileText(filePath);
        System.out.println(allText);



    }



    private String getFileText(String filePath){
        String allTextInFile = "";
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                allTextInFile += data;

            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return allTextInFile.replaceAll(" ","");

    }




}
