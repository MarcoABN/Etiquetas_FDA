package com.projeto.etiqueta.controller;

import com.projeto.etiqueta.model.PrintConfig;
import com.projeto.etiqueta.model.Product;
import com.projeto.etiqueta.repository.PrintConfigRepository;
import com.projeto.etiqueta.repository.ProductRepository;
import com.projeto.etiqueta.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.stream.Collectors;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private PrintService printService;

    @Autowired
    private PrintConfigRepository configRepository;

    // AQUI ESTÁ A MUDANÇA: Nome do arquivo ao invés do código fixo
    private final String NOME_ARQUIVO_TEMPLATE = "label_fda_base.zpl";

    @GetMapping("/novo")
    public String exibirForm(Model model) {
        model.addAttribute("product", new Product());
        return "cadastro";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        Product produto = repository.buscarPorId(id);
        if (produto == null) {
            return "redirect:/produtos/lista";
        }
        model.addAttribute("product", produto);
        return "cadastro"; 
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Product product, RedirectAttributes attributes) {
        try {
            repository.salvar(product);
            attributes.addFlashAttribute("sucesso", "Produto salvo com sucesso!");
            return "redirect:/produtos/lista";
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar: " + e.getMessage());
            return "redirect:/produtos/novo";
        }
    }

    @GetMapping("/lista")
    public String listar(@RequestParam(required = false) String termo, Model model) {
        List<Product> todosProdutos = repository.listarTodos();
        
        if (termo != null && !termo.trim().isEmpty()) {
            String termoLower = termo.toLowerCase().trim();
            
            List<Product> filtrados = todosProdutos.stream()
                .filter(p -> 
                    // Verifica Código WinThor (se existir)
                    (p.getCodprod() != null && p.getCodprod().toLowerCase().contains(termoLower)) ||
                    // Verifica Nome PT (se existir)
                    (p.getProductName() != null && p.getProductName().toLowerCase().contains(termoLower)) ||
                    // Verifica Nome EN (se existir)
                    (p.getProductNameEn() != null && p.getProductNameEn().toLowerCase().contains(termoLower))
                )
                .collect(Collectors.toList());
                
            model.addAttribute("produtos", filtrados);
            model.addAttribute("termoAtivo", termo); // Para manter o texto no input
        } else {
            model.addAttribute("produtos", todosProdutos);
        }
        
        return "lista";
    }

    @GetMapping("/impressao")
    public String telaImpressao(@RequestParam(required = false) String id,
                                @RequestParam(required = false) String buscaCod,
                                Model model) {
        
        Product produto = null;

        if (id != null && !id.isEmpty()) {
            produto = repository.buscarPorId(id);
        } else if (buscaCod != null && !buscaCod.isEmpty()) {
            produto = repository.buscarPorCodWinThor(buscaCod);
            if (produto == null) {
                model.addAttribute("erro", "Produto com código WinThor '" + buscaCod + "' não encontrado.");
            }
        }

        List<String> impressoras = printService.listarImpressorasDisponiveis();
        PrintConfig config = configRepository.carregar();

        model.addAttribute("product", produto);
        model.addAttribute("impressoras", impressoras);
        model.addAttribute("config", config);

        if (produto != null) {
            try {
                // Passa o NOME DO ARQUIVO para gerar o preview
                String zplPreview = printService.gerarZplPreview(produto, NOME_ARQUIVO_TEMPLATE, config);
                model.addAttribute("zplRaw", zplPreview);
            } catch (Exception e) {
                model.addAttribute("erro", "Erro ao carregar layout ZPL: " + e.getMessage());
            }
        }
        
        return "impressao";
    }

    @PostMapping("/executar-impressao")
    public String executarImpressao(@RequestParam String id, 
                                    @RequestParam String impressora, 
                                    @RequestParam int quantidade,
                                    @ModelAttribute PrintConfig config,
                                    RedirectAttributes attributes) {
        try {
            Product produto = repository.buscarPorId(id);
            configRepository.salvar(config); 
            
            if (produto != null) {
                printService.imprimir(produto, NOME_ARQUIVO_TEMPLATE, impressora, quantidade, config);
                attributes.addFlashAttribute("sucesso", "Etiqueta enviada para impressão! Próximo...");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
        }
        // REDIRECIONA PARA A MESMA TELA (SEM ID), LIMPANDO O FORMULÁRIO
        return "redirect:/produtos/impressao"; 
    }
    
    @PostMapping("/salvar-config")
    public String salvarConfiguracao(@RequestParam String id,
                                     @ModelAttribute PrintConfig config,
                                     RedirectAttributes attributes) {
        try {
            configRepository.salvar(config);
            attributes.addFlashAttribute("sucesso", "Ajustes aplicados! Preview atualizado.");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar config: " + e.getMessage());
        }
        // Redireciona de volta para a mesma tela, forçando a regeneração do ZPL com as novas medidas
        return "redirect:/produtos/impressao?id=" + id;
    }
}