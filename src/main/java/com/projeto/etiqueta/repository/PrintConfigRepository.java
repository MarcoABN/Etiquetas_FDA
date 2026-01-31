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
            // Salva no formato: darkness;speed;offsetX;offsetY;width;height
            writer.write(config.getDarkness() + ";" +
                         config.getPrintSpeed() + ";" +
                         config.getOffsetX() + ";" +
                         config.getOffsetY() + ";" +
                         config.getLabelWidth() + ";" +
                         config.getLabelHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintConfig carregar() {
        PrintConfig config = new PrintConfig(); // Valores padrão
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(";");
                config.setDarkness(Integer.parseInt(parts[0]));
                config.setPrintSpeed(Integer.parseInt(parts[1]));
                config.setOffsetX(Integer.parseInt(parts[2]));
                config.setOffsetY(Integer.parseInt(parts[3]));
                config.setLabelWidth(Integer.parseInt(parts[4]));
                config.setLabelHeight(Integer.parseInt(parts[5]));
            }
        } catch (Exception e) {
            // Se der erro ou não existir arquivo, retorna o padrão
            System.out.println("Nenhuma configuração salva encontrada, usando padrão.");
        }
        return config;
    }
}