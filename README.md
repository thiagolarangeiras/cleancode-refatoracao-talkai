# 1 Sobre

## 1.1 Descrição

## 1.2 Funcionalidades

## 1.3 Instalação

## 1.4 Execução

# 2 Refatoração

## 2.1 Problemas detectados

### 2.1.1 Nomenclatura:
- Nomes em português e inglês sem nenhuma diferenciação observável.  
- Pluralidade e singularidade em nomes de variáveis e classes sem distinção para identificar objetos como arrays e listas.

### 2.1.2 Estrutura do projeto e workflow:
- Projeto muito centralizado e inteiramente dependente do workflow e dos services do plano de estudo.

### 2.1.3 Tratamento de erros e exceções:
- Ausência de tratamento de erros ou uso de exceções ao identificar processos incorretos.

## 2.2 Estrategias de refatoração

### 2.2.1 Padrões de nomenclatura utilizados:
- Todas as classes e variáveis devem estar no singular, exceto quando utilizadas para listas.  
- Todas as classes e variáveis devem estar em português, exceto para termos muito específicos de tecnologia, onde pode ser usado o inglês.

## 2.2.2 Convenção de nomeação:
- **PascalCase** para classes e tipos.  
- **camelCase** para variáveis, funções e métodos.

## 2.2.3 Padrões de Git:
- **Conventional Commit**: add, remove, refactor, etc.

## 2.3 Testes

## 2.3 Interface fluente

# 3 Integrantes
- [IURI DE LIMA MARQUES](https://github.com/iurilimamarques)    
- [THIAGO LARANGEIRA DE SOUZA](https://github.com/thiagolarangeiras)
