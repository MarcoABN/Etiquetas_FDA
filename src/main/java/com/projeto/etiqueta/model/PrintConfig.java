package com.projeto.etiqueta.model;

import lombok.Data;

@Data
public class PrintConfig {
    
    // Dimensões (Milímetros)
    private Double widthMm = 100.0;     // Padrão 100mm
    private Double heightMm = 150.0;    // Padrão 150mm

    // Ajustes de Posição (Milímetros)
    private Double offsetTopMm = 0.0;   // Topo (Y)
    private Double offsetLeftMm = 0.0;  // Esquerda (X)

    // Impressão
    private Integer darkness = 15;      // 0 a 30
    private String printSpeed = "3";    // Enviaremos o código ZPL (2,3,4,5,6) mas o front mostra mm/s
	
    private Double scale = 1.0;         // 1.0 = 100%, 0.5 = 50%
    private String orientation = "N";   // N=Normal, R=90º, I=180º, B=270º
    
    
    public Double getWidthMm() {
		return widthMm;
	}
	public void setWidthMm(Double widthMm) {
		this.widthMm = widthMm;
	}
	public Double getHeightMm() {
		return heightMm;
	}
	public void setHeightMm(Double heightMm) {
		this.heightMm = heightMm;
	}
	public Double getOffsetTopMm() {
		return offsetTopMm;
	}
	public void setOffsetTopMm(Double offsetTopMm) {
		this.offsetTopMm = offsetTopMm;
	}
	public Double getOffsetLeftMm() {
		return offsetLeftMm;
	}
	public void setOffsetLeftMm(Double offsetLeftMm) {
		this.offsetLeftMm = offsetLeftMm;
	}
	public Integer getDarkness() {
		return darkness;
	}
	public void setDarkness(Integer darkness) {
		this.darkness = darkness;
	}
	public String getPrintSpeed() {
		return printSpeed;
	}
	public void setPrintSpeed(String printSpeed) {
		this.printSpeed = printSpeed;
	}
	public Double getScale() {
		return scale;
	}
	public void setScale(Double scale) {
		this.scale = scale;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	
	

    

}