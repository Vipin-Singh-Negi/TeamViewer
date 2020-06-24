import java.net.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
class TeamViewerClient extends JFrame
{
	String ip;
	JLabel l,nav;
        boolean b;
        ImageIcon icon1,icon2;
	public static void main(String a[])
	{
		new TeamViewerClient("");
	}
	TeamViewerClient(String ip)
	{
	   super(ip);
           setLayout(null);
           icon1=new ImageIcon(getClass().getResource("up_nav.png"));
           icon2=new ImageIcon(getClass().getResource("down_nav.png"));
	   setAlwaysOnTop(true);
	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   addMouseMotionListener(new MouseMotionAdapter(){
	      public void mouseMoved(MouseEvent e)
 	      {
		int x=e.getX();
		int y=e.getY();
		try{
		Socket client=new Socket(ip,6064);
		OutputStream out=client.getOutputStream();
		out.write((x+"#"+y).getBytes());	
		}catch(Exception ex)
		{
			System.out.println(ex);
		}
	      }
	  });
	  addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent e)
  		{
		try{
		  Socket client=new Socket(ip,6066);
		  OutputStream out=client.getOutputStream();
		  int b=e.getButton();
		  int cc=e.getClickCount();
		  if(cc==1)
		    out.write((b+"").getBytes());
		  else if(cc==2)
		    out.write(("4").getBytes());
		  }catch(Exception ex)
		  {
			System.out.println(ex);
		  }
		}	
	  });
	  addKeyListener(new KeyAdapter(){
		public void keyPressed(KeyEvent e)
		{
		  try{
		  Socket client=new Socket(ip,6068);
		  OutputStream out=client.getOutputStream();		
    		  int keycode=e.getKeyCode();
		  out.write(("keypressed#"+keycode).getBytes()); 
		  }catch(Exception ex)
		  {
			System.out.println(ex);
		  }
		}
		public void keyReleased(KeyEvent e)
		{
		  try{
		  Socket client=new Socket(ip,6068);
		  OutputStream out=client.getOutputStream();		
    		  int keycode=e.getKeyCode();
		  out.write(("keyreleased#"+keycode).getBytes()); 
		  }catch(Exception ex)
		  {
			System.out.println(ex);
		  }	
		}
	  });
	  this.ip=ip;
          setTitle(ip);
          try{
            BufferedImage img=ImageIO.read(getClass().getResourceAsStream("controllo_remoto.png"));
            setIconImage(img);
          }catch(Exception e)
          {}
          Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
	  int sw=(int)d.getWidth();
	  int sh=(int)d.getHeight();
	  l=new JLabel("Please wait....");
          l.setBounds(0,0, sw,sh);
	  l.setHorizontalAlignment(JLabel.CENTER);
          nav=new JLabel(icon1);
          nav.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                   if(!b){
                       dispose();
                       setUndecorated(true);
                       nav.setIcon(icon2);
                       nav.setBounds(sw-55, sh-60, 52, 52);
                       b=true;
                       setSize(sw,sh);
                       setLocationRelativeTo(null);
                       setVisible(true);
                   }else{
                       dispose();
                       setUndecorated(false);
                       nav.setIcon(icon1);
                       nav.setBounds(sw-70, sh-100, 52, 52);
                       b=false;
                       setSize(sw,sh);
                       setLocationRelativeTo(null);
                       setVisible(true);
                   }
               }
          });
          nav.setBounds(sw-70, sh-100, 52, 52);
          add(nav);
	  add(l);
	  setSize(sw,sh);
	  setVisible(true);
	  ScreenThread st=new ScreenThread();
	  st.start();		
	}
	class ScreenThread extends Thread
	{
	   public void run()
	   {
  	     while(true)
	     {
	      try{
	      Socket client=new Socket(ip,6062);
	      InputStream in=client.getInputStream();
	      BufferedImage img=ImageIO.read(in);
    	      l.setText("");
	      ImageIcon icon=new ImageIcon(img);
	      l.setIcon(icon);
	      }catch(Exception e)
	      {
		System.out.println(e);
	      }
	     }
			
	  }
	}
}