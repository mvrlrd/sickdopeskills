package ru.ibelykh.sickdopeskills.math;

import com.badlogic.gdx.math.Vector2;

public class Rect {
    public  Vector2 pos; //позиция по центру
    protected float halfWidth; // половина ширины
    protected float halfHeight; // половина высоты

    public Rect() {
        this.pos = new Vector2();
    }

    public Rect(Rect from) {
        this(from.pos.x, from.pos.y, from.getHalfWidth(), from.getHalfHeight());
    }

    public Rect(float x, float y, float halfWidth, float halfHeight) {
        this.pos = new Vector2();
        this.pos.set(x, y);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public float getLeft() {
        return this.pos.x - this.halfWidth;
    }

    public float getTop() {
        return this.pos.y + this.halfHeight;
    }

    public float getRight() {
        return this.pos.x + this.halfWidth;
    }

    public float getBottom() {
        return this.pos.y - this.halfHeight;
    }

    public float getHalfWidth() {
        return this.halfWidth;
    }

    public float getHalfHeight() {
        return this.halfHeight;
    }

    public float getWidth() {
        return this.halfWidth * 2.0F;
    }

    public float getHeight() {
        return this.halfHeight * 2.0F;
    }

    public void set(Rect from) {
        this.pos.set(from.pos);
        this.halfWidth = from.halfWidth;
        this.halfHeight = from.halfHeight;
    }

    public void setLeft(float left) {
        this.pos.x = left + this.halfWidth;
    }

    public void setTop(float top) {
        this.pos.y = top - this.halfHeight;
    }

    public void setRight(float right) {
        this.pos.x = right - this.halfWidth;
    }

    public void setBottom(float bottom) {
        this.pos.y = bottom + this.halfHeight;
    }

    public void setWidth(float width) {
        this.halfWidth = width / 2.0F;
    }

    public void setHeight(float height) {
        this.halfHeight = height / 2.0F;
    }

    public void setSize(float width, float height) {
        this.halfWidth = width / 2.0F;
        this.halfHeight = height / 2.0F;
    }

    public boolean isMe(Vector2 touch) {
        return touch.x >= this.getLeft() && touch.x <= this.getRight() && touch.y >= this.getBottom() && touch.y <= this.getTop();
    }

    public boolean isOutside(Rect other) {
        return this.getLeft() > other.getRight() || this.getRight() < other.getLeft() || this.getBottom() > other.getTop() || this.getTop() < other.getBottom();
    }

    public String toString() {
        return "Rectangle: pos" + this.pos + " size(" + this.getWidth() + ", " + this.getHeight() + ")";
    }

}