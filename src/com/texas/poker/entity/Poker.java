
package com.texas.poker.entity;

import java.io.Serializable;

import com.texas.poker.util.PokerUtil;

/*
 * author zkzhou
 * description 
 * time 2014-7-29
 *
 */
public class Poker implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int color;

    private int size;

    private int number;

    private int PokerImageId;

    public Poker() {

    }
    public Poker(int number) {
        this.number = number;
        this.color = number / 13;
        this.size = number % 13;
        PokerImageId = PokerUtil.pokersImg[number];
    }

    public Poker(Poker p) {
        this.number = p.getNumber();
        this.color = p.getColor();
        this.size = p.getSize();
        PokerImageId = PokerUtil.pokersImg[this.number];
    }

    public void setPoker(Poker p) {
        this.number = p.getNumber();
        this.color = p.getColor();
        this.size = p.getSize();
        PokerImageId = PokerUtil.pokersImg[this.number];
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public int getPokerImageId() {
        return PokerImageId;
    }

    public void setPokerImageId(int pokerImageId) {
        PokerImageId = pokerImageId;
    }

    @Override
    public boolean equals(Object o) {
        // TODO Auto-generated method stub
        Poker s = (Poker) o;
        if (s.getNumber() == this.getNumber()) {
            return true;
        } else {
            return false;
        }
        // return super.equals(o);
    }

}
