

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;


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

    public void drawGraph(String filePath, String fileName) {

        String textInFile = getFileText(filePath); // 파일내의 Text를 가져온다


        HashMap<String, LinkedHashSet<String>> functionMap = getFunctions(textInFile);
        drawNode(functionMap,fileName);


    }



    private String getFileText(String filePath){
        StringBuilder allTextInFile = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();

            while (line != null) {

                if( line.contains("//")){
                    line = line.substring(0,line.indexOf("//"));
                }

                allTextInFile.append(line);
                allTextInFile.append(System.lineSeparator());
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }




        return removeNeedlessText(allTextInFile.toString());

    }


    private String removeNeedlessText(String text){



        // remove Multi line comment
        while(true){
            int commentStartIndex = text.indexOf("/*");
            int commentEndIndex = text.indexOf("*/");

            // No more Multi line comment
            if(commentStartIndex == -1 ) break;

            text = text.substring(0,commentStartIndex-2).concat(text.substring(commentEndIndex+2));


        }

        String result = text;
        //result = result.replaceAll("async function" , "function"); // async 함수를 일반함수로
        //result = result.replaceAll("\n" , "");
        //result = result.replaceAll("\t" , "");
        //result = result.replaceAll(" " , "");

        System.out.println("result = " + result);

        return result;
    }


    private HashMap<String, LinkedHashSet<String>> getFunctions(String text){

        // 함수관계 추출
        // scwin.으로 시작하는 문자의 위치가져오기
        String[] texts = text.split("scwin.");
        ArrayList<String> functionList = new ArrayList<>();
        for (String s : texts) {
            // 함수 목록을 가져온다
            if (s.contains("=function")) {
                String[] functionNames = s.split("=function");
                functionList.add(functionNames[0]);
            }
        }

        // 해당 함수의 내용을 가져오기
        System.out.println("functionList = " + functionList);
        HashMap<String,String> bodyByFunction = new HashMap<>();
        for(int i=0; i<functionList.size(); i++){

            String definitionFunction = "scwin." + functionList.get(i) + "=function(";
            String body ="";
            if(i < functionList.size()-1){
                String nextFunction = "scwin." + functionList.get(i+1) + "=function(";
                int start = text.indexOf(definitionFunction);
                int end = text.indexOf(nextFunction);
                body = text.substring(start,end);
            } else {

                int start = text.indexOf(definitionFunction);
                body = text.substring(start);
            }

            body = body.replaceAll( "scwin." + functionList.get(i) , "");
            bodyByFunction.put(functionList.get(i) , body);

        }

        HashMap<String,LinkedHashSet<String>> result = new HashMap<>();
        bodyByFunction.forEach((key,value) ->{
            result.put(key, new LinkedHashSet<>());
            System.out.println("value = " + value);
            for(String functionName : functionList){
                if(value.contains("scwin."+functionName)){
                    System.out.println("functionName = " + functionName);
                    result.get(key).add(functionName);
                }
            }

        });

        System.out.println(result);

        return result;
    }

    private void drawNode(HashMap<String, LinkedHashSet<String>> functionMap,String fileName){

        ArrayList<Node> aaa = new ArrayList<>();

        functionMap.forEach((key,functionList) ->{

            if(functionList.size() == 0){
                aaa.add(node(key));
            } else {

                for (String function : functionList) {
                    aaa.add(node(key).with(Color.BLACK).link(function));
                }
            }

        });

        Graph g = graph("example1").directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(aaa);

        try {
            Graphviz.fromGraph(g).height(900).render(Format.SVG).toFile(new File("C:/graph/" + fileName +"_graph.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
