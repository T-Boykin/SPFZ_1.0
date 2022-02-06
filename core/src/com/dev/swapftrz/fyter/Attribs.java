package com.dev.swapftrz.fyter;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;

public interface Attribs
{
	boolean attacking();
	boolean attacked();
	boolean isplayerone();
	boolean getboxconfirm();
	boolean invul();
	boolean hit();
	void hitboxconfirm(boolean confirm);
	
	int[] activeframes();
	int combonum();
	
	float center();
	void setcombonum(int combo);
	int currentframe();
	
	SpriteAnimationComponent animationcomponent();

	SpriteAnimationStateComponent animationstate();
	
	TransformComponent attributes();
	
	DimensionsComponent dimensions();
	
	Rectangle setcharbox();
	Rectangle setrect();
	Rectangle setcross();
	Rectangle sethitbox();
	Rectangle setrflbox();
	
	ShapeRenderer drawcharbox();
	ShapeRenderer drawrect();
	ShapeRenderer drawhitbox();
	ShapeRenderer drawrflbox();
	
	Vector2 moveandjump();
	Vector2 hitboxsize();
	Vector2 hitboxpos();
}	