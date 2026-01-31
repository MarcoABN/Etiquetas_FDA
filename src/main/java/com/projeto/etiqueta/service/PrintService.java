package com.projeto.etiqueta.service;

import com.projeto.etiqueta.model.PrintConfig;
import com.projeto.etiqueta.model.Product;
import org.springframework.stereotype.Service;

import javax.print.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrintService {

    public List<String> listarImpressorasDisponiveis() {
        List<String> nomes = new ArrayList<>();
        javax.print.PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (javax.print.PrintService service : services) {
            nomes.add(service.getName());
        }
        return nomes;
    }

    // Método Público para gerar o ZPL (Usado pelo Controller para o Preview)
    public String gerarZplPreview(Product p, String zplTemplate, PrintConfig config) {
        String zplContent = processarTemplate(p, zplTemplate);

        String ajustes = String.format("^MD%d^LT%d^LS%d^PW%d^LL%d^PR%d", 
                config.getDarkness(), 
                config.getOffsetY(), 
                config.getOffsetX(),
                config.getLabelWidth(),
                config.getLabelHeight(),
                config.getPrintSpeed());

        return zplContent.replace("^XA", "^XA" + ajustes);
    }

    public void imprimir(Product p, String zplTemplate, String impressoraNome, int quantidade, PrintConfig config) throws PrintException {
        // Usa o mesmo método de geração para garantir que o preview seja idêntico à impressão
        String zplContent = gerarZplPreview(p, zplTemplate, config);
        
        // Adiciona comando de quantidade
        zplContent = zplContent.replace("^XZ", "^PQ" + quantidade + "^XZ");

        javax.print.PrintService impressoraSelecionada = null;
        javax.print.PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        
        for (javax.print.PrintService service : services) {
            if (service.getName().equals(impressoraNome)) {
                impressoraSelecionada = service;
                break;
            }
        }

        if (impressoraSelecionada == null) {
            throw new RuntimeException("Impressora não encontrada.");
        }

        DocPrintJob job = impressoraSelecionada.createPrintJob();
        byte[] bytes = zplContent.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(bytes);
        Doc doc = new SimpleDoc(is, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        job.print(doc, null);
    }

    private String processarTemplate(Product p, String template) {
        String vitaminsList = buildVitaminsString(p);
        String allergensList = buildAllergensString(p);

        // LÓGICA DE NOME: Tenta Inglês, se não tiver, usa Português
        String nomeEtiqueta = (p.getProductNameEn() != null && !p.getProductNameEn().isEmpty()) 
                            ? p.getProductNameEn() 
                            : p.getProductName();

        return template
            .replace("{PRODUCT_NAME}", nomeEtiqueta)
            .replace("{SERVINGS_PER_CONTAINER}", p.getServingsPerContainer() != null ? p.getServingsPerContainer() : "")
            .replace("{SERVING_SIZE}", (p.getServingSizeQuantity() != null ? p.getServingSizeQuantity() : "") + " " + (p.getServingSizeUnit() != null ? p.getServingSizeUnit() : ""))
            .replace("{SERVING_WEIGHT}", p.getServingWeight() != null ? p.getServingWeight() : "")
            .replace("{CALORIES}", p.getCalories() != null ? p.getCalories() : "")
            .replace("{TOTAL_FAT}", p.getTotalFat() != null ? p.getTotalFat() : "")
            .replace("{TOTAL_FAT_DV}", p.getTotalFatDv() != null ? p.getTotalFatDv() : "")
            .replace("{SAT_FAT}", p.getSatFat() != null ? p.getSatFat() : "")
            .replace("{SAT_FAT_DV}", p.getSatFatDv() != null ? p.getSatFatDv() : "")
            .replace("{TRANS_FAT}", p.getTransFat() != null ? p.getTransFat() : "")
            .replace("{TRANS_FAT_DV}", p.getTransFatDv() != null ? p.getTransFatDv() : "")
            .replace("{SODIUM}", p.getSodium() != null ? p.getSodium() : "")
            .replace("{SODIUM_DV}", p.getSodiumDv() != null ? p.getSodiumDv() : "")
            .replace("{TOTAL_CARB}", p.getTotalCarb() != null ? p.getTotalCarb() : "")
            .replace("{TOTAL_CARB_DV}", p.getTotalCarbDv() != null ? p.getTotalCarbDv() : "")
            .replace("{FIBER}", p.getFiber() != null ? p.getFiber() : "")
            .replace("{FIBER_DV}", p.getFiberDv() != null ? p.getFiberDv() : "")
            .replace("{TOTAL_SUGARS}", p.getTotalSugars() != null ? p.getTotalSugars() : "")
            .replace("{ADDED_SUGARS}", p.getAddedSugars() != null ? p.getAddedSugars() : "")
            .replace("{ADDED_SUGARS_DV}", p.getAddedSugarsDv() != null ? p.getAddedSugarsDv() : "")
            .replace("{PROTEIN}", p.getProtein() != null ? p.getProtein() : "")
            .replace("{PROTEIN_DV}", p.getProteinDv() != null ? p.getProteinDv() : "")
            .replace("{CHOLESTEROL}", p.getCholesterol() != null ? p.getCholesterol() : "")
            .replace("{CHOLESTEROL_DV}", p.getCholesterolDv() != null ? p.getCholesterolDv() : "")
            
            .replace("{VITAMINS}", vitaminsList)
            .replace("{ALLERGENS}", allergensList)
            
            .replace("{POLY_FAT}", p.getPolyFat() != null ? p.getPolyFat() : "")
            .replace("{MONO_FAT}", p.getMonoFat() != null ? p.getMonoFat() : "")
            .replace("{SUGAR_ALCOHOL}", p.getSugarAlcohol() != null ? p.getSugarAlcohol() : "")
            .replace("{INGREDIENTS}", p.getIngredients() != null ? p.getIngredients() : "")
            .replace("{IMPORTED_BY}", p.getImportedBy() != null ? p.getImportedBy() : "")
            .replace("{DAILY_CALORIES}", "2000"); 
    }

    private String buildAllergensString(Product p) {
        StringBuilder sb = new StringBuilder();
        if (p.getAllergensContains() != null && !p.getAllergensContains().trim().isEmpty()) {
            sb.append("CONTAINS: ").append(p.getAllergensContains().toUpperCase()).append(". ");
        }
        if (p.getAllergensMayContain() != null && !p.getAllergensMayContain().trim().isEmpty()) {
            sb.append("MAY CONTAIN: ").append(p.getAllergensMayContain().toUpperCase()).append(".");
        }
        return sb.toString().trim();
    }

    private String buildVitaminsString(Product p) {
        List<String> list = new ArrayList<>();
        addIfPresent(list, "Vit. D", p.getVitaminD());
        addIfPresent(list, "Calcium", p.getCalcium());
        addIfPresent(list, "Iron", p.getIron());
        addIfPresent(list, "Potas.", p.getPotassium());
        addIfPresent(list, "Vit. A", p.getVitaminA());
        addIfPresent(list, "Vit. C", p.getVitaminC());
        addIfPresent(list, "Vit. E", p.getVitaminE());
        addIfPresent(list, "Vit. K", p.getVitaminK());
        addIfPresent(list, "Thiamin", p.getThiamin());
        addIfPresent(list, "Riboflavin", p.getRiboflavin());
        addIfPresent(list, "Niacin", p.getNiacin());
        addIfPresent(list, "Vit. B6", p.getVitaminB6());
        addIfPresent(list, "Folate", p.getFolate());
        addIfPresent(list, "Vit. B12", p.getVitaminB12());
        addIfPresent(list, "Biotin", p.getBiotin());
        addIfPresent(list, "Pantothenic Acid", p.getPantothenicAcid());
        addIfPresent(list, "Phosphorus", p.getPhosphorus());
        addIfPresent(list, "Iodine", p.getIodine());
        addIfPresent(list, "Magnesium", p.getMagnesium());
        addIfPresent(list, "Zinc", p.getZinc());
        addIfPresent(list, "Selenium", p.getSelenium());
        addIfPresent(list, "Copper", p.getCopper());
        addIfPresent(list, "Manganese", p.getManganese());
        addIfPresent(list, "Chromium", p.getChromium());
        addIfPresent(list, "Molybdenum", p.getMolybdenum());
        addIfPresent(list, "Chloride", p.getChloride());
        return String.join(" • ", list);
    }

    private void addIfPresent(List<String> list, String name, String value) {
        if (value != null && !value.trim().isEmpty()) {
            list.add(name + " " + value);
        }
    }
}