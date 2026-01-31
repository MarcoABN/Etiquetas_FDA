package com.projeto.etiqueta.model;

import lombok.Data;
import java.util.UUID;

@Data
public class Product {
    
    // Identificação
    private String id = UUID.randomUUID().toString();
    private String codprod; 
    private String productName; 
    private String productNameEn;
    
    // Porções
    private String servingsPerContainer;
    private String servingWeight; 
    private String servingSizeQuantity; 
    private String servingSizeUnit; 

    // Macronutrientes
    private String calories;
    private String totalCarb;
    private String totalCarbDv;
    private String totalSugars;
    private String addedSugars;
    private String addedSugarsDv;
    private String sugarAlcohol;
    
    private String protein;
    private String proteinDv;
    
    private String totalFat;
    private String totalFatDv;
    private String satFat;
    private String satFatDv;
    private String transFat;
    private String transFatDv;
    private String polyFat;
    private String monoFat;
    
    private String fiber;
    private String fiberDv;
    private String sodium;
    private String sodiumDv;
    private String cholesterol = "0";
    private String cholesterolDv;

    // --- MICRONUTRIENTES ---
    private String vitaminD;
    private String calcium;
    private String iron;
    private String potassium;
    private String vitaminA;
    private String vitaminC;
    private String vitaminE;
    private String vitaminK;
    private String thiamin;
    private String riboflavin;
    private String niacin;
    private String vitaminB6;
    private String folate;
    private String vitaminB12;
    private String biotin;
    private String pantothenicAcid;
    private String phosphorus;
    private String iodine;
    private String magnesium;
    private String zinc;
    private String selenium;
    private String copper;
    private String manganese;
    private String chromium;
    private String molybdenum;
    private String chloride;

    // --- COMPOSIÇÃO ---
    private String ingredients;
    
    // Novos campos separados de alergênicos
    private String allergensContains;    // Ex: Milk, Soy
    private String allergensMayContain;  // Ex: Peanuts

    // Fixo
    private final String importedBy = "IMPORTED BY:\nGO MINAS DISTRIBUTION LLC\n2042 NW 55TH AVE\nMARGATE, FL33063";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodprod() {
		return codprod;
	}

