^XA
^PW912
^LL640
^LH0,0
^CI28

############################
# BORDAS GERAIS
############################
^FO20,10^GB415,620,3^FS
^FO470,10^GB420,620,3^FS

############################
# COLUNA ESQUERDA - NUTRITION FACTS
############################

# 1. Título (Fonte "Heavy" simulada: Largura 60 > Altura 54)
^FO35,20^A0N,54,60^FDNutrition Facts^FS

# 2. Primeira Barra
^FO20,75^GB415,3,3^FS

# 3. Nome do Produto
^FO35,85^A0N,30,30^FB390,1,0,L^FD{PRODUCT_NAME}^FS

# 4. Servings (Valor no final da frase)
^FO35,115^A0N,26,26^FB390,1,0,L^FDServings per container {SERVINGS_PER_CONTAINER}^FS

# 5. Serving Size (Aceita frações e usa 'Unit')
# Exemplo de preenchimento: {SERVING_SIZE}="1/6", {SERVING_WEIGHT}="145g"
^FO35,145^A0N,30,28^FB390,1,0,L^FDServing size {SERVING_SIZE} Unit ({SERVING_WEIGHT})^FS

# 6. Barra Grossa
^FO20,175^GB415,12,12^FS

# 7. Calorias
^FO35,195^A0N,24,24^FDAmount per serving^FS
^FO35,220^A0N,42,40^FDCalories^FS
^FO240,210^A0N,70,65^FB180,1,0,R^FD{CALORIES}^FS

# 8. Barra Média
^FO20,280^GB415,6,6^FS
^FO260,290^A0N,20,18^FB160,1,0,R^FD% Daily Value*^FS

# --- LISTA DE NUTRIENTES ---
# Coluna de Valor: X=230 (Alinhado à Direita, largura 100)
# Coluna de %DV:   X=340 (Alinhado à Direita, largura 80)

# Total Fat
^FO35,315^A0N,22,22^FDTotal Fat^FS
^FO230,315^A0N,22,22^FB100,1,0,R^FD{TOTAL_FAT}g^FS
^FO340,315^A0N,22,22^FB80,1,0,R^FD{TOTAL_FAT_DV}%^FS
^FO20,338^GB415,1,1^FS

# Saturated Fat (Indentado)
^FO55,342^A0N,22,22^FDSaturated Fat^FS
^FO230,342^A0N,22,22^FB100,1,0,R^FD{SAT_FAT}g^FS
^FO340,342^A0N,22,22^FB80,1,0,R^FD{SAT_FAT_DV}%^FS
^FO20,365^GB415,1,1^FS

# Trans Fat (Indentado - Sem %DV)
^FO55,369^A0N,22,22^FDTrans Fat^FS
^FO230,369^A0N,22,22^FB100,1,0,R^FD{TRANS_FAT}g^FS
^FO20,392^GB415,1,1^FS

# Cholesterol
^FO35,396^A0N,22,22^FDCholesterol^FS
^FO230,396^A0N,22,22^FB100,1,0,R^FD{CHOLESTEROL}mg^FS
^FO340,396^A0N,22,22^FB80,1,0,R^FD{CHOLESTEROL_DV}%^FS
^FO20,419^GB415,1,1^FS

# Sodium
^FO35,423^A0N,22,22^FDSodium^FS
^FO230,423^A0N,22,22^FB100,1,0,R^FD{SODIUM}mg^FS
^FO340,423^A0N,22,22^FB80,1,0,R^FD{SODIUM_DV}%^FS
^FO20,446^GB415,1,1^FS

# Total Carb
^FO35,450^A0N,22,22^FDTotal Carbohydrate^FS
^FO230,450^A0N,22,22^FB100,1,0,R^FD{TOTAL_CARB}g^FS
^FO340,450^A0N,22,22^FB80,1,0,R^FD{TOTAL_CARB_DV}%^FS
^FO20,473^GB415,1,1^FS

# Fiber (Indentado)
^FO55,477^A0N,22,22^FDDietary Fiber^FS
^FO230,477^A0N,22,22^FB100,1,0,R^FD{FIBER}g^FS
^FO340,477^A0N,22,22^FB80,1,0,R^FD{FIBER_DV}%^FS
^FO20,500^GB415,1,1^FS

# Total Sugars (Indentado)
^FO55,504^A0N,22,22^FDTotal Sugars^FS
^FO230,504^A0N,22,22^FB100,1,0,R^FD{TOTAL_SUGARS}g^FS
^FO20,527^GB415,1,1^FS

# Added Sugars (Indentado e Separado)
# Texto fixo à esquerda, Valor alinhado na coluna central
^FO75,531^A0N,22,22^FB220,1,0,L^FDIncludes Added Sugars^FS
^FO230,531^A0N,22,22^FB100,1,0,R^FD{ADDED_SUGARS}g^FS
^FO340,531^A0N,22,22^FB80,1,0,R^FD{ADDED_SUGARS_DV}%^FS
^FO20,554^GB415,1,1^FS

# Protein (Com %DV)
^FO35,558^A0N,22,22^FDProtein^FS
^FO230,558^A0N,22,22^FB100,1,0,R^FD{PROTEIN}g^FS
^FO340,558^A0N,22,22^FB80,1,0,R^FD{PROTEIN_DV}%^FS

# Barra Final
^FO20,582^GB415,6,6^FS

# Rodapé com Variável de Calorias Diárias
^FO30,592^A0N,12,12^FB400,4,0,L^FD*The % Daily Value (DV) tells you how much a nutrient in a serving of food contributes to a daily diet. {DAILY_CALORIES} calories a day is used for general nutrition advice.^FS

############################
# COLUNA DIREITA
############################

# Título Ingredients (Negrito Simulado)
^FO485,25^A0N,30,32^FDINGREDIENTS:^FS
^FO485,60^A0N,22,22^FB390,12,0,L^FD{INGREDIENTS}^FS

^FO485,340^A0N,30,32^FDALLERGENS:^FS
^FO485,375^A0N,22,22^FB390,5,0,L^FD{ALLERGENS}^FS

^FO485,500^A0N,24,22^FDIMPORTED BY:^FS
^FO485,530^A0N,20,20^FB390,4,0,L^FD{IMPORTED_BY}^FS

^XZ