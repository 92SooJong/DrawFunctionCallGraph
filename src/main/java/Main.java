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
        
        frame.setSize(1200, 800); // Frame 사이즈설정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // panel은 HTML의 div태그 같은 느낌!
        JPanel panel = new JPanel();
        panel.setSize(600,500);
        // frame에 panel 붙이기
        frame.add(panel);
        
        placeComponents(panel); // panel에 컴포넌트 그리기



        // Setting the frame visibility to true
        frame.setVisible(true);


        //start();


    }

    private static void placeComponents( JPanel panel) {

        /* We will discuss about layouts in the later sections
         * of this tutorial. For now we are setting the layout
         * to null
         */
        panel.setLayout(null);

        // 불러오기 버튼 만들기
        JButton loginButton = new JButton("파일 불러오기");
        loginButton.setBounds(50, 20, 150, 25);
        panel.add(loginButton);

        JTextField userText = new JTextField(20);
        userText.setBounds(250,20,300,25);
        userText.setEditable(false);
        panel.add(userText);


        // 불러오기 버튼 만들기
        JButton commonFile = new JButton("공통파일 불러오기");
        commonFile.setBounds(50, 90, 150, 25);
        panel.add(commonFile);
        



        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(panel, "Hello World!");
                FileDialog fd = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
                fd.setVisible(true);
                String directory = fd.getDirectory();
                String filename = fd.getFile();

                JOptionPane.showMessageDialog(panel, filename);
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
