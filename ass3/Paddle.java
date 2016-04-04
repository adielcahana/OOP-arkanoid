import java.awt.Color;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

public class Paddle implements Sprite, Collidable {
	private biuoop.KeyboardSensor keyboard;
	private Rectangle shape;
	private Color color;

	public Paddle(Point upperLeft, double width, double height, Color color, KeyboardSensor keyboard) {
		this.keyboard = keyboard;
		this.shape = new Rectangle(upperLeft, width, height);
		this.color = color;
	}

	public void moveLeft() {
		double x  = this.shape.getUpperLeft().getX();
		double y  = this.shape.getUpperLeft().getY();
		double width = this.shape.getWidth();
		double height = this.shape.getHeight();
		double leftBoundry = 0;
		this.shape = new Rectangle(new Point(x - 5, y), width, height);
		if (x - 5 > leftBoundry) {
			this.shape = new Rectangle(new Point(x - 5, y), width, height);
		} else {
			this.shape = new Rectangle(new Point(leftBoundry, y), width, height);
		}
	}

	public void moveRight() {
		double x  = this.shape.getUpperLeft().getX();
		double y  = this.shape.getUpperLeft().getY();
		double width = this.shape.getWidth();
		double height = this.shape.getHeight();
		double rightBoundry = 380;
		this.shape = new Rectangle(new Point(x + 5, y), width, height);
		if (x + 5 < rightBoundry) {
			this.shape = new Rectangle(new Point(x + 5, y), width, height);
		} else {
			this.shape = new Rectangle(new Point(rightBoundry, y), width, height);
		}
	}

	// Sprite
	public void timePassed() {
		if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)){
			System.out.println("the 'left arrow' key is pressed");
			this.moveLeft();
		}
		else if(keyboard.isPressed(KeyboardSensor.RIGHT_KEY)){
			System.out.println("the 'right arrow' key is pressed");
			this.moveRight();
		}
	}

	// Collidable
	public Rectangle getCollisionRectangle() {
		return this.shape;
	}

	public Velocity hit(Point collisionPoint, Velocity currentVelocity) {	
		final int Left = 0;
		final int LeftMiddle = 1;
		final int Middle = 2;
		final int RightMiddle = 3;
		final int Right = 4;
		double dx = currentVelocity.getDx();
		double dy = currentVelocity.getDy();
		double speed = Math.sqrt(dx * dx + dy * dy);
		Velocity newVelocity = null;
		int hitPlace = this.shape.divideRectangle(collisionPoint);
		switch(hitPlace) {
		case Left:
			newVelocity = Velocity.fromAngleAndSpeed(300, speed);
			break;
		case LeftMiddle:
			newVelocity = Velocity.fromAngleAndSpeed(330, speed);
			break;
		case Middle:
			newVelocity = Velocity.fromAngleAndSpeed(0, speed);
			break;
		case RightMiddle:
			newVelocity = Velocity.fromAngleAndSpeed(30, speed);
			break;
		case Right:
			newVelocity = Velocity.fromAngleAndSpeed(60, speed);
			break;
		default:
			System.out.println("Error: no velocity");
		}
		return newVelocity;
	}

	// Add this paddle to the game.
	public void addToGame(Game game){
		game.addSprite(this);
		game.addCollidable(this);
	}

	public void drawOn(DrawSurface surface) {
		surface.setColor(this.color);
		this.shape.drawOn(surface);
	}
}