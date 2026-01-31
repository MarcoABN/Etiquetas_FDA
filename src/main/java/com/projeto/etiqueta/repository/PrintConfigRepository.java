package com.projeto.etiqueta.repository;

import com.projeto.etiqueta.model.PrintConfig;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Repository
public class PrintConfigRepository {

    private final String CONFIG_FILE = "config_impressora.csv";

    public void salvar(PrintConfig config) {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            // Formato: darkness;speed;offsetLeft;offsetTop;width;height;scale;orientation
            writer.write(config.getDarkness() + ";" +
                         config.getPrintSpeed() + ";" +
                         config.getOffsetLeftMm() + ";" +
                         config.getOffsetTopMm() + ";" +
                         config.getWidthMm() + ";" +
                         config.getHeightMm() + ";" +
                         config.getScale() + ";" +
                         config.getOrientation());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintConfig carregar() {
        PrintConfig config = new PrintConfig();
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(";");
                if (parts.length >= 6) {
                    config.setDarkness(Integer.parseInt(parts[0]));
                    config.setPrintSpeed(parts[1]); 
                    config.setOffsetLeftMm(Double.parseDouble(parts[2]));
                    config.setOffsetTopMm(Double.parseDouble(parts[3]));
                    config.setWidthMm(Double.parseDouble(parts[4]));
                    config.setHeightMm(Double.parseDouble(parts[5]));
                }
                // Novos campos (proteção para CSV antigo)
                if (parts.length >= 8) {
                    config.setScale(Double.parseDouble(parts[6]));
                    config.setOrientation(parts[7]);
                }
            }
        } catch (Exception e) {
            System.out.println("Configuração não encontrada, usando padrão.");
        }
        return config;
    }
}