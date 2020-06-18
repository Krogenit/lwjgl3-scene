package core;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector3f;

public class House
{
	public float x,y,z;
	public float size, height;
	public Vector3f color;
	
	
	public House(float x, float y, float z, float size, float height, Vector3f color)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
		this.height = height;
		this.color = color;
	}

	public void render()
	{
		glPushMatrix();
		glTranslatef(x, 1f, z);
		glColor3f(color.x, color.y, color.z);
		glBegin(GL_QUADS);
		glNormal3f(-1, 0, 0);
		glVertex3d(-size, 0, -size);
		glVertex3d(-size, 0, size);
		glVertex3d(-size, -height, size);
		glVertex3d(-size, -height, -size);
		glNormal3f(1, 0, 0);
		glVertex3d(size, 0, -size);
		glVertex3d(size, 0, size);
		glVertex3d(size, -height, size);
		glVertex3d(size, -height, -size);
		glNormal3f(0, 0, 1);
		glVertex3d(size, 0, size);
		glVertex3d(-size, 0, size);
		glVertex3d(-size, -height, size);
		glVertex3d(size, -height, size);
		glNormal3f(0, 0, -1);
		glVertex3d(size, 0, -size);
		glVertex3d(-size, 0, -size);
		glVertex3d(-size, -height, -size);
		glVertex3d(size, -height, -size);
		glNormal3f(0, -1, 0);
		glVertex3d(size, -height, size);
		glVertex3d(size, -height, -size);
		glVertex3d(-size, -height, -size);
		glVertex3d(-size, -height, size);
		
		glEnd();
		glPopMatrix();
	}
}
