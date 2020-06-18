package core;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import util.Timer;
import client.input.Keyboard;
import client.input.Mouse;

public class Main 
{
	int windowWidth, windowHeight;
	long window;
	
	public void run()
	{
		init();
		loop();

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	private void init()
	{
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		windowWidth = 1280;
		windowHeight = 720;

		window = glfwCreateWindow(windowWidth, windowHeight, "Window", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			Keyboard.setKeyState(key, action);
		});

		glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
			Mouse.setPosition(xpos, ypos);
		});
		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			Mouse.onMouseButton(button, action, mods);
		});
		
		glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			Mouse.onMouseScroll(xoffset, yoffset);
		});

		try (MemoryStack stack = stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(window, pWidth, pHeight);
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
	}
	
	void perspectiveGL( double fovY, double aspect, double zNear, double zFar )
	{
		double fW, fH;

	    //fH = tan( (fovY / 2) / 180 * pi ) * zNear;
	    fH = Math.tan( fovY / 360 * Math.PI ) * zNear;
	    fW = fH * aspect;

	    glFrustum( -fW, fW, -fH, fH, zNear, zFar );
	}
	
	private void setupOpenGL()
	{
		glfwSetWindowSizeCallback(window, (window, width1, height1) -> {
			windowWidth = width1;
			windowHeight = height1;
			glViewport(0, 0, width1, height1);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			perspectiveGL(70, width1 / (float) height1, 0.05F, 1000);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
		});

		glViewport(0, 0, windowWidth, windowHeight);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		perspectiveGL(70, windowWidth / (float) windowHeight, 0.05F, 1000);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glClearColor(0.25F, 0.5F, 0.75F, 1.0F);
		glColor4f(1, 1, 1, 1);
		
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.1F);
	}
	
	float rotate;
	int type;
	
	private void addVertexSim(double x, double y, double origX)
	{
		glVertex2d(origX+x, y);
		glVertex2d(origX-x, y);
	}
	
	private void renderInd1()
	{
		glPushMatrix();
		glTranslatef(0,0, -5f);
		glRotatef(rotate, 0, 0, 1);
		glScalef(0.01f, 0.01f, 1);
		glBegin(GL_QUADS);
		float xSize = 25;
		float x = -300;
		glColor3f(0.5f, 0.75f, 1f);
		addVertexSim(xSize, -300, x);
		addVertexSim(-xSize, -50, x);
		
		addVertexSim(xSize, 300, x);
		addVertexSim(-xSize, 50, x);
		
		addVertexSim(xSize, -300, -x);
		addVertexSim(-xSize, -50, -x);
		
		addVertexSim(xSize, 300, -x);
		addVertexSim(-xSize, 50, -x);

		x+=70;
		addVertexSim(xSize, -300, x);
		addVertexSim(-xSize, -50, x);
		
		addVertexSim(xSize, 300, x);
		addVertexSim(-xSize, 50, x);
		
		addVertexSim(xSize, -300, -x);
		addVertexSim(-xSize, -50, -x);
		
		addVertexSim(xSize, 300, -x);
		addVertexSim(-xSize, 50, -x);
		glColor3f(0.5f, 0.5f, 0.5f);
		xSize = 150;
		x=-150;
		addVertexSim(xSize, -25, x);
		addVertexSim(-xSize, 25, x);
		
		addVertexSim(xSize, -25, -x);
		addVertexSim(-xSize, 25, -x);
		xSize = 20;
		x=-265;
		addVertexSim(xSize, -50, x);
		addVertexSim(-xSize, -25, x);
		
		addVertexSim(xSize, -50, -x);
		addVertexSim(-xSize, -25, -x);
		
		addVertexSim(xSize, 50, x);
		addVertexSim(-xSize, 25, x);
		
		addVertexSim(xSize, 50, -x);
		addVertexSim(-xSize, 25, -x);
		
		xSize = 50;
		x=0;
		addVertexSim(xSize, -50, x);
		addVertexSim(-xSize, 50, x);
		xSize = 25;

		addVertexSim(xSize, -175, x);
		addVertexSim(-xSize, -50, x);
		xSize = 50;
		addVertexSim(xSize, -200, x);
		addVertexSim(-xSize, -175, x);
		x = -75;
		xSize = 25;
		glColor3f(0.5f, 0.75f, 1f);
		addVertexSim(xSize, -275, x);
		addVertexSim(-xSize, -75, x);
		
		addVertexSim(xSize, -275, -x);
		addVertexSim(-xSize, -75, -x);
		
		glEnd();
		glPopMatrix();
	}
	
	private void addVertexSim(double x, double y, double z, double origX, double origY, double origZ)
	{
		glPushMatrix();
		glVertex3d(origX+x, y, z);
		glVertex3d(origX-x, y, z);
		glPopMatrix();
	}
	
	private ArrayList<House> houses = new ArrayList<House>();
	private ArrayList<Car> cars = new ArrayList<Car>();
	private ArrayList<Road> roads = new ArrayList<Road>();
	double angle, angle1;
	
	Vector3f addColor = new Vector3f();
	
	int fogtype;
	
	private void renderCube(float x, float y, float z)
	{
		glDisable(GL_LIGHTING);
		glPushMatrix();
		glTranslatef(x, y, z);
		glScalef(2, 2, 2);
		glBegin(GL_QUADS);
		float size = 0.5f;
		glNormal3f(-size, 0, 0f);
		glVertex3d(-size,-size,-size);
		glVertex3d(-size,size,-size);
		glVertex3d(-size,size,size);
		glVertex3d(-size,-size,size);
		glNormal3f(0, 1, 0f);
		glVertex3d(-size,size,size);
		glVertex3d(size,size,size);
		glVertex3d(size,size,-size);
		glVertex3d(-size,size,-size);
		glNormal3f(0, -1, 0f);
		glVertex3d(-size,-size,size);
		glVertex3d(-size,-size,-size);
		glVertex3d(size,-size,-size);
		glVertex3d(size,-size,size);
		glNormal3f(0, 0, -1f);
		glVertex3d(size,size,-size);
		glVertex3d(size,-size,-size);
		glVertex3d(-size,-size,-size);
		glVertex3d(-size,size,-size);
		glNormal3f(0, 0, 1f);
		glVertex3d(size,size,size);
		glVertex3d(size,-size,size);
		glVertex3d(-size,-size,size);
		glVertex3d(-size,size,size);
		glNormal3f(1, 0, 0f);
		glVertex3d(size,size,size);
		glVertex3d(size,-size,size);
		glVertex3d(size,-size,-size);
		glVertex3d(size,size,-size);
		glEnd();
		glPopMatrix();
		glEnable(GL_LIGHTING);
	}
	
	private void renderInd2()
	{
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHT1);
		glEnable(GL_COLOR_MATERIAL);

