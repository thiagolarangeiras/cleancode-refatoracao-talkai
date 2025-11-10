# 1 Sobre

## 1.1 Descrição

## 1.2 Funcionalidades

## 1.3 Instalação

### Requesitos:
- JDK 23, 24 ou 25
- InteliJ
- Postgresql
- Docker
- Conta OpenAI com chave de API
- Ultima Versão desse codigo fonte

## 1.4 Execução

### 1.4.1 Java/Backend

#### Nativa terminal:
Rodar os comandos a seguir na base do projeto
```
./gradlew bootRun
```

#### Nativa InteliJ:
Abrir o arquivo `AiApplication.java` clicar no botão de rodar

#### Docker:
```
./gradlew clean
./gradlew build
docker build --build-arg JAR_FILE=build/libs/integrador-ai-0.0.1.jar -t integrador_ai .

docker run -d -p 8080:8080 \
-e INT_WEB_POSTGRES_URL="jdbc:postgresql://host.docker.internal:5432/integrador-web" \
-e INT_WEB_POSTGRES_USER="postgres" \
-e INT_WEB_POSTGRES_PASS="1234" \
--name integrador_ai integrador_ai
```

### 1.4.2 Postgresql
Instalar a utlima versão do postgresql  
ou  
utilizar a versão do docker
```
docker pull postgres
docker run --name postgres1 -e POSTGRES_PASSWORD=1234 -d -p 5432:5432 postgres
```
Os dados que seram utilizados para conectar nele serão:
```
server: `localhost:5432/postgres`   
Usuario: `postgres`     
Senha: `1234`       
```

### 1.4.3 OpenAPI
Ao rodar o backend automaticamente o swagger sera servido em    
`localhost:8080/swagger`

# 2 Refatoração

## 2.1 Problemas detectados

### 2.1.1 Nomenclatura:
- Nomes em português e inglês sem nenhuma diferenciação observável.  
- Pluralidade e singularidade em nomes de variáveis e classes sem distinção para identificar objetos como arrays e listas.

### 2.1.2 Estrutura do projeto e workflow:
- Projeto muito centralizado e inteiramente dependente do workflow e dos services do plano de estudo.

### 2.1.3 Tratamento de erros e exceções:
- Ausência de tratamento de erros ou uso de exceções ao identificar processos incorretos.

### 2.1.4 Code Smells
- Dead Code (codigos que não são utilizados) `Varias ocorencias`
- Comments (Comentarios desnecessarios) `Varias ocorencias`
- Long Method (um metodo muito longo, que faz muitas coisas) `PlanoEstudoService.generateNewPlan()`

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
Adicionado 1 teste para cada metodos dos services `PlanoEstudoService`, `PreferenciaService`, `UsuarioService` e 1 teste para cada metodo dos Controllers `PlanoEstudoController`, `PreferenciaController`, `UsuarioController` 

## 2.3 Interface fluente
### Proposta de interface fluente 1: 
Na Criação do plano de estudo, fazer uma interface fluente para adicionar os dados do plano de estudo, fazer a chamada para o gpt e salvar os dados. substituindo a função `PlanoEstudoService.handleNewPlan()`.   
Exemplo:
```
PlanoEstudo planoNovo = PlanoEstudoBuilder.newPlan()
        .withCurrentUser()
        .loadPreferencia()
        .generateExercicios()
        .createPlan()
        .disableOldPlan()
        .buildExercicios()
        .build();
```
### Proposta de interface fluente 2:
Fazer um builder de dados para todas a Classes de Entidade(Entity) para facilitar a criação parcial delas.   
Exemplo: na classe `Preferencia`
```
Preferencia preferencia = PreferenciaBuilder.builder()
        .idUsuario(123)
        .idioma("PT")
        .addDiaSemana(DayOfWeek.MONDAY)
        .addDiaSemana(DayOfWeek.WEDNESDAY)
        .addTipoExercicio("Gramática")
        .addTipoExercicio("Vocabulário")
        .addTema("Animais")
        .addTema("Comida")
        .dificuldade("Médio")
        .nivel("Intermediário")
        .tempoMinutos(30)
        .ativo(true)
        .dataCriacao(new Date())
        .build();
```


# 3 Integrantes
- [IURI DE LIMA MARQUES](https://github.com/iurilimamarques)    
- [THIAGO LARANGEIRA DE SOUZA](https://github.com/thiagolarangeiras)
