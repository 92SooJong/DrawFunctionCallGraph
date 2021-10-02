import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;



class Main {

    public static void main(String[] args) throws IOException {

        // JFrame 생성
        JFrame frame = new JFrame("Function Call Graph");
        
        frame.setSize(800, 150); // Frame 사이즈설정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // panel은 HTML의 div태그 같은 느낌!
        JPanel panel = new JPanel();
        panel.setSize(800,150);
        // frame에 panel 붙이기
        frame.add(panel);
        
        placeComponents(panel); // panel에 컴포넌트 그리기



        // Setting the frame visibility to true
        frame.setVisible(true);


        //start();


    }

    private static void placeComponents( JPanel panel) {


        panel.setLayout(null);

        // 불러오기 버튼 만들기
        JButton chooseFileButton = new JButton("파일 불러오기");
        chooseFileButton.setBounds(50, 20, 150, 25);
        panel.add(chooseFileButton);

        JTextField chooseFileText = new JTextField(20);
        chooseFileText.setBounds(250,20,500,25);
        chooseFileText.setEditable(false);
        panel.add(chooseFileText);


        JButton startAnalyzButton = new JButton("그래프 파일 만들기");
        startAnalyzButton.setBounds(600, 60, 150, 25);
        panel.add(startAnalyzButton);


        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                FileDialog fd = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
                fd.setMultipleMode(true);
                fd.setVisible(true);

                String directory = fd.getDirectory();
                String filename = fd.getFile();

                chooseFileText.setText(directory.concat(filename));
            }
        });







        
        /* 텍스 필드를 작성한다
         */
//        JTextField userText = new JTextField(20);
//        userText.setBounds(100,20,165,25);
//        panel.add(userText);

        // Same process for password label and text field.
//        JLabel passwordLabel = new JLabel("공통파일");
//        passwordLabel.setBounds(10,50,80,25);
//        panel.add(passwordLabel);

        /*This is similar to text field but it hides the user
         * entered data and displays dots instead to protect
         * the password like we normally see on login screens.
         */
//        JPasswordField passwordText = new JPasswordField(20);
//        passwordText.setBounds(100,50,165,25);
//        panel.add(passwordText);


    }


    private static void start() throws IOException {


        Graph g = graph("example1").directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(
                        node("a").with(Color.RED).link(node("b")),
                        node("b").link(
                                to(node("c")).with(attr("weight", 5), Style.DASHED)
                        )
                );
        Graphviz.fromGraph(g).height(100).render(Format.SVG).toFile(new File("example/ex1.html"));


    }


}