	public void setCodprod(String codprod) {
		this.codprod = codprod;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getServingsPerContainer() {
		return servingsPerContainer;
	}

	public void setServingsPerContainer(String servingsPerContainer) {
		this.servingsPerContainer = servingsPerContainer;
	}

	public String getServingWeight() {
		return servingWeight;
	}

	public void setServingWeight(String servingWeight) {
		this.servingWeight = servingWeight;
	}

	public String getServingSizeQuantity() {
		return servingSizeQuantity;
	}

	public void setServingSizeQuantity(String servingSizeQuantity) {
		this.servingSizeQuantity = servingSizeQuantity;
	}

	public String getServingSizeUnit() {
		return servingSizeUnit;
	}

	public void setServingSizeUnit(String servingSizeUnit) {
		this.servingSizeUnit = servingSizeUnit;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

	public String getTotalCarb() {
		return totalCarb;
	}

	public void setTotalCarb(String totalCarb) {
		this.totalCarb = totalCarb;
	}

	public String getTotalCarbDv() {
		return totalCarbDv;
	}

	public void setTotalCarbDv(String totalCarbDv) {
		this.totalCarbDv = totalCarbDv;
	}

	public String getTotalSugars() {
		return totalSugars;
	}

	public void setTotalSugars(String totalSugars) {
		this.totalSugars = totalSugars;
	}

	public String getAddedSugars() {
		return addedSugars;
	}

	public void setAddedSugars(String addedSugars) {
		this.addedSugars = addedSugars;
	}

	public String getAddedSugarsDv() {
		return addedSugarsDv;
	}

	public void setAddedSugarsDv(String addedSugarsDv) {
		this.addedSugarsDv = addedSugarsDv;
	}

	public String getSugarAlcohol() {
		return sugarAlcohol;
	}

	public void setSugarAlcohol(String sugarAlcohol) {
		this.sugarAlcohol = sugarAlcohol;
	}

	public String getProtein() {
		return protein;
	}

	public void setProtein(String protein) {
		this.protein = protein;
	}

	public String getProteinDv() {
		return proteinDv;
	}

	public void setProteinDv(String proteinDv) {
		this.proteinDv = proteinDv;
	}

	public String getTotalFat() {
		return totalFat;
	}

	public void setTotalFat(String totalFat) {
		this.totalFat = totalFat;
	}

	public String getTotalFatDv() {
		return totalFatDv;
	}

	public void setTotalFatDv(String totalFatDv) {
		this.totalFatDv = totalFatDv;
	}

	public String getSatFat() {
		return satFat;
	}

	public void setSatFat(String satFat) {
		this.satFat = satFat;
	}

	public String getSatFatDv() {
		return satFatDv;
	}

	public void setSatFatDv(String satFatDv) {
		this.satFatDv = satFatDv;
	}

	public String getTransFat() {
		return transFat;
	}

	public void setTransFat(String transFat) {
		this.transFat = transFat;
	}

	public String getTransFatDv() {
		return transFatDv;
	}

	public void setTransFatDv(String transFatDv) {
		this.transFatDv = transFatDv;
	}

	public String getPolyFat() {
		return polyFat;
	}

	public void setPolyFat(String polyFat) {
		this.polyFat = polyFat;
	}

	public String getMonoFat() {
		return monoFat;
	}

	public void setMonoFat(String monoFat) {
		this.monoFat = monoFat;
	}

	public String getFiber() {
		return fiber;
	}

	public void setFiber(String fiber) {
		this.fiber = fiber;
	}

	public String getFiberDv() {
		return fiberDv;
	}

	public void setFiberDv(String fiberDv) {
		this.fiberDv = fiberDv;
	}

	public String getSodium() {
		return sodium;
	}

	public void setSodium(String sodium) {
		this.sodium = sodium;
	}

	public String getSodiumDv() {
		return sodiumDv;
	}

	public void setSodiumDv(String sodiumDv) {
		this.sodiumDv = sodiumDv;
	}

	public String getCholesterol() {
		return cholesterol;
	}

	public void setCholesterol(String cholesterol) {
		this.cholesterol = cholesterol;
	}

	public String getCholesterolDv() {
		return cholesterolDv;
	}

	public void setCholesterolDv(String cholesterolDv) {
		this.cholesterolDv = cholesterolDv;
	}

	public String getVitaminD() {
		return vitaminD;
	}

	public void setVitaminD(String vitaminD) {
		this.vitaminD = vitaminD;
	}

	public String getCalcium() {
		return calcium;
	}

	public void setCalcium(String calcium) {
		this.calcium = calcium;
	}

	public String getIron() {
		return iron;
	}

	public void setIron(String iron) {
		this.iron = iron;
	}

	public String getPotassium() {
		return potassium;
	}

	public void setPotassium(String potassium) {
		this.potassium = potassium;
	}

	public String getVitaminA() {
		return vitaminA;
	}

	public void setVitaminA(String vitaminA) {
		this.vitaminA = vitaminA;
	}

	public String getVitaminC() {
		return vitaminC;
	}

	public void setVitaminC(String vitaminC) {
		this.vitaminC = vitaminC;
	}

	public String getVitaminE() {
		return vitaminE;
	}

	public void setVitaminE(String vitaminE) {
		this.vitaminE = vitaminE;
	}

	public String getVitaminK() {
		return vitaminK;
	}

	public void setVitaminK(String vitaminK) {
		this.vitaminK = vitaminK;
	}

	public String getThiamin() {
		return thiamin;
	}

	public void setThiamin(String thiamin) {
		this.thiamin = thiamin;
	}

	public String getRiboflavin() {
		return riboflavin;
	}

	public void setRiboflavin(String riboflavin) {
		this.riboflavin = riboflavin;
	}

	public String getNiacin() {
		return niacin;
	}

	public void setNiacin(String niacin) {
		this.niacin = niacin;
	}

	public String getVitaminB6() {
		return vitaminB6;
	}

	public void setVitaminB6(String vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}

	public String getFolate() {
		return folate;
	}

	public void setFolate(String folate) {
		this.folate = folate;
	}

	public String getVitaminB12() {
		return vitaminB12;
	}

	public void setVitaminB12(String vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}

	public String getBiotin() {
		return biotin;
	}

	public void setBiotin(String biotin) {
		this.biotin = biotin;
	}

	public String getPantothenicAcid() {
		return pantothenicAcid;
	}

	public void setPantothenicAcid(String pantothenicAcid) {
		this.pantothenicAcid = pantothenicAcid;
	}

	public String getPhosphorus() {
		return phosphorus;
	}

	public void setPhosphorus(String phosphorus) {
		this.phosphorus = phosphorus;
	}

	public String getIodine() {
		return iodine;
	}

	public void setIodine(String iodine) {
		this.iodine = iodine;
	}

	public String getMagnesium() {
		return magnesium;
	}

	public void setMagnesium(String magnesium) {
		this.magnesium = magnesium;
	}

	public String getZinc() {
		return zinc;
	}

	public void setZinc(String zinc) {
		this.zinc = zinc;
	}

	public String getSelenium() {
		return selenium;
	}

	public void setSelenium(String selenium) {
		this.selenium = selenium;
	}

	public String getCopper() {
		return copper;
	}

	public void setCopper(String copper) {
		this.copper = copper;
	}

	public String getManganese() {
		return manganese;
	}

	public void setManganese(String manganese) {
		this.manganese = manganese;
	}

	public String getChromium() {
		return chromium;
	}

	public void setChromium(String chromium) {
		this.chromium = chromium;
	}

	public String getMolybdenum() {
		return molybdenum;
	}

	public void setMolybdenum(String molybdenum) {
		this.molybdenum = molybdenum;
	}

	public String getChloride() {
		return chloride;
	}

	public void setChloride(String chloride) {
		this.chloride = chloride;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getAllergensContains() {
		return allergensContains;
	}

	public void setAllergensContains(String allergensContains) {
		this.allergensContains = allergensContains;
	}

	public String getAllergensMayContain() {
		return allergensMayContain;
	}

	public void setAllergensMayContain(String allergensMayContain) {
		this.allergensMayContain = allergensMayContain;
	}

	public String getImportedBy() {
		return importedBy;
	}

	public String getProductNameEn() {
		return productNameEn;
	}

	public void setProductNameEn(String productNameEn) {
		this.productNameEn = productNameEn;
	}

    
    
}
