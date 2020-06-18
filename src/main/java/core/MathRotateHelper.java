package core;

import org.joml.Vector3f;

public class MathRotateHelper
{
	public static void rotateAboutY(Vector3f vec, double a)
	{
		float newX = (float) (Math.cos(a) * vec.x + Math.sin(a) * vec.z);
		vec.z = (float) (-Math.sin(a) * vec.x + Math.cos(a) * vec.z);
		vec.x = newX;
	}

	public static void rotateAboutX(Vector3f vec, double a)
	{
		float newY = (float) (Math.cos(a) * vec.y - Math.sin(a) * vec.z);
		vec.z = (float) (Math.sin(a) * vec.y + Math.cos(a) * vec.z);
		vec.y = newY;
	}

	public static void rotateAboutZ(Vector3f vec, double a)
	{
		float newX = (float) (Math.cos(a) * vec.x - Math.sin(a) * vec.y);
		vec.y = (float) (Math.sin(a) * vec.x + Math.cos(a) * vec.y);
		vec.x = newX;
	}
}
