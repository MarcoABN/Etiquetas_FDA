package com.projeto.etiqueta.model;

import lombok.Data;

@Data
public class PrintConfig {
    private int darkness = 15;      // Escuridão (0-30)
    private int printSpeed = 3;     // Velocidade
    private int offsetX = 0;        // Deslocamento Horizontal (mm ou dots)
    private int offsetY = 0;        // Deslocamento Vertical
    private int labelWidth = 800;   // Largura (dots) - 100mm ~= 800 dots
    private int labelHeight = 640;  // Altura (dots) - 80mm ~= 640 dots
	public int getDarkness() {
		return darkness;
	}
	public void setDarkness(int darkness) {
		this.darkness = darkness;
	}
	public int getPrintSpeed() {
		return printSpeed;
	}
	public void setPrintSpeed(int printSpeed) {
		this.printSpeed = printSpeed;
	}
	public int getOffsetX() {
		return offsetX;
	}
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	public int getOffsetY() {
		return offsetY;
	}
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	public int getLabelWidth() {
		return labelWidth;
	}
	public void setLabelWidth(int labelWidth) {
		this.labelWidth = labelWidth;
	}
	public int getLabelHeight() {
		return labelHeight;
	}
	public void setLabelHeight(int labelHeight) {
		this.labelHeight = labelHeight;
	}
    
    
}