package core;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import org.joml.Vector3f;

public class Car
{
	public float x,y,z;
	public Vector3f color;
	
	public Car(float x, float y, float z, Random rand)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = new Vector3f(0.25f+rand.nextFloat()/1.33f, 0.25f+rand.nextFloat()/1.33f, 0.25f+rand.nextFloat()/1.33f);
	}

	public void render()
	{
		Vector3f winColor = new Vector3f(0.25f,0.5f,0.75f);
		Vector3f wheelColor = new Vector3f(0.25f,0.25f,0.25f);
		glPushMatrix();
		glTranslatef(x, y-0.5f, z);
		glColor4f(1f, 1f, 1f, 1f);
		//glScalef(0.01f, 0.02f, 0.01f);
		//krisha
		glBegin(GL_QUADS);
		glColor3f(color.x, color.y, color.z);
		glNormal3f(0, -1, 0);
		glVertex3d(-0.5,-0,-0.75);
		glVertex3d(-0.5,-0,0.75);
		glVertex3d(0.5,-0,0.75);
		glVertex3d(0.5,-0,-0.75);
		//left
		glColor3f(winColor.x, winColor.y, winColor.z);
		glNormal3f(1, 0, 0);
		glVertex3d(0.5,-0,-0.75);
		glVertex3d(0.5,0.5,-0.75);
		glVertex3d(0.5,0.5,0.75);
		glVertex3d(0.5,-0,0.75);
		glColor3f(color.x, color.y, color.z);
		glNormal3f(1, 0, 0);
		glVertex3d(0.5,0.5,-1.1);
		glVertex3d(0.5,0.95,-1.1);
		glVertex3d(0.5,0.95,0.3);
		glVertex3d(0.5,0.5,0.3);
		glNormal3f(1, 0, 0);
		glVertex3d(0.5,0.5,0.3);
		glVertex3d(0.5,0.6,0.3);
		glVertex3d(0.5,0.6,1);
		glVertex3d(0.5,0.5,1);
		//right
		glColor3f(winColor.x, winColor.y, winColor.z);
		glNormal3f(-1, 0, 0);
		glVertex3d(-0.5,-0,-0.75);
		glVertex3d(-0.5,0.5,-0.75);
		glVertex3d(-0.5,0.5,0.75);
		glVertex3d(-0.5,-0,0.75);
		glColor3f(color.x, color.y, color.z);
		glNormal3f(-1, 0, 0);
		glVertex3d(-0.5,0.5,-1.1);
		glVertex3d(-0.5,0.95,-1.1);
		glVertex3d(-0.5,0.95,0.3);
		glVertex3d(-0.5,0.5,0.3);
		glNormal3f(-1, 0, 0);
		glVertex3d(-0.5,0.5,-1.1);
		glVertex3d(-0.5,0.6,-1.1);
		glVertex3d(-0.5,0.6,-1.8);
		glVertex3d(-0.5,0.5,-1.8);
		glNormal3f(-1, 0, 0);
		glVertex3d(-0.5,0.5,0.3);
		glVertex3d(-0.5,0.6,0.3);
		glVertex3d(-0.5,0.6,1);
		glVertex3d(-0.5,0.5,1);
		//steklo
		glColor3f(winColor.x, winColor.y, winColor.z);
		glNormal3f(0, -1, -1f);
		glVertex3d(-0.5,-0,-0.75);
		glVertex3d(-0.5,0.5,-1.25);
		glVertex3d(0.5,0.5,-1.25);
		glVertex3d(0.5,-0,-0.75);
		glNormal3f(0, -1, 1f);
		glVertex3d(-0.5,-0,0.75);
		glVertex3d(-0.5,0.5,1.25);
		glVertex3d(0.5,0.5,1.25);
		glVertex3d(0.5,-0,0.75);
		glColor3f(color.x, color.y, color.z);
		glNormal3f(1, 0, 0);
		glVertex3d(0.5,0.5,-1.1);
		glVertex3d(0.5,0.6,-1.1);
		glVertex3d(0.5,0.6,-1.8);
		glVertex3d(0.5,0.5,-1.8);
		//zad
		glColor3f(color.x, color.y, color.z);
		glNormal3f(0, 0, 1f);
		glVertex3d(-0.5,0.5,1.25);
		glVertex3d(-0.5,0.75,1.25);
		glVertex3d(0.5,0.75,1.25);
		glVertex3d(0.5,0.5,1.25);
		
		glNormal3f(1, 0, 0);
		glVertex3d(0.5,0.5,1.25);
		glVertex3d(0.5,0.75,1.25);
		glVertex3d(0.5,0.75,1.0);
		glVertex3d(0.5,0.5,1.0);
		glNormal3f(-1, 0, 0);
		glVertex3d(-0.5,0.5,1.25);
		glVertex3d(-0.5,0.75,1.25);
		glVertex3d(-0.5,0.75,1.0);
		glVertex3d(-0.5,0.5,1.0);
		
		glNormal3f(0, 1, 1f);
		glVertex3d(-0.5,0.75,1.25);
		glVertex3d(-0.5,0.95,1.0);
		glVertex3d(0.5,0.95,1.0);
		glVertex3d(0.5,0.75,1.25);
		//dno
		glNormal3f(0, 1, 0f);
		glVertex3d(-0.4,0.9,-1.85);
		glVertex3d(-0.4,0.9,1.05);
		glVertex3d(0.4,0.9,1.05);
		glVertex3d(0.4,0.9,-1.85);
		//wheels
		glColor3f(wheelColor.x, wheelColor.y, wheelColor.z);
		glNormal3f(1, 0, 0f);
		glVertex3d(0.5,1.2,0.4);
		glVertex3d(0.5,0.7,0.4);
		glVertex3d(0.5,0.7,0.9);
		glVertex3d(0.5,1.2,0.9);
		glNormal3f(0, 1, 0f);
		glVertex3d(0.5,1.2,0.4);
		glVertex3d(0.5,1.2,0.9);
		glVertex3d(0.4,1.2,0.9);
		glVertex3d(0.4,1.2,0.4);
		glNormal3f(0, -1, 0f);
		glVertex3d(0.5,0.7,0.4);
		glVertex3d(0.5,0.7,0.9);
		glVertex3d(0.4,0.7,0.9);
		glVertex3d(0.4,0.7,0.4);
		glNormal3f(0, 0, 1f);
		glVertex3d(0.4,1.2,0.9);
		glVertex3d(0.4,0.7,0.9);
		glVertex3d(0.5,0.7,0.9);
		glVertex3d(0.5,1.2,0.9);
		glNormal3f(0, 0, -1f);
		glVertex3d(0.4,1.2,0.4);
		glVertex3d(0.4,0.7,0.4);
		glVertex3d(0.5,0.7,0.4);
		glVertex3d(0.5,1.2,0.4);
		glNormal3f(-1, 0, 0f);
		glVertex3d(0.4,1.2,0.4);
		glVertex3d(0.4,0.7,0.4);
		glVertex3d(0.4,0.7,0.9);
		glVertex3d(0.4,1.2,0.9);
		
		glNormal3f(1, 0, 0f);
		glVertex3d(0.5,1.2,-1.2);
		glVertex3d(0.5,0.7,-1.2);
		glVertex3d(0.5,0.7,-1.7);
		glVertex3d(0.5,1.2,-1.7);
		glNormal3f(0, 1, 0f);
		glVertex3d(0.5,1.2,-1.2);
		glVertex3d(0.5,1.2,-1.7);
		glVertex3d(0.4,1.2,-1.7);
		glVertex3d(0.4,1.2,-1.2);
		glNormal3f(0, -1, 0f);
		glVertex3d(0.5,0.7,-1.2);
		glVertex3d(0.5,0.7,-1.7);
		glVertex3d(0.4,0.7,-1.7);
		glVertex3d(0.4,0.7,-1.2);
		glNormal3f(0, 0, -1f);
		glVertex3d(0.4,1.2,-1.7);
		glVertex3d(0.4,0.7,-1.7);
		glVertex3d(0.5,0.7,-1.7);
		glVertex3d(0.5,1.2,-1.7);
		glNormal3f(0, 0, 1f);
		glVertex3d(0.4,1.2,-1.2);
		glVertex3d(0.4,0.7,-1.2);
		glVertex3d(0.5,0.7,-1.2);
		glVertex3d(0.5,1.2,-1.2);
		glNormal3f(-1, 0, 0f);
		glVertex3d(0.4,1.2,-1.2);
		glVertex3d(0.4,0.7,-1.2);
		glVertex3d(0.4,0.7,-1.7);
		glVertex3d(0.4,1.2,-1.7);
		
		glNormal3f(-1, 0, 0f);
		glVertex3d(-0.5,1.2,0.4);
		glVertex3d(-0.5,0.7,0.4);
		glVertex3d(-0.5,0.7,0.9);
		glVertex3d(-0.5,1.2,0.9);
		glNormal3f(0, 1, 0f);
		glVertex3d(-0.5,1.2,0.4);
		glVertex3d(-0.5,1.2,0.9);
		glVertex3d(-0.4,1.2,0.9);
		glVertex3d(-0.4,1.2,0.4);
		glNormal3f(0, -1, 0f);
		glVertex3d(-0.5,0.7,0.4);
		glVertex3d(-0.5,0.7,0.9);
		glVertex3d(-0.4,0.7,0.9);
		glVertex3d(-0.4,0.7,0.4);
		glNormal3f(0, 0, 1f);
		glVertex3d(-0.4,1.2,0.9);
		glVertex3d(-0.4,0.7,0.9);
		glVertex3d(-0.5,0.7,0.9);
		glVertex3d(-0.5,1.2,0.9);
		glNormal3f(0, 0, -1f);
		glVertex3d(-0.4,1.2,0.4);
		glVertex3d(-0.4,0.7,0.4);
		glVertex3d(-0.5,0.7,0.4);
		glVertex3d(-0.5,1.2,0.4);
		glNormal3f(1, 0, 0f);
		glVertex3d(-0.4,1.2,0.4);
		glVertex3d(-0.4,0.7,0.4);
		glVertex3d(-0.4,0.7,0.9);
		glVertex3d(-0.4,1.2,0.9);
		
		glNormal3f(-1, 0, 0f);
		glVertex3d(-0.5,1.2,-1.2);
		glVertex3d(-0.5,0.7,-1.2);
		glVertex3d(-0.5,0.7,-1.7);
		glVertex3d(-0.5,1.2,-1.7);
		glNormal3f(0, 1, 0f);
		glVertex3d(-0.5,1.2,-1.2);
		glVertex3d(-0.5,1.2,-1.7);
		glVertex3d(-0.4,1.2,-1.7);
		glVertex3d(-0.4,1.2,-1.2);
		glNormal3f(0, -1, 0f);
		glVertex3d(-0.5,0.7,-1.2);
		glVertex3d(-0.5,0.7,-1.7);
		glVertex3d(-0.4,0.7,-1.7);
		glVertex3d(-0.4,0.7,-1.2);
		glNormal3f(0, 0, -1f);
		glVertex3d(-0.4,1.2,-1.7);
		glVertex3d(-0.4,0.7,-1.7);
		glVertex3d(-0.5,0.7,-1.7);
		glVertex3d(-0.5,1.2,-1.7);
		glNormal3f(0, 0, 1f);
		glVertex3d(-0.4,1.2,-1.2);
		glVertex3d(-0.4,0.7,-1.2);
		glVertex3d(-0.5,0.7,-1.2);
		glVertex3d(-0.5,1.2,-1.2);
		glNormal3f(1, 0, 0f);
		glVertex3d(-0.4,1.2,-1.2);
		glVertex3d(-0.4,0.7,-1.2);
		glVertex3d(-0.4,0.7,-1.7);
		glVertex3d(-0.4,1.2,-1.7);
		glColor3f(color.x, color.y, color.z);
		//kapot
		glNormal3f(0, -1, 0);
		glVertex3d(-0.5,0.5,-1.25);
		glVertex3d(-0.5,0.5,-2);
		glVertex3d(0.5,0.5,-2);
		glVertex3d(0.5,0.5,-1.25);
		//bamper
		glNormal3f(0, 0, -1);
		glVertex3d(-0.5,0.5,-2);
		glVertex3d(-0.5,0.75,-2);
		glVertex3d(0.5,0.75,-2);
		glVertex3d(0.5,0.5,-2);
		
		glNormal3f(1, 0, 0);
		glVertex3d(0.5,0.5,-1.8);
		glVertex3d(0.5,0.75,-1.8);
		glVertex3d(0.5,0.75,-2);
		glVertex3d(0.5,0.5,-2);
		glNormal3f(-1, 0, 0);
		glVertex3d(-0.5,0.5,-1.8);
		glVertex3d(-0.5,0.75,-1.8);
		glVertex3d(-0.5,0.75,-2);
		glVertex3d(-0.5,0.5,-2);
		
		glNormal3f(0, 1, 1);
		glVertex3d(-0.5,0.75,-2);
		glVertex3d(-0.5,0.95,-1.8);
		glVertex3d(0.5,0.95,-1.8);
		glVertex3d(0.5,0.75,-2);
		
		glEnd();
		glBegin(GL_TRIANGLES);
		glColor3f(winColor.x, winColor.y, winColor.z);
		glNormal3f(1, 0, 0);
		glVertex3d(0.5,0.0,-0.75);
		glVertex3d(0.5,0.5,-0.75);
		glVertex3d(0.5,0.5,-1.25);
		glVertex3d(0.5,0.0,0.75);
		glVertex3d(0.5,0.5,0.75);
		glVertex3d(0.5,0.5,1.25);
		glColor3f(color.x, color.y, color.z);
		glVertex3d(0.5,0.75,1);
		glVertex3d(0.5,0.75,1.25);
		glVertex3d(0.5,0.95,1);
		glVertex3d(0.5,0.75,-1.8);
		glVertex3d(0.5,0.75,-2);
		glVertex3d(0.5,0.95,-1.8);
		
		glNormal3f(-1, 0, 0);
		glColor3f(winColor.x, winColor.y, winColor.z);
		glVertex3d(-0.5,0.0,-0.75);
		glVertex3d(-0.5,0.5,-0.75);
		glVertex3d(-0.5,0.5,-1.25);
		glVertex3d(-0.5,0.0,0.75);
		glVertex3d(-0.5,0.5,0.75);
		glVertex3d(-0.5,0.5,1.25);
		glColor3f(color.x, color.y, color.z);
		glVertex3d(-0.5,0.75,1);
		glVertex3d(-0.5,0.75,1.25);
		glVertex3d(-0.5,0.95,1);
		glVertex3d(-0.5,0.75,-1.8);
		glVertex3d(-0.5,0.75,-2);
		glVertex3d(-0.5,0.95,-1.8);
		
		glEnd();
		glPopMatrix();
	}
}
