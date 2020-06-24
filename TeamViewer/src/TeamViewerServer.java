import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*; 
class TeamViewerServer
{
        static boolean serverb=true;
	public static void main(String a[])
	{
		new TeamViewerServer();
	}
	TeamViewerServer()
	{
		ScreenThread st=new ScreenThread();
		st.start();
		MouseThread mt=new MouseThread();
		mt.start();
		MouseClickThread mct=new MouseClickThread();
		mct.start();
		KeyThread kt=new KeyThread();
		kt.start();
	}
	static class KeyThread extends Thread
	{
		static ServerSocket server;
		KeyThread()
		{
			try{
				server=new ServerSocket(6068);
			}catch(Exception e)
			{}
		}
		public void run()
		{
			while(serverb)
			{
				try{
				Robot rt=new Robot();
				Socket client=server.accept();
				InputStream in=client.getInputStream();
				byte b[]=new byte[20];
				in.read(b);
				String s=new String(b).trim();
				String a[]=s.split("#");
				if(a[0].equals("keypressed")){
					int keycode=Integer.parseInt(a[1]);
					
					rt.keyPress(keycode);
				}else if(a[0].equals("keyreleased")){
					int keycode=Integer.parseInt(a[1]);
					
					rt.keyRelease(keycode);
				}
				}catch(Exception e)
				{
					System.out.println(e);
				}
			}
		}
	}
	static class MouseClickThread extends Thread
	{
		static ServerSocket server;
		MouseClickThread()
		{
			try{
			server=new ServerSocket(6066);
			}catch(Exception e)
			{
				System.out.println(e);
			}
		}
		public void run()
		{
			while(serverb){
			try{
			Socket client=server.accept();
			InputStream in=client.getInputStream();
			byte b[]=new byte[1];
			in.read(b);
			String s=new String(b);
			if(s.equals("1")){
				Robot rt=new Robot();
				rt.mousePress(InputEvent.BUTTON1_MASK);
				rt.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			else if(s.equals("3"))
			{
				Robot rt=new Robot();
				rt.mousePress(InputEvent.BUTTON3_MASK);
				rt.mouseRelease(InputEvent.BUTTON3_MASK);
			}
			else if(s.equals("4"))
			{
				Robot rt=new Robot();
				rt.mousePress(InputEvent.BUTTON1_MASK);
				rt.mouseRelease(InputEvent.BUTTON1_MASK);
				rt.mousePress(InputEvent.BUTTON1_MASK);
				rt.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			}catch(Exception ex)
			{
				System.out.println(ex);
			}
			}
		}
	}
	static public class MouseThread extends Thread
	{
		static ServerSocket server;
		MouseThread()
		{
			try{
			server=new ServerSocket(6064);
			}catch(Exception e)
			{
				System.out.println(e);
			}
		}
		public void run(){
			while(serverb)
			{
				try{
				Socket client=server.accept();
				InputStream in=client.getInputStream();
				byte b[]=new byte[10];
				in.read(b);
				String s=new String(b).trim();
				String a[]=s.split("#");
				Robot rt=new Robot();
				int x=Integer.parseInt(a[0]);
				int y=Integer.parseInt(a[1]);
				rt.mouseMove(x,y);
				}catch(Exception e)
				{
					System.out.println(e);
				}
			}
		}
	}
	static public class ScreenThread extends Thread
	{
		static ServerSocket server;
		ScreenThread()
		{
			try{
			server=new ServerSocket(6062);
			}catch(Exception e)
			{}
		}
		public void run()
		{
			while(serverb)
			{
				try{
				Socket client=server.accept();
				OutputStream out=client.getOutputStream();
				BufferedImage img=getScreenShot();
				ImageIO.write(img,"PNG",out);
				}catch(Exception e)
				{
					System.out.println(e);
				}
			}				
		}
		public BufferedImage getScreenShot()
		{
			BufferedImage img=null;
			try{
			Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
			Robot rt=new Robot();
			img=rt.createScreenCapture(new Rectangle(d));			
			}catch(Exception e)
			{}
			return img;	
		}
	}
}