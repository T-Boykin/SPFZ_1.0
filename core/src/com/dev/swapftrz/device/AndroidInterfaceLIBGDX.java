package com.dev.swapftrz.device;


/**
 * Interface is used to incorporate Android functions within the LIBGDX game lifecycle
 */
public interface AndroidInterfaceLIBGDX
{
	void toast(final String toast);

	void NEW_SPFZ_AD(String ADTYPE);
	
	void loadresources();
	
	void lockOrientation(final boolean unlock, String orientation);
	
	void adjustBrightness(final float brightness);

	int getBrightness();
	
	void getrotation();
	
	String getorientation();
	
	void setorientation(final String orient);
	
	void writeFile(final String s, boolean a);
	
	String readFile(final String File);

	void videocall();

	void hideAD();

	int getADattr();

	int visible();

	boolean loaded();
	
}
