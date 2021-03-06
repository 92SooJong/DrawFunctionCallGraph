import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




class Main {

    private static String fileName;

    public static void main(String[] args){

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

        frame.setVisible(true);



    }

    private static void placeComponents( JPanel panel) {


        panel.setLayout(null);

        // 불러오기 버튼 만들기
        JButton chooseFileButton = new JButton("Load File");
        chooseFileButton.setBounds(50, 20, 150, 25);
        panel.add(chooseFileButton);

        JTextField chooseFileText = new JTextField(20);
        chooseFileText.setBounds(250,20,500,25);
        chooseFileText.setEditable(false);
        panel.add(chooseFileText);


        JButton startAnalyzButton = new JButton("Create Graph");
        startAnalyzButton.setBounds(600, 60, 150, 25);
        panel.add(startAnalyzButton);

        // 파일선택 버튼 이벤트 처리
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileDialog fd = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
                fd.setMultipleMode(true);
                fd.setVisible(true);

                String directory = fd.getDirectory();
                fileName = fd.getFile();

                chooseFileText.setText(directory.concat(fileName));

            }
        });

        // 분석시작 버튼 클릭 이벤트 처리
        startAnalyzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FunctionCallGraph instance = new FunctionCallGraph();


                instance.drawGraph(chooseFileText.getText(),fileName);

            }
        });


    }



}