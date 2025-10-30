# 1 Problemas Encontrados

## 1.1 Nomenclatura:
- Nomes em português e inglês sem nenhuma diferenciação observável.  
- Pluralidade e singularidade em nomes de variáveis e classes sem distinção para identificar objetos como arrays e listas.

## 1.2 Estrutura do projeto e workflow:
- Projeto muito centralizado e inteiramente dependente do workflow e dos services do plano de estudo.

## 1.3 Tratamento de erros e exceções:
- Ausência de tratamento de erros ou uso de exceções ao identificar processos incorretos.

# 2 Estratégia para refatoração desses problemas

## 2.1 Padrões de nomenclatura utilizados:
- Todas as classes e variáveis devem estar no singular, exceto quando utilizadas para listas.  
- Todas as classes e variáveis devem estar em português, exceto para termos muito específicos de tecnologia, onde pode ser usado o inglês.

## 2.2 Convenção de nomeação:
- **PascalCase** para classes e tipos.  
- **camelCase** para variáveis, funções e métodos.

## 2.3 Padrões de Git:
- **Conventional Commit**: add, remove, refactor, etc.

# 3 Modificações:

## 3.1: bc0ab1f -> rename: nomenclaturas de classes e packages

### Removido pluradidade de nomes como: 
    ExerciciosGpt -> ExercicioGpt
    ExercicioDtoGramaticaCompl  -> ExercicioGramaticaComplDto

### Removido mistura de linguagens como:
    /studyplan -> planoestudo

## 3.2: 9702114, c3a55ef, afc4173 -> refactor: alterado estrutura e formatacao de trechos do codigo

### Alterado nomes de variaveis como:
    PlanoEstudoService service -> PlanoEstudoService planoEstudoService

### Ajustes de estrutura:
     public ResponseEntity<Object> getAll(
            @Nullable @RequestParam Integer page,
            @Nullable @RequestParam Integer count
    ) {}

    //para

    public ResponseEntity<Object> getAll(@Nullable @RequestParam Integer page,
                                         @Nullable @RequestParam Integer count) {}
