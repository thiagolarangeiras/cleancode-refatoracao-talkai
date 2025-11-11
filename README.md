# 1 Sobre

## 1.1 Descrição
Este projeto apresenta um aplicativo para o aprendizado de idiomas, utilizando inteligência artificial para personalizar a experiência de aprendizado do usuário.     

O backend deste projeto é responsável por gerenciar toda a lógica do aplicativo de aprendizado de idiomas, garantindo uma experiência personalizada e eficiente para cada usuário. Ele atua como o núcleo que conecta o usuário à inteligência artificial, processando dados de perfil, histórico de aprendizado e preferências individuais para oferecer conteúdos adaptados às necessidades de cada pessoa.

Uma das funcionalidades centrais do backend é a integração com o ChatGPT, que é utilizada para gerar exercícios e atividades de aprendizado de forma dinâmica. Com base no nível do usuário, nos tópicos estudados e nos objetivos definidos, o ChatGPT cria exercícios personalizados, como traduções, perguntas de compreensão, diálogos simulados e quizzes interativos, tornando o aprendizado mais envolvente e eficaz.

Além disso, o backend gerencia autenticação de usuários, armazenamento seguro de dados, rastreamento de progresso e comunicação entre o aplicativo e o modelo de IA, garantindo que toda a experiência seja fluida e confiável. Essa estrutura permite que o aplicativo ofereça um aprendizado de idiomas adaptativo, capaz de evoluir junto com o usuário.

## 1.2 Funcionalidades

### 1.2.1 Gerenciamento de usuários
- Cadastro, login e autenticação segura.
- Armazenamento e atualização de perfis/dados de usuários.

### 1.2.2 Personalização do aprendizado
- Registro do histórico de aprendizado de cada usuário.
- Ajuste do nível de dificuldade dos exercícios com base no desempenho.
- Recomendações de conteúdos e atividades personalizadas.

### 1.2.3 Geração de exercícios com IA
- Integração com o ChatGPT para criação dinâmica de exercícios.
- Produção de atividades como traduções, quizzes, diálogos simulados e perguntas de compreensão.
- Adaptação dos exercícios conforme o progresso do usuário.

### 1.2.4 Gerenciamento de progresso
- Armazenamento de resultados e métricas de desempenho.
- Rastreamento de evolução ao longo do tempo.

### 1.2.5 Comunicação com o front-end
- Disponibilização de APIs para que o aplicativo móvel ou web consuma dados e receba exercícios.
- Envio de respostas e feedback do usuário para o sistema de IA.

### 1.2.6 Segurança
- Proteção de dados sensíveis do usuário.

## 1.3 Instalação

### Requesitos:
- JDK 23, 24 ou 25
- IntelliJ
- Postgresql
- Docker
- Conta OpenAI com chave de API
- Ultima Versão desse codigo fonte

## 1.4 Execução

### 1.4.1 Java/Backend

Atenção o ambiente de execução local está configurado para não realizar as chamadas para a API da OpenAI, por motivos de segurança e praticidade na execução local.

Alterar o ambiente de execução para `prod` case queira rodar utilizando a API 

#### Nativa terminal:
Rodar os comandos a seguir na base do projeto
```
./gradlew bootRun
```

#### Nativa IntelliJ:
Abrir o arquivo `AiApplication.java` clicar no botão de rodar

#### Docker:
Apenas para publicação, ainda é necessario compilar o codigo localmente
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

Necessario apenas para execução em ambiente de produção, para execução local já está configurado o H2

Instalar a utlima versão do postgresql ou utilizar a versão do docker
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

### 2.2.2 Convenção de nomeação:
- **PascalCase** para classes e tipos.  
- **camelCase** para variáveis, funções e métodos.

## 2.2.3 Padrões de Git:
- **Conventional Commit**: add, remove, refactor, etc.

## 2.2.4 Linter
- Utilizado o CheckStyle e seu pluggin para o Intellij `CheckStyle-IDE` com os padrões do Google, para analisar o codigo.
- Utilizado o formatador do IntelliJ, para formatar o codigo.

## 2.3 Testes
Adicionado 1 plano teste para cada metodos dos services `PlanoEstudoService`, `PreferenciaService`, `UsuarioService` e 1 teste para cada metodo dos Controllers `PlanoEstudoController`, `PreferenciaController`, `UsuarioController` 

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
