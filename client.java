import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client extends JFrame implements ActionListener {
    JTextField text;
    static JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    Client() {
        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(218, 112, 214));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        // Back button
        ImageIcon i1 = new ImageIcon("Icons/3.png");
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        // Profile Picture
        ImageIcon i4 = new ImageIcon("Icons/2.jpg");
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        // Video icon
        ImageIcon i7 = new ImageIcon("Icons/video.png");
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel video = new JLabel(new ImageIcon(i8));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        // Phone icon
        ImageIcon i10 = new ImageIcon("Icons/phone.png");
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_SMOOTH);
        JLabel phone = new JLabel(new ImageIcon(i11));
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        // More icon
        ImageIcon i13 = new ImageIcon("Icons/3icon.png");
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_SMOOTH);
        JLabel more = new JLabel(new ImageIcon(i14));
        more.setBounds(420, 20, 10, 25);
        p1.add(more);

        JLabel name = new JLabel("Customer Name");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.white);
        name.setFont(new Font("Times New Roman", Font.BOLD, 15));
        p1.add(name);

        JLabel status = new JLabel("Online");
        status.setBounds(110, 37, 100, 15);
        status.setForeground(Color.white);
        status.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        p1.add(status);

        // Panel for messages
        a1 = new JPanel();
        a1.setLayout(new BoxLayout(a1, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(a1);
        scrollPane.setBounds(5, 75, 440, 475);
        scrollPane.setBorder(null);
        f.add(scrollPane);

        // Message input field
        text = new JTextField();
        text.setBounds(5, 555, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        // Send button
        JButton send = new JButton("Send");
        send.setBounds(320, 555, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

        // Frame settings
        f.setSize(450, 600);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(new Color(135, 31, 120));
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText().trim();
            if (out.isEmpty()) return;

            JPanel p2 = formatLabel(out);

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            a1.add(vertical);
            a1.revalidate();

            dout.writeUTF(out);
            text.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel output = new JLabel("<html><p style=\"width:150px\">" + out + "</p></html>");
        output.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        new Client();

        try {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            // Use a thread to receive messages
            Thread receive = new Thread(() -> {
                try {
                    while (true) {
                        String msg = din.readUTF();
                        JPanel p2 = formatLabel(msg);

                        JPanel left = new JPanel(new BorderLayout());
                        left.add(p2, BorderLayout.LINE_START);
                        vertical.add(left);
                        vertical.add(Box.createVerticalStrut(15));
                        a1.add(vertical);
                        a1.revalidate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receive.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
