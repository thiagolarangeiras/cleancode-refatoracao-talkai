# 1. Problemas Encontrados

## 1.1 Nomeclatura:
- Nomes em protugues e ingles sem nenhuma diferenciação observavel
- pluralidade e singularidade em nomes de variaveis e classes sem distinção para indentifica objetos como arrays e listas
- nomes  
    

# Estratégia para refatoração desses problemas

## Padrões de nomeclatura utilizados 
- Todas a classes e variaveis devem estar no singular, exceto quando são utilizadas para listas
- Todas a classes e variaveis devem estar em portugues, exceto para coisas muito expecificas de tecnologia, onde pode ser usado ingles

## Covenção de nomeação: 
- PascalCase para classes e tipos
- cameCase para variaveis, Funções e metodos

# Modificações:

## bc0ab1f -> rename: nomenclaturas de classes e packages

### Removido pluradidade de nomes como: 
    ExerciciosGpt -> ExercicioGpt
    ExercicioDtoGramaticaCompl  -> ExercicioGramaticaComplDto

### Removido mistura de linguagens como:
    /studyplan -> planoestudo

## 9702114, c3a55ef, afc4173 -> refactor: alterado estrutura e formatacao de trechos do codigo

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
