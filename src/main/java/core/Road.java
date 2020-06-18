package core;

import static org.lwjgl.opengl.GL11.*;

public class Road
{
	public float x, z;
	
	public Road(float x, float z)
	{
		this.x = x;
		this.z = z;
	}
	
	public void render()
	{
		float size = 2;
		glPushMatrix();
		glTranslatef(x, 0.7f, z);
		glColor3f(0.2f,0.2f,0.2f);
		glBegin(GL_QUADS);
		glNormal3f(0, -1, 0);
		glVertex3d(-size, 0, -size);
		glVertex3d(-size, 0, size);
		glVertex3d(size, 0, size);
		glVertex3d(size, 0, -size);
		glColor3f(0.8f,0.8f,0.8f);
		float y = -0.0001f;
		float x = -1.5f;
		float height = 0.05f;
		float width = 0.4f;
		float xOut = 1.25f;
		glNormal3f(0, -1, 0);
		glVertex3d(-height, y, x-width);
		glVertex3d(-height,y,x+ width);
		glVertex3d(height, y, x+width);
		glVertex3d(height, y, x-width);
		x+=xOut;
		glNormal3f(0, -1, 0);
		glVertex3d(-height, y, x-width);
		glVertex3d(-height, y, x+width);
		glVertex3d(height, y, x+width);
		glVertex3d(height, y, x-width);
		x+=xOut;
		glNormal3f(0, -1, 0);
		glVertex3d(-height, y, x-width);
		glVertex3d(-height, y, x+width);
		glVertex3d(height, y, x+width);
		glVertex3d(height, y, x-width);
		
		glEnd();
		glPopMatrix();
	}
}
