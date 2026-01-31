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

    private final String ZPL_TEMPLATE = "^XA\n" +
            "^CI28\n" +
            "^FO20,20^A0N,35,35^FB760,2,0,C^FD{PRODUCT_NAME}^FS\n" +
            "^FO20,100^GB760,600,3^FS\n" + 
            
            "^FO30,120^A0N,40,40^FDNutrition Facts^FS\n" +
            "^FO30,170^A0N,25,25^FDServing size {SERVING_SIZE}^FS\n" +
            "^FO20,200^GB760,5,5^FS\n" +
            
            "^FO30,210^A0N,30,30^FDCalories^FS\n" +
            "^FO600,210^A0N,50,50^FD{CALORIES}^FS\n" +
            "^FO20,270^GB760,3,3^FS\n" +
            
            "^FO30,290^A0N,22,22^FDTotal Fat {TOTAL_FAT}g^FS\n" +
            "^FO30,320^A0N,22,22^FDSat. Fat {SAT_FAT}g^FS\n" +
            "^FO30,350^A0N,22,22^FDTrans Fat {TRANS_FAT}g^FS\n" +
            "^FO30,380^A0N,22,22^FDCholest. {CHOLESTEROL}mg^FS\n" +
            "^FO30,410^A0N,22,22^FDSodium {SODIUM}mg^FS\n" +
            "^FO30,440^A0N,22,22^FDTotal Carb. {TOTAL_CARB}g^FS\n" +
            "^FO30,470^A0N,22,22^FDFiber {FIBER}g^FS\n" +
            "^FO30,500^A0N,22,22^FDSugars {TOTAL_SUGARS}g^FS\n" +
            "^FO30,530^A0N,22,22^FDProtein {PROTEIN}g^FS\n" +
            
            "^FO20,560^GB760,3,3^FS\n" +
            "^FO30,575^A0N,22,22^FB740,2,0,L^FD{VITAMINS}^FS\n" +
            
            "^FO20,640^A0N,18,18^FB760,2,0,L^FD* The % Daily Value tells you... (Base {DAILY_CALORIES} kcal)^FS\n" +
            
            // INGREDIENTES
            "^FO20,710^A0N,20,20^FB760,4,0,L^FDING: {INGREDIENTS}^FS\n" +
            
            // ALERGÊNICOS (Sem prefixo fixo)
            "^FO20,800^A0N,20,20^FB760,2,0,L^FD{ALLERGENS}^FS\n" +
            
            "^FO20,860^A0N,20,20^FB760,3,0,L^FD{IMPORTED_BY}^FS\n" +
            "^XZ";

    @GetMapping("/novo")
    public String exibirForm(Model model) {
        model.addAttribute("product", new Product());
        return "cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Product product, RedirectAttributes attributes) {
        try {
            repository.salvar(product);
            attributes.addFlashAttribute("sucesso", "Produto salvo!");
            return "redirect:/produtos/lista";
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar: " + e.getMessage());
            return "redirect:/produtos/novo";
        }
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("produtos", repository.listarTodos());
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

        // --- NOVIDADE: GERA O ZPL PARA PREVIEW ---
        if (produto != null) {
            String zplPreview = printService.gerarZplPreview(produto, ZPL_TEMPLATE, config);
            // Escapa aspas para não quebrar o JS
            model.addAttribute("zplRaw", zplPreview);
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
                printService.imprimir(produto, ZPL_TEMPLATE, impressora, quantidade, config);
                attributes.addFlashAttribute("sucesso", "Enviado para impressão!");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
        }
        return "redirect:/produtos/lista";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        Product produto = repository.buscarPorId(id);
        if (produto == null) {
            return "redirect:/produtos/lista"; // Se não achar, volta pra lista
        }
        model.addAttribute("product", produto);
        return "cadastro"; // Reutiliza a tela de cadastro, mas preenchida!
    }
    
    
}