//		glColorMaterial(GL_FRONT, GL_AMBIENT);
//		glColorMaterial(GL_FRONT, GL_DIFFUSE);
//		glColorMaterial(GL_FRONT, GL_SPECULAR);
		FloatBuffer amb = BufferUtils.createFloatBuffer(4);
		amb.put(0.1f).put(0.1f).put(0.1f).put(1.0f).flip();
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, amb);
		
		FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
		Vector3f lightVect = new Vector3f(0, 0f, 40f);
		//MathRotateHelper.rotateAboutY(lightVect, angle);
		MathRotateHelper.rotateAboutX(lightVect, angle1);
		FloatBuffer lightPos1 = BufferUtils.createFloatBuffer(4);
		Vector3f lightVect1 = new Vector3f(0, 0f, -40f);
		//MathRotateHelper.rotateAboutY(lightVect1, angle);
		MathRotateHelper.rotateAboutX(lightVect1, angle1);
		lightPos.put(lightVect.x).put(lightVect.y).put(lightVect.z).put(1.0f).flip();
		lightPos1.put(lightVect1.x).put(lightVect1.y).put(lightVect1.z).put(1.0f).flip();
		
		
		float l;
		float l1;
		
		if(angle1 < (float) -Math.PI)
		{
			l = 1;
		}
		else
		{
			l = 0;
		}

		if(angle1 > (float) -Math.PI)
		{
			l1 = 1;
		}
		else
		{
			l1 = 0;
		}
			
		FloatBuffer lightColor = BufferUtils.createFloatBuffer(4);
		lightColor.put((1f+addColor.x)*l).put(1.0f*l).put(0.75f*l).put(1.0f).flip();
		FloatBuffer lightColor1 = BufferUtils.createFloatBuffer(4);
		lightColor1.put((0.25f+addColor.x)*l1).put(0.25f*l1).put(0.4f*l1).put(1.0f).flip();
				

        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightColor);
        glLightfv(GL_LIGHT1, GL_DIFFUSE, lightColor1);
        //System.out.println(fogtype);
        switch(fogtype)
        {
        case 0:
    		FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
            fogColor.put(0.25f).put(0.5f).put(0.75f).put(1.0f).flip();

    		glFogi(GL_FOG_MODE, GL_LINEAR);
    		glFogfv(GL_FOG_COLOR, fogColor); 
    		glFogf(GL_FOG_DENSITY, 1.0f); 
    		glHint(GL_FOG_HINT, GL_DONT_CARE); 
    		glFogf(GL_FOG_START, 4.0f); 
    		glFogf(GL_FOG_END, 50.0f); 
        	break;
        case 1:
    		fogColor = BufferUtils.createFloatBuffer(4);
            fogColor.put(1f).put(0.5f).put(0.25f).put(1.0f).flip();

    		glFogi(GL_FOG_MODE, GL_LINEAR);
    		glFogfv(GL_FOG_COLOR, fogColor); 
    		glFogf(GL_FOG_DENSITY, 1.0f); 
    		glHint(GL_FOG_HINT, GL_DONT_CARE); 
    		glFogf(GL_FOG_START, 4.0f); 
    		glFogf(GL_FOG_END, 50.0f); 
        	break;
        case 2: 
        	fogColor = BufferUtils.createFloatBuffer(4);
            fogColor.put(1f).put(1f).put(1f).put(1.0f).flip();

    		glFogi(GL_FOG_MODE, GL_LINEAR);
    		glFogfv(GL_FOG_COLOR, fogColor); 
    		glFogf(GL_FOG_DENSITY, 1.0f); 
    		glHint(GL_FOG_HINT, GL_LINEAR); 
    		glFogf(GL_FOG_START, 2.0f); 
    		glFogf(GL_FOG_END, 20.0f); 
        	break;
        }

		glEnable(GL_FOG);
		glPushMatrix();
		glTranslatef(0,-1,-4f);
		glRotatef(5f, 1, 0, 0);
		//rotate=1f;
		glRotatef(rotate, 0, 1, 0);
		glRotatef(180, 0, 0, 1);

        glLightfv(GL_LIGHT0, GL_POSITION, lightPos);
        glLightfv(GL_LIGHT1, GL_POSITION, lightPos1);
        
		
		glPushMatrix();
		float size = 10;
		glTranslatef(0, 0.71f, 0);
		glColor3f(0.1f,0.1f,0.1f);

		glBegin(GL_QUADS);
		glNormal3f(0, -1, 0);
		for(int i= -3; i< 4; i++)
		{
			for(int j=-3;j<4;j++)
			{
		glVertex3d(-size+(i*20), 0, -size+(j*20));
		glVertex3d(-size+(i*20), 0, size+(j*20));
		glVertex3d(size+(i*20), 0, size+(j*20));
		glVertex3d(size+(i*20), 0, -size+(j*20));
			}
		}
		glEnd();
		glPopMatrix();
		glColor3f(1, 0, 0);
		renderCube(lightVect.x, lightVect.y, lightVect.z);
		glColor3f(0, 0, 1);
		renderCube(lightVect1.x, lightVect1.y, lightVect1.z);
		
		glPushMatrix();
		float scale = 100f;
		glScalef(scale, scale, scale);
		renderCube(0, 0, 0);
		glPopMatrix();
		for(int i=0;i<houses.size();i++)
			houses.get(i).render();
		for(int i=0;i<roads.size();i++)
			roads.get(i).render();
		for(int i=0;i<cars.size();i++)
			cars.get(i).render();

		glPopMatrix();
	}
	
	private void renderScene()
	{
		switch(type)
		{
		case 0:
			renderInd2();
			break;
		case 1:

			break;
		}
	}
	
	private void update()
	{
		angle += 0.01f;
		angle1 -= 0.005f;
//		angle1 = Math.PI-0.2f;
		if(angle > 2*Math.PI)
			angle -=  2*Math.PI;
		if(angle1 <  -2*Math.PI)
			angle1 +=  2*Math.PI;
		for(int i=0;i<roads.size();i++)
		{
			Road r = roads.get(i);
			r.z += 0.1f;
			if(r.z > 44)
			{
				r.z = -199.5f;
			}
		}
		
		for(int i=0;i<houses.size();i++)
		{
			House h = houses.get(i);
			h.z +=0.1f;
			if(h.z > 84)
			{
				houses.remove(i);
			}
		}
		
		for(int i=4;i<cars.size();i++)
		{
			Car c = cars.get(i);
			if(c.x == -2)
			{
				c.z +=0.05f;
				if(c.z > 76)
				{
					cars.remove(i);
				}
			}
			else
			{
				c.z -=0.05f;
				if(c.z < -76)
				{
					cars.remove(i);
				}
			}
		}
		
		if(cars.size() < 20)
		{
			int r = rand.nextInt(2);
			Car c = new Car(r == 0 ? -2 : 2, 0, r == 0 ? -80 : 80,rand);
			
			boolean isAdd = true;
			for(int i = 0; i<cars.size();i++)
			{
				Car c1 = cars.get(i);
				if(Math.abs(c.x - c1.x) < 1)
				{
					if(Math.abs(c.z - c1.z) < 20)
					{
						isAdd = false;
						break;
					}
				}
			}
			if(isAdd)
			cars.add(c);
		}
		
		if(houses.size() < 500)
		{
			generateHouse();
		}
		
		
		float baseSpeed = 1f;
		if(Keyboard.KEY_W.isPress())
		{
			Keyboard.KEY_W.isPress = 0;
			fogtype++;
			if(fogtype > 2)
				fogtype = 2;
		}
		else if(Keyboard.KEY_S.isPress())
		{
			Keyboard.KEY_S.isPress = 0;
			fogtype--;
			if(fogtype < 0)
				fogtype = 0;
		}
		if(Keyboard.KEY_DOWN.isPress())
		{
			addColor.x += 0.01f;
			if(addColor.x > 1)
				addColor.x = 1;
		}
		else if(Keyboard.KEY_UP.isPress())
		{
			addColor.x -= 0.01f;
			if(addColor.x < 0)
				addColor.x = 0;
		}
		if(Keyboard.KEY_LEFT.isPress() || Keyboard.KEY_A.isPress())
		{
			rotate-=baseSpeed;
		}
		else if(Keyboard.KEY_RIGHT.isPress() || Keyboard.KEY_D.isPress())
		{
			rotate+=baseSpeed;
		}
	}
	
	Random rand = new Random();
	
	private void generateHouse()
	{
		float x = 6 + rand.nextFloat()*20;
		float z =  -100;
		float size = 1f + rand.nextFloat()*2f;
		float height = 1f + rand.nextFloat()*20f;
		Vector3f color = new Vector3f(0.25f+rand.nextFloat()/1.33f, 0.25f+rand.nextFloat()/1.33f, 0.25f+rand.nextFloat()/1.33f);
		
		boolean isCollide = false;
		for(int i = 0; i<houses.size();i++)
		{
			House h = houses.get(i);
			if(Math.abs(x - h.x) < size + h.size)
			{
				if(Math.abs(z - h.z) < size + h.size)
				{
					//generateHouse();
					return;
				}
			}
		}

		houses.add(new House(x, 0, z, size, height, color));
		
		x = -6 - rand.nextFloat()*20;
		z =  -100;
		size = 1f + rand.nextFloat()*2f;
		height = 1f + rand.nextFloat()*20f;
		color = new Vector3f(0.25f+rand.nextFloat()/1.33f, 0.25f+rand.nextFloat()/1.33f, 0.25f+rand.nextFloat()/1.33f);
		
		for(int i = 0; i<houses.size();i++)
		{
			House h = houses.get(i);
			if(Math.abs(x - h.x) < size + h.size)
			{
				if(Math.abs(z - h.z) < size + h.size)
				{
					//generateHouse();
					return;
				}
			}
		}

		houses.add(new House(x, 0, z, size, height, color));
	}

	private void loop()
	{
		GL.createCapabilities();

		setupOpenGL();

		float delta;
		float accumulator = 0f;
		float interval = 1f / 60.0f;

		Timer timer = new Timer();
		
		cars.add(new Car(0, 0, 0,rand));
		cars.add(new Car(0, 0, 15,rand));
		cars.add(new Car(0, 0, -10,rand));
		cars.add(new Car(0, 0, -20,rand));

		for(int i=0;i<50;i++)
		{
			roads.add(new Road(1, -4*i));
			roads.add(new Road(-1, -4*i));
			roads.add(new Road(1, 4*i));
			roads.add(new Road(-1, 4*i));
		}
		
		for(int i=0;i<10;i++)
		{
			generateHouse();
		}

		while (!glfwWindowShouldClose(window))
		{
			delta = timer.getDelta();
			accumulator += delta;

			while (accumulator >= interval)
			{
				update();
				timer.updateUPS();
				accumulator -= interval;
				Mouse.updateState();
			}
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			renderScene();
		
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}
	
	public static void main(String[] args)
	{
		new Main().run();
	}
}
