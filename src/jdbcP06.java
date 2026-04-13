import java.sql.*;
import java.awt.*;
import java.awt.event.*;
class jdbcP06 extends Frame implements ItemListener,ActionListener
{
    Checkbox cs,cc;
    CheckboxGroup cbg;

    Label la,lc;

    jdbcP06(){

        this.setLayout(new FlowLayout());

        cbg=new CheckboxGroup();
        cs=new Checkbox("Log-In",cbg,false);
        cc=new Checkbox("Create Account",cbg,false);

        la=new Label("Already have an account");
        lc=new Label("New User");

        this.add(la);
        this.add(cs);
        this.add(lc);
        this.add(cc);

        cs.addItemListener(this);
        cc.addItemListener(this);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });

    }
    public static void main(String args[]){
        jdbcP06 j1=new jdbcP06();
        j1.setTitle("Sign-In");
        j1.setSize(400,400);
        j1.setVisible(true);
    }

    public void itemStateChanged(ItemEvent ie){
        if(ie.getSource() == cc){
            FrameCR fc=new FrameCR("jdbc:oracle:thin:@localhost:1521:xe","system","Akaal-hi-akaal");
            fc.setTitle("Create Account");
            fc.setSize(400,400);
            fc.setVisible(true);
        }
        if(ie.getSource() == cs){
            FrameLO fl=new FrameLO("jdbc:oracle:thin:@localhost:1521:xe","system","Akaal-hi-akaal");
            fl.setTitle("Log-In");
            fl.setSize(400,400);
            fl.setVisible(true);
        }
    }
    public void actionPerformed(ActionEvent ae){

    }
}
class FrameCR extends Frame implements ActionListener{
    Label lu,lp;
    TextField tu,tp;

    String Tuser,Tpass;

    static String  url,user,pass;

    Button bs;

    boolean found=false;

    FrameCR(String url,String user,String pass){

        this.url=url;
        this.user=user;
        this.pass=pass;

        this.setLayout(new FlowLayout());

        lu=new Label("Enter Username");
        lp=new Label("Enter Password");

        tu=new TextField(20);
        tp=new TextField(20);
        tp.setEchoChar('*');

        bs=new Button("Save");

        this.add(lu);
        this.add(tu);
        this.add(lp);
        this.add(tp);
        this.add(bs);

        tu.addActionListener(this);
        tp.addActionListener(this);
        bs.addActionListener(this);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    public void actionPerformed(ActionEvent ae){

        if(ae.getSource() == bs){
            Tuser=tu.getText();
            Tpass=tp.getText();
            try{
                Connection con=DriverManager.getConnection(url,user,pass);
                Statement stmt=con.createStatement();
                System.out.println("Connected Successfully!");
                ResultSet rs1= stmt.executeQuery("SELECT USERNAME FROM logInData");
                while (rs1.next()){
                    String cch=rs1.getString("USERNAME");
                    if(Tuser.equals(cch)){
                        found=true;
                        repaint();
                        return;

                    }
                }
                if(!found){
                    addData(Tuser,Tpass,stmt);

                    FrameW fw=new FrameW();
                    fw.setTitle("Welcome!");
                    fw.setSize(400,400);
                    fw.setVisible(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public void paint(Graphics g){
        if(found){
            g.drawString("Already Taken",130,130);
        }

    }

    public void addData(String au,String ap,Statement stmt){

        String query="INSERT INTO logInData VALUES('"+au+"','"+ap+"')";
        try{
            ResultSet rs=stmt.executeQuery(query);
        }
        catch (SQLException se){
            se.printStackTrace();
        }
    }
    public static void main(String args[]){

    }
}
class FrameW extends Frame implements ActionListener {
    FrameW(){
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }

    public static void main(String args[]){
    }
    public void actionPerformed(ActionEvent ae){
        repaint();
    }
    public void paint(Graphics g){
        g.setColor(Color.red);
        Font f1=new Font("f1",Font.ROMAN_BASELINE,20);
        g.drawString("WELCOME!",180,180);
    }
}

class FrameLO extends Frame implements ActionListener{
    Label llu,llp;
    TextField ttu,ttp;
    Button bln;
    String lurl,luser,lpass;
    boolean fol=false;
    boolean attempt=false;
    FrameLO(String lurl,String luser,String lpass){
        this.lurl=lurl;
        this.luser=luser;
        this.lpass=lpass;

        this.setLayout(new FlowLayout());

        llu=new Label("Enter Username");
        llp=new Label("Enter Password");

        ttu=new TextField(20);
        ttp=new TextField(20);
        ttp.setEchoChar('*');

        bln=new Button("Next");

        this.add(llu);
        this.add(ttu);
        this.add(llp);
        this.add(ttp);
        this.add(bln);

        ttu.addActionListener(this);
        ttp.addActionListener(this);
        bln.addActionListener(this);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == bln){
            String lu=ttu.getText();
            String lp=ttp.getText();
            fol=false;
            attempt=true;

            try{
                Connection con=DriverManager.getConnection(lurl,luser,lpass);
                Statement stmt=con.createStatement();

                String query="SELECT USERNAME,PASSWORD FROM logInData";
                ResultSet rs= stmt.executeQuery(query);

                while(rs.next()){
                    String ch=rs.getString("USERNAME");
                    String ps=rs.getString("PASSWORD");
                    if(lu.equals(ch) && lp.equals(ps)){
                        fol=true;
                        break;

                    }

                }
                if(fol){
                    FrameW fw=new FrameW();
                    fw.setTitle("Welcome!");
                    fw.setSize(400,400);
                    fw.setVisible(true);
                }
                else{
                    repaint();
                }

            }
            catch (SQLException se){
                se.printStackTrace();

            }
        }
    }
    public void paint(Graphics g){
        if(attempt && !fol){
            g.drawString("Invalid Details",130,130);
        }

    }
}