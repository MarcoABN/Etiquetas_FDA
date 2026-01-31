package com.projeto.etiqueta.service;

import com.projeto.etiqueta.model.PrintConfig;
import com.projeto.etiqueta.model.Product;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.print.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PrintService {

    private static final double DPI_CONVERSION = 8.0;

    private String carregarTemplateDoArquivo(String nomeArquivo) {
        try {
            ClassPathResource resource = new ClassPathResource("zpl/" + nomeArquivo);
            if (!resource.exists()) throw new RuntimeException("Template não encontrado: " + nomeArquivo);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler ZPL: " + e.getMessage());
        }
    }

    public List<String> listarImpressorasDisponiveis() {
        List<String> nomes = new ArrayList<>();
        javax.print.PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (javax.print.PrintService service : services) {
            nomes.add(service.getName());
        }
        return nomes;
    }

    public String gerarZplPreview(Product p, String nomeArquivoTemplate, PrintConfig config) {
        String templateConteudo = carregarTemplateDoArquivo(nomeArquivoTemplate);
        
        // 1. Substitui variáveis
        String zplContent = processarTemplate(p, templateConteudo);

        // 2. APLICA ESCALA MATEMÁTICA (Se for diferente de 100%)
        if (config.getScale() != null && config.getScale() != 1.0) {
            zplContent = aplicarEscalaInteligente(zplContent, config.getScale());
        }

        // 3. Configurações de Driver
        int widthDots = (int) Math.round(config.getWidthMm() * DPI_CONVERSION);
        int heightDots = (int) Math.round(config.getHeightMm() * DPI_CONVERSION);
        int topDots = (int) Math.round(config.getOffsetTopMm() * DPI_CONVERSION);
        int leftDots = (int) Math.round(config.getOffsetLeftMm() * DPI_CONVERSION);

        // ^PO: Print Orientation (Inverte a impressão se necessário)
        String orientacao = config.getOrientation() != null ? config.getOrientation() : "N";

        String ajustes = String.format("^MD%d^LT%d^LS%d^PW%d^LL%d^PR%s^PO%s", 
                config.getDarkness(), 
                topDots, 
                leftDots,
                widthDots,
                heightDots,
                config.getPrintSpeed(),
                orientacao); // Injeta a rotação

        return zplContent.replace("^XA", "^XA" + ajustes);
    }

    // --- ALGORITMO DE ESCALA DE ZPL ---
    private String aplicarEscalaInteligente(String zpl, double scale) {
        // Regex para capturar comandos numéricos comuns:
        // ^FOx,y | ^FTx,y | ^GBw,h,t | ^A0N,h,w | ^FBw,...
        
        // 1. Escalar Posições (^FO e ^FT)
        zpl = scaleCommand(zpl, "\\^(FO|FT)([0-9]+),([0-9]+)", scale, true);
        
        // 2. Escalar Caixas (^GB w,h,t)
        zpl = scaleCommand(zpl, "\\^(GB)([0-9]+),([0-9]+),([0-9]+)", scale, false);
        
        // 3. Escalar Fontes (^A0N,h,w ou ^A0R...)
        zpl = scaleCommand(zpl, "\\^(A[0-9A-Z])([A-Z]),([0-9]+),([0-9]+)", scale, false);
        
        // 4. Escalar Field Block (^FB width)
        // O FB é chato pois tem muitos parametros opcionais, vamos simplificar escalando o primeiro numero
        Pattern patternFB = Pattern.compile("\\^FB([0-9]+)");
        Matcher matcherFB = patternFB.matcher(zpl);
        StringBuffer sb = new StringBuffer();
        while (matcherFB.find()) {
            int val = Integer.parseInt(matcherFB.group(1));
            matcherFB.appendReplacement(sb, "^FB" + (int)Math.round(val * scale));
        }
        matcherFB.appendTail(sb);
        zpl = sb.toString();

        return zpl;
    }

    private String scaleCommand(String text, String regex, double scale, boolean isPosition) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        
        while (m.find()) {
            // Reconstrói o comando preservando o prefixo (Ex: ^FO)
            StringBuilder replacement = new StringBuilder("^" + m.group(1));
            
            // Para Fontes, o grupo 2 é a rotação (N,R...), pulamos ele na matemática
            int startIdx = 2;
            if (m.group(1).startsWith("A")) {
                replacement.append(m.group(2)).append(","); // Mantém a rotação da fonte
                startIdx = 3;
            }

            // Itera pelos grupos numéricos capturados
            for (int i = startIdx; i <= m.groupCount(); i++) {
                if (i > startIdx) replacement.append(","); // Adiciona vírgula entre numeros
                try {
                    int val = Integer.parseInt(m.group(i));
                    int scaledVal = (int) Math.round(val * scale);
                    replacement.append(scaledVal);
                } catch (NumberFormatException e) {
                    replacement.append(m.group(i)); // Mantém original se erro
                }
            }
            m.appendReplacement(sb, replacement.toString());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public void imprimir(Product p, String nomeArquivoTemplate, String impressoraNome, int quantidade, PrintConfig config) throws PrintException {
        String zplContent = gerarZplPreview(p, nomeArquivoTemplate, config);
        zplContent = zplContent.replace("^XZ", "^PQ" + quantidade + "^XZ");

        javax.print.PrintService impressoraSelecionada = null;
        javax.print.PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (javax.print.PrintService service : services) {
            if (service.getName().equals(impressoraNome)) {
                impressoraSelecionada = service;
                break;
            }
        }
        if (impressoraSelecionada == null) throw new RuntimeException("Impressora não encontrada.");

        DocPrintJob job = impressoraSelecionada.createPrintJob();
        byte[] bytes = zplContent.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(bytes);
        Doc doc = new SimpleDoc(is, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        job.print(doc, null);
    }

    private String processarTemplate(Product p, String template) {
        // ... (Mantenha o processarTemplate igual ao anterior) ...
        String vitaminsList = buildVitaminsString(p);
        String allergensList = buildAllergensString(p);
        String nomeEtiqueta = (p.getProductNameEn() != null && !p.getProductNameEn().isEmpty()) ? p.getProductNameEn() : p.getProductName();

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