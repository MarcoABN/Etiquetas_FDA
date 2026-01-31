package com.projeto.etiqueta.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.projeto.etiqueta.model.Product;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    private final String ARQUIVO = "produtos_cadastro.csv";

    public List<Product> listarTodos() {
        File file = new File(ARQUIVO);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            List<Product> lista = new CsvToBeanBuilder<Product>(reader)
                    .withType(Product.class)
                    .withSeparator(';')
                    .build()
                    .parse();
            return lista != null ? lista : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Product buscarPorId(String id) {
        if (id == null) return null;
        return listarTodos().stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElse(null);
    }

    public void salvar(Product produto) {
        List<Product> produtos = listarTodos();
        boolean atualizado = false;

        if (produto.getId() != null) {
            // 1. Tenta encontrar e atualizar na lista existente
            for (int i = 0; i < produtos.size(); i++) {
                Product pExistente = produtos.get(i);
                // Verifica se o ID bate (para atualizar)
                if (pExistente.getId() != null && pExistente.getId().equals(produto.getId())) {
                    produtos.set(i, produto); // Substitui o antigo pelo novo
                    atualizado = true;
                    break;
                }
            }
        }

        // 2. Se não encontrou (é novo ou não tinha ID), adiciona na lista
        if (!atualizado) {
            produtos.add(produto);
        }

        // 3. Reescreve o arquivo inteiro com a lista atualizada
        reescreverArquivo(produtos);
    }

    private void reescreverArquivo(List<Product> produtos) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(ARQUIVO), StandardCharsets.UTF_8)) {
            StatefulBeanToCsv<Product> sbc = new StatefulBeanToCsvBuilder<Product>(writer)
                    .withSeparator(';') 
                    .build();
            sbc.write(produtos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
    
    public Product buscarPorCodWinThor(String codWinThor) {
        if (codWinThor == null || codWinThor.isEmpty()) return null;
        return listarTodos().stream()
                .filter(p -> codWinThor.equals(p.getCodprod())) // Compara Strings
                .findFirst()
                .orElse(null);
    }
}