# Como Criar um Servlet com **Visual Vtudio Code DevContainers**

Olá, este é um tutorial completo para aprender a criar um servlet em Java, utilizando o ambiente de desenvolvimento do visual studio code em conjunto a um recurso chamado DevDontainers, ele permite que possamos replicar o mesmo ambiente de desenvolvimento com outros desenvolvedores e também nos permite desenvolver nossas aplicações sem a necessidade de instalar localmente (computador local) software para desenvolvimento, como é o caso do java, sem falar de que fica muito mais fácil de futuramente colocarmos nosso software em um contêiner quando enviar software para produção

_Para este tutorial é necessário estar ou em um Github Codespaces ou ter Docker instalado na máquina base_

_Vou deixar entre `()` o que eu for colocando caso esteja acompanhando e fazendo_

_**Código Fonte**: https://github.com/Cabeludo36/servlet-devContainer_
## 1. Preparando o Ambiente

### Criando um DevContainer
Primeiramente é necessário criar um `devContainer`, pressionando `ctrl + shift + P` e selecionando a opção `Dev Containers: Add Dev Container Configuration Files...` isso fará algumas perguntas:
1. Qual tipo de contêiner você vai usar? -> Java
2. Qual versão vai usar? -> Mais recente (21-bullseye)
3. Caso pergunte se você que instalar `Maven` ou `Gradle`, não selecione nenhuma, vamos instalar ela no contêiner manualmente, pois `SDKMAN` pode bugar com `devContainer` às vezes
### Instalando Dependências Pendentes
Como comentado no passo anterior optamos por não instalar Maven de cara, por conta de alguns bugs que podem ocorrer entre o `SDKMAN` e devContainer

```bash
# Para listar as versões do maven disponíveis
sdk list maven

# Para instalar a versão desejada (em 3.9.10 troque pela versão desejada)
sdk install maven 3.9.10
```

---
## 2. Instalando Extensões
Para uma melhor experiência de desenvolvimento, é recomendado baixar as seguintes extensões para o VS Code:
### 1. Extension Pack for Java
Ela nos dará uma experiência de desenvolvedor mais fácil para debugar, compilar e executar novas versões do nosso código para teste
### 2. XML
Ela nos permitirá editar arquivos `.xml`, com auto completes e outras facilidades para esse tipo de arquivo
### 3. Community Server Connectors
Ela nos permite criar um servidor de teste com inúmeros conectores, mas vamos usar apenas o `tomcat`

---
## 3. Criando um Projeto
1. Para criar um projeto basta apertar `ctrl + shift + P`
2. Selecionar a opção `Java: Create Java Project...`
3. Selecionar a opção Maven
4. Selecionar o Archetype `maven-archetype-webapp`
5. Selecionar a opção de versão desejada do Archetype (1.7)
6. Dê um `Group ID` do projeto (com.example)
7. Dê um Nome ao projeto
8. Selecione o local que ficará o projeto
9. No terminal irá pedir para dar uma versão ao seu projeto, pode apertar `Enter`
10. Para confirmar as decisões anteriores, aperte `Enter`

Isso irá criar uma nova pasta no diretório escolhido para o projeto, que terá a seguinte estrutura:
```
meuprojeto/
- src/
-- main/
--- webapp/
---- index.jsp
---- WEB-INF/
----- web.xml
- pom.xml
```
- `meuprojeto/`: É o `root` (raiz) do seu projeto que terá todos os arquivos e pastas do seu projeto
- `src/`: é onde editamos o código, temos que ter ela para diferenciarmos das coisas que são buildadas na pasta `target/` que vamos ver para o que serve mais para frente
- `main/`: para o código principal de nossa aplicação
- `webapp/`: para o código que vai definir nossa aplicação web, onde podemos definir páginas `HTML` (mas que vão ter extensão `.jsp`) e onde poderemos definir nossos endpoints `servlets`
- `index.jsp`: arquivo com a página inicial do seu projeto, que no momento tem apenas um `hello world`
- `WEB-INF/`: pasta onde vamos definir o arquivo de nossos servlets
- `web.xml`: arquivo com a informação que o servidor olha pra saber **onde buscar** servlets
- `pom.xml`: arquivo que Maven usa para definir quais pacotes/bibliotecas devem ser instalados para a aplicação funcionar

## Configurando o projeto

### 1. Entrando na pasta do projeto
Para que o VS Code entenda que estamos em um projeto Java e para diminuir a quantidade de mais informações em nosso explorador de arquivos no terminal use o comando `code meuprojeto/ -r` onde 
- `code`: é para chamarmos o VS Code
- `meuprojeto/`: que é usada para falarmos que queremos abrir o VS Code na pasta `meuprojeto/` 
- `-r`: para definir que queremos apenas fazer reload da tela que estamos ao invés de abrir uma nova
_Caso um erro ocorra ao executar o comando, mate o terminal usando o ícone de lixeira, abra um novo e tente novamente_
### Definindo uma pasta de projeto java
Para termos uma pasta para colocarmos os nossos servlets e definirmos o que eles vão fazer é necessário criar uma pasta de projeto java, pois no java existe um sistema de arquivos próprio usado para definir pacotes entre outras coisas, por isso não usamos apenas pastas comuns no diretório do projeto e precisamos adicionar como uma pasta de `Source Path` para o Java

Neste caso vou criar a pasta `java/` em `meuprojeto/src/main/` e vou adicionar ela como uma pasta do projeto java clicando com botão direito nela no explorador de arquivos e selecionando a opção `Add Folder to Java Source Path` (Esta opção só aparece caso tenha instalado o `Extension Pack for Java`), caso tenha seguido até aqui corretamente em seu explorador de arquivos ou ao lado de seu terminal terá aparecido uma aba chamada `Java Projects`, se expandirmos ela vamos ver o projeto e uma das pastas vai ser `src/main/java` isso significa que nosso projeto já entendeu que essa é uma pasta para ele ficar de olho quando formos lançar versões/depurarmos/compilarmos nossos arquivos que estão nela

### 2. Definindo um pacote
Depois de criar a pasta no projeto precisamos criar um pacote assim podemos definir de onde buscar os servlets quando o `web.xml` for direcionar usuários para eles, para criar um pacote é bem simples, na nova aba de `Java Projects` clique com o botão direito na pasta que criamos no passo anterior, `new`, selecione `package` e crie um nome ao seu pacote (`com.servletApi`), se você seguiu corretamente verá que no explorador de arquivos terá novas pastas onde o caminho completo forma `src/main/java/com/servletApi` e na aba `Java Projects` abaixo da pasta que criamos terá `com.servletApi`

### 3. Instalando dependências
Agora que nosso projeto já esta com as pastas configuradas podemos baixar as dependências do nosso projeto, no caso deste tutorial será apenas para que possamos usar servlets na nossa aplicação

Para instalar dependências pressione `ctrl + shift + P`, selecione `Maven: add dependency...` , escreva `jakarta.servlet-api`, de `Enter` para pesquisar e selecione o pacote de nome `jakarta.servlet-api` 

Isso irá criar o seguinte bloco no arquivo `pom.xml` dentro do bloco `<dependencies>`
```xml
<dependency>
	<groupId>jakarta.servlet</groupId>
	<artifactId>jakarta.servlet-api</artifactId>
	<version>6.1.0</version>
</dependency>
```

_Nota: muitas das fontes que for procurar online vão estar usando `xjava.servlet-api`, mas isso é porque o Java EE passou da tutela da Oracle para a Eclipse Foundation ( Jakarta EE ), com isso os imports do Java EE (javax.servlet) foram substituídos pelo novo Jakarta EE, portanto os imports passaram a ser jakarta.servlet.*_

_Qual import utilizar vai depender da versão da servlet/tomcat que você está usando_ 

## Botando a Mão na Massa
### Criando um servlet
Para criar um serlet precisamos criar uma classe java que `extends` (herde) a classe `HttpServlet` e que sobrescreva o método original `doGet`

Para criar uma classe Java no VS Code temos várias opções
- Simplesmente criar um arquivo na pasta que queremos e escrever a classe manualmente;
- Clicar com o botão direito na pasta que queremos no explorador de arquivos > `New Java File` e selecionar `Class`;
- E o que vamos usar, na aba `Java Projects` clique com o botão direito **no pacote** `com.servletApi` > `New` > `Class...` e dar o nome da classe (`ServletHello`)
Ao final devemos ter um resultado como
```java
// pacote que contém a classe ServletHello
package com.servletApi;

public class ServletHello {

}
```
Vamos adicionar a herança da classe `HttpServlet`
```java
package com.servletApi;

// importações necessárias para o servlet
import jakarta.servlet.http.HttpServlet;

public class ServletHello extends HttpServlet {

}
```
Vamos agora sobrescrever o método `onGet`
```java
package com.servletApi;

// importações necessárias para o servlet
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletHello extends HttpServlet {
	// este método é chamado quando o servlet recebe uma requisição HTTP GET
	// onde HttpServletRequest req contém informações sobre a requisição
	// e HttpServletResponse res é usado para enviar uma resposta ao cliente
	@Override // sobrescreve método da classe base HttpServlet
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("text/html"); // define o tipo de conteúdo da resposta como HTML
		res.setCharacterEncoding("UTF-8"); // define a codificação de caracteres da resposta como UTF-8
		try { // bloco try para capturar possíveis exceções de E/S
			PrintWriter out = res.getWriter(); // obtém um PrintWriter para escrever a resposta
			out.println("<html>"); // inicia o documento HTML
			out.println("<head><title>Servlet Hello</title></head>"); // define o título da página
			out.println("<body>"); // inicia o corpo do documento HTML
			out.println("<h1>Hello, Servlet!</h1>"); // escreve um cabeçalho no corpo do HTML
			out.println("<p>This is a simple servlet example.</p>"); // escreve um parágrafo no corpo do HTML
			out.println("</body>"); // fecha o corpo do documento HTML
			out.println("</html>"); // fecha o documento HTML
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
```

### Liberando o serviço
Este servlet que criamos irá enviar para o cliente uma página HTML, porém nosso serviço web ainda não sabe que ele existe, precisamos declarar sua existência no arquivo `web.xml`
Seu arquivo deve estar da seguinte forma
```xml
<web-app>
  <display-name>Archetype Created Web Application</display-name>
</web-app>
```
Antes de adicionar o servlet precisamos definir algumas informações de configuração de como interpretar nosso projeto no nosso arquivo `xml` 
```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	version="3.1">
	<display-name>Archetype Created Web Application</display-name>
</web-app>
```
Com o arquivo configurado podemos então criar uma referência ao nosso servlet
```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	version="3.1">
	<display-name>Archetype Created Web Application</display-name>

	<servlet>
		<servlet-name>ServletHello</servlet-name>
		<servlet-class>com.servletApi.ServletHello</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>ServletHello</servlet-name>
		<url-pattern>/api/hello</url-pattern>
	</servlet-mapping>

</web-app>
```
- `servlet`: é usada para definirmos informações da nossa classe que criamos
- `servlet-name`: é o nome que esse servlet tem no arquivo, como se fosse um nome de uma variável 
- `servlet-mapping`: é usada para as informações de mapeamento web desse servlet
- `url-pattern`: é a URI que irá chamar a função `onGet`, `onPost`, `onPut`, `onDelete` dependendo do método HTTP
### Testando o servlet
#### Gerando um build da nossa aplicação
Para que um servidor possa rodar nossa aplicação é necessário fazer primeiramente o build (compilar) dela para que seja possível executar o serviço

Para gerar um build temos duas partes, que devem ser feitas a cada nova versão do código: 
##### Gerar a compilação dos arquivos
Temos que compilar nossa aplicação java para que o `Java Runtime` entenda como interpretar nossos arquivos, mas antes se você seguiu a risca o que estou fazendo neste tutorial tem uma coisa que ainda não fizemos que é definir qual é o alvo de compilação do Maven ou seja qual versão do java está instalada, temos que fazer isso pois o VS Code não traz a versão correta que esta instalada, mas sim a de um template predefinido, no arquivo  `pom.xml` no bloco `<properties>` deixe da seguinte forma:
```xml
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<maven.compiler.source>1.21</maven.compiler.source>
	<maven.compiler.target>1.21</maven.compiler.target>
</properties>
```
_no lugar dos `1.21` troque pela sua versão_

Com isso resolvido vá na aba `Java Projects`, clique com o botão direito no projeto (meuprojeto) e selecione `Rebuild All`, isso irá gerar uma pasta chamada `target/`, onde nossa classe `ServletHello` compilada (agora com a extensão `.class`) estará.
##### Gerar o arquivo `.war`
Para que possamos executar nossa aplicação, agora **como um projeto**, temos que gerar um arquivo `.war`, para fazer isso basta irmos na aba `Maven`, expandir a aba do seu projeto (meuprojeto), expandir `Plugins`, expandir `war`, em `exploded` clicar com o botão direito e selecionar `Run` 

E assim será gerado um novo diretório na pasta `target` com o nome do seu projeto, neste caso o caminho completo ficou `target/meuprojeto/` 

_Nota: nenhum arquivo da pasta `target/` pode ser alterado, caso o contrário isso pode resultar no projeto não funcionando corretamente_ 
#### Subindo um servidor
Para que possamos testar o servlet é necessário termos um servidor, maior parte das fontes java usam o `Tomcat`, ele pode ser instalado na máquina local, mas como estamos em um devContainer porque não queremos instalar coisas localmente podemos usar a extensão `Community Server Connectors` que nos permitirá baixar no contêiner e executar nossa aplicação

Na aba `Servers` em seu explorador de arquivos, clique no ícone de servidor com um `+` selecione a opção `yes` para baixar um servidor, selecione `Apache Tomcat` > selecione continuar, aceite os termos da licença, e ele começará a fazer download do `Tomcat`

Quando o download tiver concluído aparecerá um servidor `Tomcat` com isso podemos simplesmente clicar com o botão direito na pasta de nosso projeto na pasta `target/` (`target/meuprojeto`), e rodar `debug on server` ou `run on server`, neste caso vou debugar > selecionar o servidor que quero rodar a aplicação, selecionar que não desejo opções adicionais e dar um nome de projeto para depuração (`meuprojeto`)

Você irá perceber que no canto inferior direito aparecerá uma notificação de que uma porta foi aberta do contêiner para a máquina local, se clicar em `Abrir no Browser`, você será redirecionado para a url `http://localhost:8080/` com algumas informações sobre o `Tomcat`, esse é o root do servidor que o `Tomcat` abriu para nós

Se navegarmos para `http://localhost:8080/{nome da pasta em target/}/` (`meuprojeto`) veremos que a mensagem padrão é `Hello World` como especificado no arquivo `src/main/webapp/index.jsp`
```html
<html>
<body>
	<h2>Hello World!</h2>
</body>
</html>
```
É possível editar esse arquivo para deixar mais apresentável, mas ainda não vimos nosso servlet em ação para que possamos fazer isso precisamos ir para o endpoint que definimos no `src/main/webapp/WEB-INF/web.xml` neste caso ficou `http://localhost:8080/meuprojeto/api/hello`, vamos ver a mensagem que colocamos lá para fazer o display

Mas lembrando, o ideal é deixar telas dinâmicas, e na maior parte das vezes usamos ferramentas backend para formar essas páginas ou enviar informações para elas, isso incluindo também outras operações como 
- Create: Criar Dados (POST)
- Read: Ler Dados (GET)
- Update: Atualizar Dados (PUT)
- Delete: Deletar Dados (DELETE)

# Criando Páginas Dinâmicas
Esta seção serve para desenvolvermos páginas dinâmicas para desenvolvimento web com java, partindo de onde a seção anterior foi finalizada, _com um adendo, foi iniciado um novo projeto java no mesmo repositório o `dadosdinamicos` para preservar o código da primeira versão do código sem a necessidade de utilizar `git` e poder acompanhar o tutorial_, mas caso esteja seguindo da última seção pode usar o mesmo projeto, só alterar onde coloco `dadosdinamicos` pelo projeto da seção anterior.

Este modulo tem alguns pré-requisitos
- Ter conhecimento básico de programação
- Ter um projeto de Servlets Java funcional ou ter seguido a primeira seção do tutorial

## Tipos de Renderização
Existem várias formas de renderizar páginas web, cada uma com suas vantagens e desvantagens, algumas podem dar muita interatividade/ reatividade a nossa página enquanto outros dão mais `SEO` e segurança a elas, mas segue uma definição de algumas formas de renderização.  
### Static HTML
É extremamente rápido por ser uma página de HTML CSS apenas, mas deixa a desejar na parte de interatividade por não permitir interação imediata com o servidor, precisando de outros recursos como outras URLs entre outros
### Client-Side Rendering (CSR)
É como aplicações tradicionais eram usadas, as páginas eram renderizadas no cliente com informações dinâmicas que eram buscadas do servidor. São bem rápidas para carregar em um primeiro momento e tem a possibilidade de interação com o usuário usando javascript, mas demoravam para serem preenchidas, por não estarem preenchidas seu `SEO` (Search Engine Optimization)
### Server-Side Rendering (SSR)
É como grande parte dos sites hoje funciona principalmente sites que precisam de um bom `SEO` como E-Commerces, onde a página é renderizada totalmente nos servidores e o cliente recebe a tela já com os dados. Tem a vantagem de atingir públicos maiores por conta de SEO, mas tem um tempo de carregamento da página um pouco maior, pois todas as consultas já são feitas antes do cliente receber a página, isso pode ser um problema as vezes por acontecerem postbacks ("piscadas" que acontecem quando você clica para pesquisar algo ou termina de preencher um formulário por exemplo) para que novos dados sejam mostrados, isso é o usuário executando algo, o servidor recebe e analisa, cria uma nova página e manda de volta para o cliente
### Hydration
Hydration junta as duas vantagens de sites SSR e CSR, é quando o site é gerado totalmente pelo servidor, dando um ótimo SEO, e enviando a bundle (arquivos que são enviados para o cliente) com todos os arquivos JS, apesar de ter um primeiro carregamento mais demorado é extremamente interativo e responsivo para o usuário, pois tem JS executando junto do site e interagindo diretamente com os elementos que o servidor enviou, como regras de botões não aparecerem quando não devem, entre outros.

## Nossa Primeira Página SSR
Para criar uma página renderizada pelo servidor em um arquivo `.jsp` é necessário abrir uma tag `<% %>` que colocamos código java e para fazer isso interagir com a página é necessário usar a tag `<%= %>` que permite usarmos as variáveis da tag anterior um exemplo seria um loop for onde queremos escrever uma linha para cada elemento, podemos testar editando o arquivo `index.jsp` para 
```jsp
<html>
<body>

	<h2>Hello World!</h2>
	<%
		// inicia loop for
		for(int i = 0; i < 5; i++) {
		%>
	<!-- escreve linha com a variável i -->
	<p class="green-text">Line <%= i + 1 %></p>
	<%
		// fecha o loop for
		}
		%>

</body>
</html>
```
Vamos fazer um novo build de nossa aplicação, gerar um novo `war` e ver as alterações, se formos para `http://localhost:8080/dadosdinamicos/` que é onde nosso index está sendo mostrado vamos ver 5 linhas com seus valores dinâmicos e nem utilizamos JS ou escrevemos manualmente, e para provar que isso foi gerado no servidor podemos apertar com o botão direito em qualquer lugar da página > inspecionar > Network (Rede) > encontra nossa página com nome `dadosdinamicos/` > Preview, e ai veremos que a página foi gerada completamente no servidor por ter essas 5 linhas logo quando chegou no cliente

Outra forma de se fazer parecida com a que fizemos a página servlet na seção anterior é 
```java
package com.servletApi;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletHello extends HttpServlet {

    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {

        res.setContentType("text/html"); 
        res.setCharacterEncoding("UTF-8"); 
        
        try { 
            
            PrintWriter out = res.getWriter(); 
            out.println("<html>"); 
            out.println("<head><title>Servlet Hello</title></head>"); 
            out.println("<body>"); 
            out.println("<h1>Hello, Servlet!</h1>");
            // Loop para escrever 5 parágrafos no corpo do HTML 
            for (int i = 0; i < 5; i++) { 
                out.println("<p>This is a simple servlet example "+i+".</p>"); // escreve um parágrafo no corpo do HTML
            }
            out.println("</body>"); 
            out.println("</html>"); 
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
```
O problema dessa forma de se fazer é que não é muito escalável, imagine tendo que fazer uma página muito grande com milhares de `println`, mas é bom saber que é possível fazer desta forma também

## Organizando as Coisas
Como vamos começar a ter várias telas para nossos testes decidi criar uma seção apenas para organizar o projeto um pouco

Primeira coisa é criar um novo arquivo ao lado do atual `index.jsp` chamado `SSR.jsp`, no qual vamos copiar o conteúdo do `index.jsp` para ele enquanto no `index.jsp` vamos substituir o conteúdo anterior por uma lista que leva para essas páginas, ou seja, será como um hub para nossos testes, mas para não ficar sem nada vamos colocar um conteúdo dinâmico que será o horário, e no `SSR.jsp` vamos colocar um botão de voltar que voltará para o `index.jsp`, deixando nossos códigos da seguinte forma:

`index.jsp`
```jsp
<html>
<body>
    <h2>Hello World!</h2>
	<!-- escreve linha com horário -->
    <%= "Current time is: " + new java.util.Date() %>
    
    <ul>
        <li><a href="SSR.jsp">SSR</a></li>
    </ul>
</body>
</html>
```

`SSR.jsp`
```jsp
<html>
<body>
    <a href="index.jsp"><-- Back to Index</a>
    
    <%
        for(int i = 0; i < 5; i++) {
    %>
            <p>Line <%= i + 1 %></p>
    <%
        }
     %>
</body>
</html>
```

Perfeito agora temos espaço para crescer um pouco mais

## Caprichando um pouco mais
Para deixarmos nossos projetos reutilizáveis e organizados, dividimos as responsabilidades de cada parte do código em camadas, a mais comum no desenvolvimento Web é a MVC onde: 
- `M`: significa `Model` (`DAO`) esta é a parte onde são acessados os dados que vamos utilizar
- `V`: significa `View` esta é a parte onde os cliente sejam eles programadores, pessoas comuns, máquinas ou até hackers vão ter visão de nossas aplicações
- `C`: significa `controller` ela é a parte que lida com regras de negócio de nossa aplicação e onde a maior parte do código reutilizável deve ficar, ela que valida se os dados inseridos estão certos ou se pessoas tem acesso a funcionalidade que estão querendo utilizar
Dentro desta arquitetura a camada de `Model` (`DAO`) nunca poderá conversar diretamente com a camada de `View`, para que a camada `View` acesse dados terá que realizar uma chamada na `Controller` para fazer uma consulta na `Model` que faz o gerenciamento de serviços externos como bancos de dados por exemplo 

No nosso caso já temos "duas camadas" `View` (entre aspas bem grandes), pois pelo `Archetype` deste projeto de servlets que escolhemos, temos acesso pelas telas web (`SSR.jsp` e `index.jsp`) e a um serviço de API (`ServletHello`) então temos duas forma e abordar situações, uma que é criando a tela e mostrando os dados que buscarmos direto (SSR) e outra que é gerar a tela e buscar depois (CSR)

Vou criar uma página buscando os mesmos dados do mesmo lugar (`controller`) e mostrando na tela de formas diferentes para que possamos ver a diferença entre essas duas formas de renderização, mas primeiro precisamos criar o local que essas informações serão retiradas então vamos criar a camada de `DAO` (`Model`) e a camada de Negócio (`Controller`)  
### Criando novas camadas
Para criarmos novas camadas precisamos acessar a aba de `Java Projects` na mesma página que colocamos o pacote `com.servletApi` (`src/main/java`) vamos criar mais três pacotes, `com.models`, `com.controllers` e `com.dto` que é para nossos `Data Transfer Object` (`DTO`)
### Criando dados fictícios
#### Criar uma classe `Pessoa`
Vamos criar uma classe `Pessoa` em `com.dto`, ela servirá para guardarmos os dados de um objeto 
```java
package com.dto;

public class PessoaDTO {
	// declara atributos
    private String nome;
    private int idade;

	// construtor da pessoa
    public PessoaDTO(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

	// gets e sets 
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}
```
#### Criar lista fictícia
Agora que já temos a classe que vamos guardar nossos dados, precisamos de um local para guardar vários objetos derivados dessa classe `Pessoa`, assim simulamos um banco de dados. Vamos criar uma nova classe `PessoaModel` em `com.models` agora sendo estática, assim não perdemos os dados que estão nela e conseguimos fazer quantas consultas quisermos

Para simular um banco de dados vamos também adicionar um tempo para os dados serem retornados
```java
package com.models;

import java.util.ArrayList;
import java.util.List;

import com.dto.PessoaDTO;

public final class PessoaModel {

    // lista estática de pessoas
    // inicializada com alguns dados de exemplo
    // não irá mudar durante a execução do programa
    private static List<PessoaDTO> pessoas = new ArrayList<PessoaDTO>() {
        {
            add(new PessoaDTO("João", 30));
            add(new PessoaDTO("Maria", 25));
            add(new PessoaDTO("José", 40));
        }
    };

    // constructor privado para evitar instância
    private PessoaModel() {}

    // método para listar as pessoas
    public static List<PessoaDTO> getPessoas() {
        // delay para simular uma operação de longa duração
        try {
            Thread.sleep(2000); // 2 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // retorna pessoas
        return pessoas;
    }
}

```
### Criar `Controller` de pessoas
Para que possamos gerir métodos comuns entre vários lugares da nossa aplicação precisamos de um lugar centralizado e esse lugar são as `Controllers`, neste caso vamos ter dois métodos um `getPessoasAsync` e um `calcularPodeBeber(idade)`, vamos criar uma classe `PessoaController` em `com.controllers`
```java
package com.controllers;

import java.util.List;

import com.dto.PessoaDTO;
import com.models.PessoaModel;

// 
public final class PessoaController {

    public static List<PessoaDTO> getPessoasAsync() {
        // Chama o método estático da PessoaModel para obter a lista de pessoas
        var pessoas = PessoaModel.getPessoas();
        
        return pessoas;
    }

    public static boolean calcularPodeBeber(int idade) {
        // Verifica se a idade é maior ou igual a 18
        return idade >= 18;
    }
}

```
### Usando dados fictícios na página
Agora que já temos todas as classes e métodos preparados para nos trazer uma lista de pessoas, precisamos preparar o local que iremos utilizar essas informações, vamos criar um novo arquivo, novamente ao lado do `index.jsp`, chamado `ssrPessoa.jsp` que terá o seguinte conteúdo
```jsp
<html>
<body>
    <a href="index.jsp"><-- Back to Index</a>

    <ul>
        <%
            // importando as classes necessárias com caminho completo Ex.: java.util.List
            // assim JSP entende qual classe usar
            java.util.List<com.dto.PessoaDTO> pessoas = com.controllers.PessoaController.getPessoasAsync();
            for (var pessoa : pessoas) {
        %>
            <li class="blue-text">
	            <!-- usa nome do objeto PessoaDTO -->
                <span>Name: <%= pessoa.getNome() %></span>
                -
	            <!-- usa idade do objeto PessoaDTO -->
                <span>Age: <%= pessoa.getIdade() %></span>
                - 
	            <!-- usa idade do objeto PessoaDTO par calcular se pode beber usando método calcularPodeBeber do PessoaController -->
                <span>Can Drink: <%= com.controllers.PessoaController.calcularPodeBeber(pessoa.getIdade()) ? "Yes" : "No" %></span>

            </li>
        <%
            }
        %>
    </ul>
</body>
</html>
```
Vamos também adicionar um link para essa nova tela no nosso hub
```jsp
<html>
<body>
    <h2>Hello World!</h2>

    <%= "Current time is: " + new java.util.Date() %>
    
    <ul>
        <li><a href="SSR.jsp">SSR</a></li>
        <li><a href="ssrPessoa.jsp">SSR Pessoas</a></li>
    </ul>
</body>
</html>

```
### Entendendo resultados de uma página SSR
Agora que tudo está feito vamos recompilar e gerar o `war` para vermos como ficou ao final na tela `SSRPessoa.jsp`. Se formos para a tela vamos perceber que a tela agora ela está demorando 2 segundos para ser aberta, isso porque a tela precisa ser gerado no servidor antes de chegar ao cliente e como adicionamos 2 segundos para simular uma consulta em um banco de dados ocorreu esse delay para abrir a página e para comprovarmos que a tela foi gerado no servidor vamos clicar com o botão direito em qualquer lugar da página > inspecionar > Network (Rede) > encontra nossa página com nome `ssrPessoa.jsp` > Preview, e veremos que os dados foram enviados direto para o cliente sem precisar 

Agora temos um site com arquitetura de camadas MVC que pode listar, mostrar e fazer cálculos de informações de pessoas
## Criando uma página CSR
Como comentamos em [[#Tipos de Renderização]], outra forma de adicionarmos dados dinâmicos na nossa página é usando CSR, nesta forma de renderização a página é enviada para o cliente, porém seus dados dinâmicos são buscados depois com JS ou em outros casos WebAssambly, isso da uma performance mais rápida em um primeiro momento, porém para sites de busca, como google e bing, os dados dinâmicos muitas vezes não são reconhecidos por não estarem no primeiro carregamento
### Criando uma API de dados
Como teremos que buscar dados de algum lugar irei lembrar que temos duas camadas de `View` em nossa aplicação, uma delas é nossa página `.jsp`, como foi o caso da `ssrPessoa.jsp` e também temos os nossos servlets, que são como endpoints de uma `API` (Application Programming Interface), `API`s nos ajudam a criar automações e geralmente não são feitas para serem usadas por usuários comuns mas sim por aplicações que realizam chamadas em seus endpoints, sejam eles em qualquer linguagem de programação, uma aplicação `JS`, `Vue.js`, `React.js`, `C#`, `python`, entre várias outras podem ser usadas para chamar esta nossa `API` que esta em `Java` 

No momento temos apenas o endpoint `ServletHello` que retorna uma página html dizendo `Hello world` e um teste que fizemos com SSR no servlet, mas como comentei, usar servlets para retornar páginas não se torna viável em projetos maiores, portanto vou adicionar um novo endpoint chamado `PessoaServlet.java` que poderá ser acessado em `/api/pessoa` este retornará uma lista de todas as pessoas do sistema, como já implementamos a camada de `Model` (`DAO`) e a camada de `Controller`, não vamos precisar reescrever toda a lógica para a listagem então vamos partir direto para o servlet começando com o seguinte código

```java
package com.servletApi;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PessoaServlet extends HttpServlet {
    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        
    }
}
```

#### Entendendo `JSON`
Como você já deve ter percebido o `content type` que estamos colocando está como `application/json` isso significa que vamos retornar um `JSON` (`JavaScript Object Notation`), é um tipo de formato padronizado utilizado por inúmeras aplicações para se comunicarem com outros serviços, seu formato é simples e versátil aqui vão alguns exemplos:
##### Texto Simples
É definido com aspas e o texto no meio 
```json
"Hello World"
```
##### Números Simples
É definido pelo número apenas
```json
123
```
##### Booleano Simples
É devinido pelos valores verdadeiro (`true`) ou falso (`false`)
```json
true
```
##### Objetos
Objetos são estruturas mais complexas de dados que representam algo como Pessoas, Carros, Frutas, etc..., eles são definidos por chaves (`{}`) e entre elas pares de chave, que devem estar entre aspas, que representam uma propriedade do objeto, dois pontos (`:`) , por fim o valor que será do tipo texto, número ou booleano, e vários atributos podem ser separados por virgula, o exemplo abaixo representa uma pessoa
```json
{
	"name": "Breno Pimentel",
	"age": 18,
	"canDrink": true,
	"children": null
}
```
##### Lista
É definido por colchetes (`[]`) e entre eles os valores, sendo eles textos, números ou objetos, separados por virgula (`,`), neste exemplo coloquei uma lista de pessoas
```json
[
	{
		"name": "Breno Pimentel",
		"age": 18,
		"canDrink": true,
		"children": null
	},
	{
		"name": "Marcelo Pimentel",
		"age": 15,
		"canDrink": false,
		"children": null
	},
	{
		"name": "Cicera Pimentel",
		"age": 55,
		"canDrink": true,
		"children": [
			"Breno Pimentel",
			"Marcelo Pimentel"
		]
	}
]
```


#### Retornando dados
Agora que definimos como os dados serão retornados temos que criar uma forma de converter nossos objetos `java` em `JSON`, para isso temos várias formas
- Escrever manualmente no servlet;
- Ou a que iremos fazer neste tutorial, baixar uma biblioteca de conversão para `JSON`
No meu caso vou utilizar a biblioteca `com.fasterxml.jackson.databind`, vamos fazer da mesma forma que a biblioteca dos servlets, vamos pressionar `ctrl + shift + P`, selecionar `Maven: Add Dependency`, pesquisar por `com.fasterxml.jackson.databind`, `Enter` para buscar e vamos selecionar a de nome `jackson-databind`, isso vai adicionar no arquivo `pom.xml` um novo registro em `<dependencies>` 
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.19.0</version>
</dependency>
```
Para usarmos essa biblioteca em nosso código, precisamos antes ter os dados que vem de nossa `Model`, então vamos editar nosso servlet de pessoas para buscar essas informações o resultado deve ser algo parecido com o seguinte no método `doGet`
```java
@Override 
protected void doGet(HttpServletRequest req, HttpServletResponse res) {
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();
}
```
Agora vamos utilizar a biblioteca `jackson` para converter nossa lista de objetos `java` em uma lista de objetos `JSON`
```java
@Override 
protected void doGet(HttpServletRequest req, HttpServletResponse res) {
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();

    // utilizar o PrintWriter desta forma garante que o recurso será fechado corretamente
    // e evita vazamentos de recursos, mesmo que ocorra uma exceção.
    // O bloco try-with-resources é uma boa prática para garantir que o PrintWriter seja 
    // fechado automaticamente após o uso, evitando a necessidade de fechá-lo manualmente.
    try(PrintWriter out = res.getWriter()) {
        ObjectMapper mapper = new ObjectMapper(); // objeto de mapeamento JSON
        String json = mapper.writeValueAsString(pessoas); // converte a lista de pessoas para JSON
        out.print(json); // escreve o JSON na resposta
        out.flush(); // garante que todos os dados sejam enviados ao cliente
    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
}
```
Vale também ressaltar que para deixar mais fácil o mapeamento de nossos servlets, vamos utilizar o atributo `@WebServlet` que nos possibilita fazer o mapeamento automático de nossos servlets deixando o resultado final de nosso servlet da seguinte forma
```java
package com.servletApi;

import java.io.PrintWriter;
import java.util.List;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// mapeamento do servlet dando nome e url de acesso
@WebServlet(name = "PessoaServlet", urlPatterns = {"/api/pessoa"})
public class PessoaServlet extends HttpServlet {
    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();
        
        try(PrintWriter out = res.getWriter()) {
            ObjectMapper mapper = new ObjectMapper(); 
            String json = mapper.writeValueAsString(pessoas); 
            out.print(json); 
            out.flush(); 
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
```
Com isso sem precisar escrever mais nenhuma linha de código vamos poder ver nosso ver nosso endpoint em `http://localhost:8080/dadosdinamicos/api/pessoa` as informações que solicitamos
### Usando a `API` em uma tela
Para usarmos nossa api em uma página CSR para confirmarmos que a `API` funcionará em qualquer tipo de tecnologia vamos ao invés de criar uma página `.jsp` vamos criar uma `.html` com o nome de `csrPessoa.html` que chamará a API via JavaScript 
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pessoas CSR</title>
</head>
<body>
    <a href="index.jsp"><-- Back to Index</a>
    <h1>Pessoas CSR</h1>
    <div id="pessoas">
        Loading...
    </div>
    
    <script>
	    // chama API no endpoint api/pessoa
        fetch('api/pessoa')
	        // converte dados JSON em Objetos JS
            .then(response => response.json())
            // Faz processamento dos dados
            .then(data => {
	            // pega o elemento div com id pessoas
                const pessoasDiv = document.getElementById('pessoas');
                // cria um elemento de ul
                const pessoasUl = document.createElement("ul");
                // para cada dado (pessoa)
                data.forEach(pessoa => {
	                // cria um elemento <li>
                    const li = document.createElement('li');
                    // coloca o conteúdo desse elemento li
                    li.innerText = `Name: ${pessoa.nome} - Age: ${pessoa.idade} - Can Drink: ${pessoa.podeBeber ? 'Yes' : 'No'}`;
                    // coloca li dentro do elemento ul
                    pessoasUl.appendChild(li);
                });
                
                // ao final coloca lista na div com id pessoas
                pessoasDiv.innerHTML = pessoasUl.getHTML();
            });
    </script>
</body>
</html>
```
Com esses desenvolvimentos concluídos, podemos ver que na tela ao entrarmos vamos ter um `Loading...` e após dois segundos os nosso dados vão aparecer por conta da chamada que o JS fez, porém ainda não temos a informação da propriedade `podeBeber` correta, pois não temos ele em nosso objeto base, você pode comprovar que não estamos enviando essa informação indo na chamada na página de rede depois de inspecionar a página

#### Ajustes finais
Para criar um objeto com a propriedade `podeBeber` temos que decidir como vamos fazer isso, nós podemos
- Criar uma nova classe apenas para esse objeto
- Criar um record de retorno
- Criar um map, que é um objeto que podemos colocar chaves e valores
- ou o que vamos utilizar, criar uma `ObjectNode` que também é um objeto chave e valor igual o map, mas da biblioteca jackson
E para fazer um loop na lista também temos várias opções, como:
- Criar um for loop
- Criar um while loop
- ou a que vamos usar que serão `stream`s, eles são métodos que nos permitem com mais facilidade filtrar, fazer mapeamentos, ordenar entre outras coisas em listas

```java
package com.servletApi;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServlet", urlPatterns = {"/api/pessoa"})
public class PessoaServlet extends HttpServlet {
    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();
        
        // utilizar o PrintWriter desta forma garante que o recurso será fechado corretamente
        // e evita vazamentos de recursos, mesmo que ocorra uma exceção.
        // O bloco try-with-resources é uma boa prática para garantir que o PrintWriter seja
        // fechado automaticamente após o uso, evitando a necessidade de fechá-lo manualmente.
        try(PrintWriter out = res.getWriter()) {
            ObjectMapper mapper = new ObjectMapper(); // objeto de mapeamento JSON

            // mapeia valores para json
            String json = mapper.writeValueAsString(
                // faz mapeamento dinâmico das nossas propriedades
                // dessa forma não precisamos criar uma classe apenas para
                // satisfazer um retorno com o atributo "podeBeber"
                // fazemos isso com o metodo .stream().map() onde haverá 
                // um loop por cada pessoa criando um objeto dinâmico
                // com a propriedade "podeBeber"
                // !Nota: poderiamos ter feito isso com uma class ou record tambem!
                pessoas.stream().map(pessoa -> { 
                    // map para fazer mapeamento dinâmico
                    ObjectNode obj = mapper.createObjectNode();

                    // adiciona as propriedades e valores
                    obj.put("nome", pessoa.getNome());
                    obj.put("idade", pessoa.getIdade());
                    obj.put("podeBeber", PessoaController.calcularPodeBeber(pessoa.getIdade()));

                    // retorna da função map o objeto de resposta da pessoa
                    return obj;
                })
                // coleta dados do map como lista e fecha o mapeamento para JSON
                .collect(Collectors.toList())
            ); // converte a lista de pessoas para JSON no ultimo )

            out.print(json); // escreve o JSON na resposta
            out.flush(); // garante que todos os dados sejam enviados ao cliente
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
```
Dessa forma podemos ver que agora nosso objeto esta sendo mapeado corretamente com a propriedade `podeBeber` e isso será refletido na tela

## Conclusão
Então descobrimos que cada forma de renderização tem seus pros e contras, enquanto a SSR carrega a página por completo antes de deixar o usuário utilizar a CSR nos permite deixar o usuário carregar a página mais rapidamente e apenas depois buscar as informações depois, assim possibilitando o usuário interagir com o outras funcionalidades do site antes de terminar a busca do recurso e a `API` desenvolvida podendo funcionar para qualquer tecnologia como cliente, mas não sendo tão bom para páginas de busca quanto o SSR, existem outras formas e tecnologias. Apesar de oque foi mostrado nessa seção, exitem inúmeras tecnologias que podem ser utilizadas, como Vue.js, React.js, entre outros que podem atingir os mesmos resultados ou até melhores.

# Páginas CRUD
Como na seção anterior começamos a desenvolver páginas dinâmicas, ou seja, páginas que mudam dependendo de informações que são fornecidas, agora podemos pensar em trazer ações para nossas páginas, entender mais um pouco os prós e contras dos diferentes estilos de renderização em nossas páginas, poderemos também entender ainda mais porque separar nossa aplicação em camadas deixa nosso código mais organizado e reutilizável e entender oque é `CRUD`

_Nota: Como foi feito na seção anterior haverá uma cópia do projeto para que seja possível manter um histórico, nesta seção o nome do projeto será `páginasCrud`_

## Oque é `CRUD`
`CRUD` é um estilo de aplicação que nos permite **Criar**, **Ler**, **Atualizar** e **Deletar** dados de uma certa fonte como banco de dados ou nesse inicio de nosso projeto em uma lista, mas também podemos colocar em uma arquivo pasta entre outros, em desenvolvimento web geralmente associamos isso a métodos HTTP, isso é uma grande parte de como a web funciona e de seus padrões 
- `C`: `CREATE`, ato de **Criar** dados novos, geralmente isso esta associado ao método `POST`, isso seria inserir dados em um banco de dados
- `R`: `READ`, ato de **Ler** dados, geralmente esta associado ao método `GET`, isso seria ler dados do banco de dados
- `U`: `UPDATE`, ato de **Atualizar** dados, geralmente isso esta associado aos métodos `PUT` ou `PATCH`, onde `PUT` é para atualização completa de um dado e `PATCH` é para atualização parcial de um dado
- `D`: `DELETE`, ato de **Deletar** dados, geralmente isso esta associado ao método `DELETE`, isso seria dar um `delete` em dados de um banco de dados
## Aplicando `CRUD`
Agora que entendemos oque é `CRUD`, podemos colocar nossos conhecimentos em prática.

Nesse momento já estamos aplicando um dos métodos em nossas páginas, o `GET`, que é buscar dados ou o simples fato de busca a página em si, mas ainda não podemos por exemplo refrescar os dados das páginas que seria uma ação que os usuários poderiam clicar em um botão e dados novos aparecerão

### Identificar Dados Novos
Como no nosso caso estamos lidando com dados que vem de uma lista fixa neste primeiro momento, vamos criar um campo que muda a cada chamada, no meu caso será um campo de numero da sorte, este será um campo que irá gerar um numero aleatório sempre que consultarmos uma pessoa, o código ficará o seguinte
```java
package com.dto;

public class PessoaDTO {
    private String nome;
    private int idade;
    

    public PessoaDTO() {
    }

    public PessoaDTO(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

	// gera numero randomico de 1 a 100
    public int getNumeroSorte() {
        return (int) (Math.random() * 100);
    }
}
```
E agora podemos usar em nossas telas `ssrPessoa.jsp` 
```jsp
<html>
<body>
    <a href="index.jsp"><-- Back to Index</a>

    <ul>
        <%
            // importando as classes necessárias com caminho completo Ex.: java.util.List
            // assim JSP entende qual classe usar
            java.util.List<com.dto.PessoaDTO> pessoas = com.controllers.PessoaController.getPessoasAsync();
            for (var pessoa : pessoas) {
        %>
            <li class="blue-text">
                <span>Name: <%= pessoa.getNome() %></span>
                -
                <span>Age: <%= pessoa.getIdade() %></span>
                - 
                <span>Can Drink: <%= com.controllers.PessoaController.calcularPodeBeber(pessoa.getIdade()) ? "Yes" : "No" %></span>
                -
                <span>Lucky Number: <%= pessoa.getNumeroSorte() %></span>

            </li>
        <%
            }
        %>
    </ul>
</body>
</html>
```
e `csrPessoa.html` 
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>Pessoas CSR</h1>
    <ul id="pessoas">
        
    </ul>
    <script>
        fetch('api/pessoa')
            .then(response => response.json())
            .then(data => {
                const pessoasDiv = document.getElementById('pessoas');
                data.forEach(pessoa => {
                    const li = document.createElement('li');
                    li.className = 'blue-text';
                    li.innerHTML = `Name: ${pessoa.nome} - Age: ${pessoa.idade} - Can Drink: ${pessoa.podeBeber ? 'Yes' : 'No'} - Lucky Number: ${pessoa.numeroSorte}`;
                    pessoasDiv.appendChild(li);
                });
            });
    </script>
</body>
</html>
```
Para funcionar na página CSR devemos colocar a nova propriedade no servlet
```java
package com.servletApi;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServlet", urlPatterns = {"/api/pessoa"})
public class PessoaServlet extends HttpServlet {
    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();
        
        try(PrintWriter out = res.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();

            // mapeia valores para json
            String json = mapper.writeValueAsString(
                pessoas.stream().map(pessoa -> { 
                    ObjectNode obj = mapper.createObjectNode();

                    // adiciona as propriedades
                    obj.put("nome", pessoa.getNome());
                    obj.put("idade", pessoa.getIdade());
                    obj.put("podeBeber", PessoaController.calcularPodeBeber(pessoa.getIdade()));
                    // novo campo 
                    obj.put("numeroSorte", pessoa.getNumeroSorte());

                    return obj;
                })
                .collect(Collectors.toList())
            ); 

            out.print(json);
            out.flush(); 
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
```
Após essas pequenas alterações devemos ter as duas páginas funcionando com o novo dado de número da sorte e sempre que apertarmos `F5` para atualizar a página aparecerá um novo número

### Adicionando Ações na página
Agora que temos como identificar dados que mudam de tempos em tempos e podemos ter certeza de que não estamos simplesmente pegando dados desatualizados, podemos adicionar botões em nossas páginas

#### Botão `Refresh`
Nossa primeira ação será de refrescar os dados, então o fluxo será bem simples, usuário clica no botão e a página traz dados novos de alguma forma, vamos então criar um botão em para as duas páginas (`SSR` e `CSR`), vamos fazer primeiramente a `SSR`, onde a ação será simplesmente fazer a mesma ação que nosso `F5`, porem com um botão na página

Vamos colocar o seguinte código logo abaixo do botão de voltar para home, tela `ssrPessoa.jsp`
```jsp
<a href="index.jsp"><-- Back to Index</a>
<%-- quebra linha --%>
<br> 
<%-- botão de refresh --%>
<button type="button" onclick="location.reload();">Refresh</button>
<%-- botão de refresh --%>
<%-- resto do código ... --%>
```
Dessa forma nossa página `SSR` já estará trazendo dados novos, agora vamos para a página `CSR`, para ele vamos criar uma função para mostrar a mensagem de `loading...`, mas em geral terá a mesma funcionalidade
```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <a href="index.jsp">Back to Index</a>
    <!-- quebra linha -->
    <br>
    <!-- botão de refresh -->
    <button type="button" onclick="buscarPessoas();">Refresh</button>
    <h1>Pessoas CSR</h1>
    <div id="pessoas">
        Loading...
    </div>

    <script>
	    // nova função criada
        function buscarPessoas() {
            // subimos a busca da pessoasDiv
            const pessoasDiv = document.getElementById('pessoas');
            pessoasDiv.innerHTML = 'Loading...';
            fetch('api/pessoa')
                .then(response => response.json())
                .then(data => {
                    const pessoasUl = document.createElement("ul");
                    data.forEach(pessoa => {
                        const li = document.createElement('li');
                        li.innerHTML = `Name: ${pessoa.nome} - Age: ${pessoa.idade} - Can Drink: ${pessoa.podeBeber ? 'Yes' : 'No'} - Lucky Number: ${pessoa.numeroSorte}`;
                        pessoasUl.appendChild(li);
                    });
                    pessoasDiv.innerHTML = pessoasUl.outerHTML;
                });
        }
		// usamos a nova função quando a página é iniciada
		// assim os dados são automaticamente buscados
        buscarPessoas();
    </script>
</body>
</html>
```
#### Botão `Criar`
Para o botão criar, teremos que criar uma nova funcionalidade em nosso sistema, esta funcionalidade precisara inserir uma nova pessoa em nossa lista que esta simulando um banco de dados

Para isso vamos trabalhar da camada mais profunda (`Model`) até a camada mais externa (`páginas` e `servlets`). Primeiro vamos criar um método que insere em nossa lista na `Model` 
```java
package com.models;

import java.util.ArrayList;
import java.util.List;

import com.dto.PessoaDTO;

public final class PessoaModel {
    private static final int DELAY = 2000; // 2 segundos

    // lista estática de pessoas
    // inicializada com alguns dados de exemplo
    // não irá mudar durante a execução do programa
    private static List<PessoaDTO> pessoas = new ArrayList<PessoaDTO>() {
        {
            add(new PessoaDTO("João", 30));
            add(new PessoaDTO("Maria", 25));
            add(new PessoaDTO("José", 40));
        }
    };

    // constructor privado para evitar instância
    private PessoaModel() {}

    // método para listar as pessoas
    public static List<PessoaDTO> getPessoas() {
        try {
            Thread.sleep(DELAY); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pessoas;
    }

    // adiciona uma nova pessoa à lista
    // retorna true se a adição for bem-sucedida
    // delay para simular uma operação de longa duração
    public static boolean addPessoa(PessoaDTO pessoa) {
        try {
            Thread.sleep(DELAY); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pessoas.add(pessoa);
    }
}
```
Agora que temos como inserir nossa lista podemos criar um método em nossa `controller` que vai fazer as validações e depois chamará a `model` para inserir nosso registro;

A `controller`serve para que possamos replicar uma funcionalidade em vários pontos com suas validações sempre sendo consistentes, em todos os pontos. 
```java 
package com.controllers;

import java.util.List;

import com.dto.PessoaDTO;
import com.models.PessoaModel;

// 
public final class PessoaController {

    public static List<PessoaDTO> getPessoasAsync() {
        // Chama o método estático da PessoaModel para obter a lista de pessoas
        var pessoas = PessoaModel.getPessoas();

        return pessoas;
    }

    public static Boolean addPessoaAsync(PessoaDTO pessoa) {
        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            return false; // Nome inválido
        }
        if (pessoa.getIdade() < 0) {
            return false; // Idade inválida

        }
        var sucesso = PessoaModel.addPessoa(pessoa);

        return sucesso;
    }

    public static boolean calcularPodeBeber(int idade) {
        // Verifica se a idade é maior ou igual a 18
        return idade >= 18;
    }
}
```
Agora que temos nossa `controller` funcional podemos partir para o desenvolvimento nas páginas e `servlets`

Para que possamos inserir dados vamos criar um formulário acima da lista com os campos de idade, nome e um botão para gravar

Como vamos desenvolver primeiro para o modo `SSR` vamos ter uma dificuldade comum em aplicações que usam apenas ela para renderizar seus conteúdos, que é a de executar ações, para que possamos fazer isso com uma pagina inteiramente `SSR`, vamos precisar criar uma servlet para a ação que queremos executar e ela irá mandar o a mensagem de sucesso ou erro para nossa pagina, para que isso possa acontecer precisamos antes pegar um parâmetro enviado para a pagina e mostrar 

Uma forma comum e simples nesse tipo de aplicação e que pode ser atingida sem nenhuma biblioteca são atributos, neste caso dei o nome do atributo de `msg`, validei se ele não é nulo e caso não seja, usa o texto na tela
```jsp
<html>
<body>
    <a href="/paginasCrud/index.jsp"><-- Back to Index</a>
    <br>
    <h1>Create Person</h1>
    <form action="/paginasCrud/api/pessoassr" method="post">
        <label for="nome">Name:</label>
        <input type="text" id="nome" name="nome" required>
        <br>
        <label for="idade">Age:</label>
        <input type="number" id="idade" name="idade" required>
        <br>
        <button type="submit">Create</button>
    </form>
    <br>
    <%
	    // uso do attribute msg
        String msg = (String) request.getAttribute("msg");
        if (msg != null) {
    %>
        <div>
            <p><%= msg %></p>
        </div>
    <%
        }
    %>
    <button type="button" onclick="location.reload();">Refresh</button>
    <h1>List of People</h1>
    <ul>
        <%
            java.util.List<com.dto.PessoaDTO> pessoas = com.controllers.PessoaController.getPessoasAsync();
            for (var pessoa : pessoas) {
        %>
            <li class="blue-text">
                <span>Name: <%= pessoa.getNome() %></span>
                -
                <span>Age: <%= pessoa.getIdade() %></span>
                - 
                <span>Can Drink: <%= com.controllers.PessoaController.calcularPodeBeber(pessoa.getIdade()) ? "Yes" : "No" %></span>
                -
                <span>Lucky Number: <%= pessoa.getNumeroSorte() %></span>
            </li>
        <%
            }
        %>
    </ul>
</body>
</html>
```
_Nota: Como você já deve ter percebido  alterei os caminhos para que fossem absolutos. ou seja, invés de `index.jsp` foi usado `/paginasCrud/index.jsp`, isso acontece para que tenhamos certeza que os caminhos não serão alterados após fazer chamadas_

Então agora que temos nosso formulário apontando para um de nossos servlets que servirá a mesma pagina `SSR` e a mensagem de sucesso/erro, podemos partir para o código do servlet

O que precisamos fazer é o seguinte, devemos pegar os parâmetros enviados pelos formulários, usar nossa `controller` para validar antes da inserção, nossa `controller` irá chamar a `Model` para inserir dados e iremos retornar uma pagina (no caso a mesma que já tínhamos) com um `attribute` de `msg` para usarmos na nossa página  
```java
package com.servletApi;

import java.io.PrintWriter;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServletssr", urlPatterns = {"/api/pessoassr"})
public class PessoaServletssr extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {

        try (PrintWriter out = res.getWriter()) {
	        // pega parametros
            String nome = req.getParameter("nome");
            String idade = req.getParameter("idade");

			// monta objeto de pessoa
			PessoaDTO pessoa = new PessoaDTO(nome, Integer.parseInt(idade));
			
			// adiciona a pessoa e define qual será a mensagem
            // Adiciona a pessoa
            var sucesso = PessoaController.addPessoaAsync(pessoa);
            var msg = "";
            if (sucesso) {
                // Responde com sucesso e status de criado
                res.setStatus(HttpServletResponse.SC_CREATED);
                msg = "Pessoa adicionada com sucesso!";
                
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                msg = "Erro ao adicionar pessoa!";
            }

			// coloca o atributo msg
            req.setAttribute("msg", msg);
            // busca a pagina que usaremos na resposta
            RequestDispatcher rd = req.getRequestDispatcher("/ssrPessoa.jsp");
            // manda a pagina com o atributo para o usuario
            rd.forward(req, res);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }
}
```
_Nota: Para esse tipo de retorno é recomendado utilizar um objeto de retorno que diga o tipo de erro que deu, também para avisar o usuário de qual foi o problema, mas pela simplicidade, estamos fazendo desta forma com um valor booleano_

_Nota 2: Note que como na tela usamos o `POST` como `method` estamos utilizando `doPost` do servlet, pois isso é uma ação de **Criação**_

_Nota 3: `res.setStatus`, serve para que possamos retornar códigos de status de sucesso/erro especifico, onde valores de 200 a 299 são resultados de sucesso, de 300 a 399 são resultados para redirecionamento, de 400 a 499 são erros que foram causados por algo do usuário e 500 as 599 são erros que aconteceram por falhas no servidor. Para servlets temos o objeto estático `HttpServletResponse` que da nomes de alguns dos status mais usados em aplicações como por exemplo o `HttpServletResponse.SC_CREATED` que representa o status `201` que é o status de **criado** ou o `HttpServletResponse.SC_NOT_FOUND` que representa o status `404` de nenhum valor encontrado/não existe. Uma coisa a se notar é que as bases são status genéricos como por exemplo `200` é sucesso, `400` é erro causado pelo usuário e `500` é erro no servidor_

Com isso feito podemos criar uma nova pessoa com nosso formulário, note que ao criar a pessoa, a tela irá "piscar", isso ocorre pois a tela será refeita pelo servidor, portanto o cliente terá que baixar novamente a pagina com o novo conteúdo, no caso a mensagem do `attribute` `msg` e o novo registro da lista

##### Em uma Página `CSR`
Para a pagina `CSR` devemos devemos apenas criar um novo endpoint em nosso servlet que envie um `JSON` com a resposta de sucesso ou erro

Em nosso `PessoaServlet` vamos criar o método `doPost`, que não muda muito de nossa forma de buscar dados que usamos anteriormente
```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse res) {
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    try (PrintWriter out = res.getWriter()) {
        ObjectMapper mapper = new ObjectMapper();
        PessoaDTO pessoa = mapper.readValue(req.getInputStream(), PessoaDTO.class);

        // Adiciona a pessoa
        var sucesso = PessoaController.addPessoaAsync(pessoa);
        var mensagem = "";
        if (sucesso) {
            // Responde com sucesso e status de criado
            res.setStatus(HttpServletResponse.SC_CREATED);
            mensagem = "Pessoa adicionada com sucesso!";
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mensagem = "Erro ao adicionar pessoa!";
        }
        out.print("{\"message\": \"" + mensagem + "\"}");
        out.flush();
    } catch (Exception e) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        e.printStackTrace();
    }
}
```
E nossa página ganhará uma nova função que será para fazer a requisição para o servlet, uma `div` para a mensagem e nosso formulário não apontando para um endpoint mais sim para essa função que chamará o servlet
```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <a href="index.jsp">Back to Index</a>
    <!-- quebra linha -->
    <br>
    <h1>Create Person</h1>
    <!-- formulário de criação -->
    <form onsubmit="adicionarPessoa(); return false;">
        <label for="nome">Name:</label>
        <input type="text" id="nome" name="nome" required>
        <br>
        <label for="idade">Age:</label>
        <input type="number" id="idade" name="idade" required>
        <br>
        <button type="submit">Create</button>
    </form>
    <br>
    <div id="mensagem"></div>
    <!-- botão de refresh -->
    <button type="button" onclick="buscarPessoas();">Refresh</button>
    <h1>Pessoas CSR</h1>
    <div id="pessoas">
        Loading...
    </div>

    <script>
        <!-- função anterior de get -->

        // função pra adicionar uma pessoa
        function adicionarPessoa() {
            const nome = document.getElementById('nome').value;
            const idade = document.getElementById('idade').value;
            // envia uma requisição POST para o servidor
            fetch('api/pessoa', {
                method: 'POST', // diz que é POST
                headers: {
                    'Content-Type': 'application/json' // diz que o corpo da requisição é JSON
                },
                body: JSON.stringify({ nome: nome, idade: idade }) // converte o objeto para JSON
            })
            .then(response => {
                // verifica se a resposta foi bem sucedida
                if (response.status > 499) {
                    throw new Error('Network response was not ok');
                }

                buscarPessoas(); // atualiza a lista de pessoas

                const mensagemDiv = document.getElementById('mensagem');
                response.json()
                    .then(data => {
                        // exibe a mensagem de sucesso ou erro
                        mensagemDiv.innerHTML = data.message || 'Person added successfully!';
                    })
            })
            .catch(error => console.error('Error:', error));
        }

        buscarPessoas();

    </script>
</body>

</html>
```
Uma coisa interessante de se notar é que dessa forma diferente do `SSR` não ocorre a "piscada" na tela e nos permite adicionar inúmeras interações enquanto ocorre o carregamento

Outra coisa a se notar é que, se tentarmos inserir uma pessoa com informações incorretas como por exemplo, uma idade menor que `0` vamos receber um erro e não conseguiremos inserir graças a nossa regra na `controller`, isso irá ocorrer tanto na `SSR` quanto na `CSR`, pois nossas regras de negocio estão separadas de nossa camada de `View`   
#### Botão `Deletar`
Pra o botão deletar vamos adicionar novamente uma funcionalidade ao sistema, mas dessa vez vamos precisar definir como vamos identificar registros em nossa lista de dados, algumas opções são
- Utilizar o nome ou adicionar uma propriedade de CPF/RG, mas estes podem repetir ou até mesmo mudar ou perderem o sentido no futuro
- Ou a que vamos usar, criaremos uma propriedade ID que será fixo na unidade e fará sentido para nossa aplicação
Para criar esse IDs temos varias opções de tipos de dados
- `UUID`: significa Universally unique identifier, é como uma string de 16 bytes que sempre será unica, independente do sistema, é otina para microserviços ou sistemas com um grande numero de dados e em algumas versões facilita a ordenação e é fácil de ler, mas ocupa um espaço significativo para cada registro
- `BIGINT`(`long`): São números −9,223,372,036,854,775,808 a 9,223,372,036,854,775,807, são bons para sistemas que não tendem a crescer muito e são fáceis de ordenar, mas possuem um tamanho máximo fixo 
Neste tutorial vamos usar o `long` por simplicidade e fácil compreensão, mas sempre ao fazer o design de um sistema é bom prestar atenção nesses tipos de detalhe e entender qual atende a sua necessidade

Para adicionarmos um `long` em nossa classe pessoa a unica coisa que precisamos fazer é adicionar nossa propriedade `id` com o tipo `long` e criar um construtor com 
```java
package com.dto;

public class PessoaDTO {
    private String nome;
    private int idade;
    private long id; // nova propriedade com get, set e construtor

	public PessoaDTO(long id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }
    
    public PessoaDTO(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    // gera numero randomico de 1 a 100
    public int getNumeroSorte() {
        return (int) (Math.random() * 100);
    }
}
```
Vamos agora adicionar a propriedade id em nossa lista de dados de teste e vamos incrementar toda vez que um registro for inserido com uma variável estática 
```java
package com.models;

import java.util.ArrayList;
import java.util.List;

import com.dto.PessoaDTO;

public final class PessoaModel {
    private static final int DELAY = 2000; // 2 segundos
    private static long idPessoaAtual = 3; // ID inicial para a próxima pessoa

    // lista estática de pessoas
    // inicializada com alguns dados de exemplo
    // não irá mudar durante a execução do programa
    private static List<PessoaDTO> pessoas = new ArrayList<PessoaDTO>() {
        {
            add(new PessoaDTO(1, "João", 30));
            add(new PessoaDTO(2, "Maria", 25));
            add(new PessoaDTO(3, "José", 40));
        }
    };

    // constructor privado para evitar instância
    private PessoaModel() {}

    // método para listar as pessoas
    public static List<PessoaDTO> getPessoas() {
        try {
            Thread.sleep(DELAY); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // retorna pessoas
        return pessoas;
    }

    // adiciona uma nova pessoa à lista
    // retorna true se a adição for bem-sucedida
    // delay para simular uma operação de longa duração
    public static boolean addPessoa(PessoaDTO pessoa) {
        try {
            Thread.sleep(DELAY); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // incrementa o ID atual
        idPessoaAtual++;
        // adiciona um ID único à pessoa
        pessoa.setId(idPessoaAtual);
        return pessoas.add(pessoa);
    }
}
```
Agora que conseguimos adicionar a pessoa precisamos criar o método de deleção baseado no ID de uma pessoa, isso nos dará a seguinte função
```java
// remove uma pessoa da lista pelo ID
// retorna true se a remoção for bem-sucedida
// delay para simular uma operação de longa duração
public static boolean removePessoa(long id) {
    try {
        Thread.sleep(DELAY);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    // busca a pessoa pelo ID e remove
    return pessoas.removeIf(p -> p.getId() == id);
}
```
Com nosso funcionalidade de deleção concluída na `Model` podemos passar para a `controller`, onde poderíamos verificar se existiria uma condição onde a pessoa não pode ser deletada, neste caso não temos isso portanto vou fazer da seguinte forma
```java
public static boolean removePessoaAsync(long id) {
    // Chama o método estático da PessoaModel para remover a pessoa pelo ID
    return PessoaModel.removePessoa(id);
}
```
Vamos partir então para o servlet da página `SSR`, nesta parte teremos que fazer uma coisa um pouco diferente do que estivemos fazendo até agora, invés de usarmos a classe `PessoaServletssr` vamos ter que criar um novo servlet chamado `PessoaServletDeletessr`, isso porque para formulários `HTML` comuns, os métodos `PUT` e `DELETE` não são reconhecidos, necessitando de seus próprios arquivos servlet que utilizem o método `POST` para executar suas ações que irá chamar nossa funcionalidade na `controller` e irá nos mostrar uma mensagem de erro ou de sucesso
```java
package com.servletApi;

import java.io.PrintWriter;

import com.controllers.PessoaController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// url com * significa que é dinâmica portanto pode ser passado qualquer valor
@WebServlet(name = "PessoaServletDeletessr", urlPatterns = {"/api/pessoassr/delete/*"})
public class PessoaServletDeletessr extends HttpServlet {

    // usa post pois html não suporta metodo DELETE
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        try (PrintWriter out = res.getWriter()) {
            long id = 0;
            var msg = "";

            // Verifica se o ID foi passado na URL
            try {
                String idStr = req.getPathInfo().split("/")[1];
                id = Long.parseLong(idStr);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                msg = "ID inválido!";
            }


            // Remove a pessoa
            var sucesso = PessoaController.removePessoaAsync(id);
            if (sucesso) {
                // Responde com sucesso e status de OK
                res.setStatus(HttpServletResponse.SC_OK);
                msg = "Pessoa removida com sucesso!";
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                msg = "Pessoa não encontrada!";
            }

            req.setAttribute("msg", msg);
            RequestDispatcher rd = req.getRequestDispatcher("/ssrPessoa.jsp");
            rd.forward(req, res);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }
}
```
Para usarmos isso na página teremos que criar um formulário ao lado das informações da pessoa com apenas um botão de submit para o formulário, o método deste formulário será `POST` e a `action` será dinâmica com o ID da pessoa, assim cada item da lista vai ficar da seguinte forma
```jsp
<li>
    <span>Name: <%= pessoa.getNome() %></span>
    -
    <span>Age: <%= pessoa.getIdade() %></span>
    - 
    <span>Can Drink: <%= com.controllers.PessoaController.calcularPodeBeber(pessoa.getIdade()) ? "Yes" : "No" %></span>
    -
    <span>Lucky Number: <%= pessoa.getNumeroSorte() %></span>
    -
    <!-- action de delete com o id da pessoa -->
    <form action="/paginasCrud/api/pessoassr/delete/<%= pessoa.getId() %>" method="post" style="display:inline;">
        <button type="submit">X</button>
    </form>
</li>
```
Desta forma poderemos fazer o split da URL enviada ao servlet e olhando neste caso na posição `[1]`, podemos pegar o ID da remoção
##### Em uma Página `CSR`
Para termos a mesma funcionalidade de em uma pagina `CSR` é um poco menos complicado, oque precisamos fazer é deixar o mesmo servlet (`PessoaServlet`) com a url dinâmica, ou seja, colocar o `/*` ao final, assim podemos receber o ID a ser removido, e poderemos adicionar o método `doDelete`
```java
@Override
protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    long id = 0;
    var msg = "";

    try (PrintWriter out = res.getWriter()) {
        // Verifica se o ID foi passado na URL
        try {
            String idStr = req.getPathInfo().split("/")[1];
            id = Long.parseLong(idStr);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            msg = "ID inválido!";
        }

        boolean sucesso = PessoaController.removePessoaAsync(id);

        if (sucesso) {
            res.setStatus(HttpServletResponse.SC_OK);
            msg = "Pessoa removida com sucesso!";
        } else {
            msg = "Pessoa não encontrada!"; 
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        out.print("{\"message\": \"" + msg + "\"}");
        out.flush();
    } catch (Exception e) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        e.printStackTrace();
    }
}
```
E para o arquivo `HTML` vamos apenas adicionar o botão do tipo `button`, para não "piscar" a tela, e que execute a função que irá chamar o servlet para executar a tarefa, lembrando que tudo isso é adicionado via JavaScript na função `buscarPessoas()`, portanto é necessario mapear o novo campo de ID no método `doGet` do servlet

**PessoaServlet**
```java
@Override 
protected void doGet(HttpServletRequest req, HttpServletResponse res) {

    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    List<PessoaDTO> pessoas = PessoaController.getPessoasAsync();
    
    try(PrintWriter out = res.getWriter()) {
        ObjectMapper mapper = new ObjectMapper();

        // mapeia valores para json
        String json = mapper.writeValueAsString(
            pessoas.stream().map(pessoa -> { 
                ObjectNode obj = mapper.createObjectNode();

                // adiciona as propriedades
                obj.put("nome", pessoa.getNome());
                obj.put("idade", pessoa.getIdade());
                obj.put("podeBeber", PessoaController.calcularPodeBeber(pessoa.getIdade()));
                obj.put("numeroSorte", pessoa.getNumeroSorte());
                obj.put("id", pessoa.getId());

                return obj;
            })
            .collect(Collectors.toList())
        ); 

        out.print(json);
        out.flush(); 
    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
}
```

**csrPessoa.html**
```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <a href="index.jsp">Back to Index</a>
    <!-- quebra linha -->
    <br>
    <h1>Create Person</h1>
    <form onsubmit="adicionarPessoa(); return false;">
        <label for="nome">Name:</label>
        <input type="text" id="nome" name="nome" required>
        <br>
        <label for="idade">Age:</label>
        <input type="number" id="idade" name="idade" required>
        <br>
        <button type="submit">Create</button>
    </form>
    <br>
    <div id="mensagem"></div>
    <!-- botão de refresh -->
    <button type="button" onclick="buscarPessoas();">Refresh</button>
    <h1>Pessoas CSR</h1>
    <div id="pessoas">
        Loading...
    </div>

    <script>
        function buscarPessoas() {
            const pessoasDiv = document.getElementById('pessoas');
            pessoasDiv.innerHTML = 'Loading...';
            fetch('api/pessoa')
                .then(response => response.json())
                .then(data => {
                    const pessoasUl = document.createElement("ul");
                    data.forEach(pessoa => {
                        const li = document.createElement('li');
                        li.innerHTML = `
                            Name: ${pessoa.nome} - 
                            Age: ${pessoa.idade} - 
                            Can Drink: ${pessoa.podeBeber ? 'Yes' : 'No'} - 
                            Lucky Number: ${pessoa.numeroSorte} 
                            <button type="button" onclick="removerPessoa(${pessoa.id})">X</button>`;
                        pessoasUl.appendChild(li);
                    });
                    pessoasDiv.innerHTML = pessoasUl.outerHTML;
                });
        }

        // função pra adicionar uma pessoa

        // função para remover uma pessoa
        function removerPessoa(id) {
            fetch(`api/pessoa/${id}`, {
                method: 'DELETE' // diz que é DELETE
            })
            .then(response => {
                // verifica se a resposta foi bem sucedida
                if (response.status > 499) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const mensagemDiv = document.getElementById('mensagem');
                // exibe a mensagem de sucesso ou erro
                mensagemDiv.innerHTML = data.message || 'Person removed successfully!';
                buscarPessoas(); // atualiza a lista de pessoas
            })
            .catch(error => console.error('Error:', error));
        }

        buscarPessoas();

    </script>
</body>

</html>
```
Mas com nossas alterações nas propriedades do `PessoaDTO` acabamos quebrando a funcionalidade do método `POST` que usava um mapper (um objeto que nos ajuda a mapear propriedades mais facilmente), isso acontece pois estamos criando uma pessoa, mas não estamos enviando um ID, oque acaba deixando o mapper confuso, para resolvermos isso precisamos mudar a forma com a qual fazemos esse mapeamento, no caso vou fazer de forma manual, mas usando a biblioteca `jackson` 
```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse res) {
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");

    try (PrintWriter out = res.getWriter()) {
	    // deixamos o mapper declarado pois vamos usar ele
        ObjectMapper mapper = new ObjectMapper();

		// declara pessoa de forma anonima
        PessoaDTO pessoa;
        try {
	        // mapeia o json
            var jsonNode = mapper.readTree(req.getReader());
            String nome = jsonNode.get("nome").asText(); // pega nome do json como texto
            int idade = jsonNode.get("idade").asInt(); // pega idade do json como int
            pessoa = new PessoaDTO(nome, idade); // mapeia manualmente
        } catch (Exception e) {
	        // caso de um erro no mapeamento
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\": \"Erro ao processar os dados!\"}");
            out.flush();
            return;
        }

        // Adiciona a pessoa
        var sucesso = PessoaController.addPessoaAsync(pessoa);
        var mensagem = "";
        if (sucesso) {
            // Responde com sucesso e status de criado
            res.setStatus(HttpServletResponse.SC_CREATED);
            mensagem = "Pessoa adicionada com sucesso!";
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mensagem = "Erro ao adicionar pessoa!";
        }
        out.print("{\"message\": \"" + mensagem + "\"}");
        out.flush();
    } catch (Exception e) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        e.printStackTrace();
    }
}
```
Finalizando isso teremos nossa tela `CSR` com as mesmas funcionalidades que a `SSR`, novamente oque foi possível notar foi que conseguimos evitar a "piscada" da tela, pois as requisições estão sendo feitas via JS

#### `Edição` de Registros
Nosso site esta quase perfeito, agora a unica ação que falta nele é a edição de registros para fazer isso vamos adicionar a funcionalidade na `Model` editando o registro em nossa lista
```java
// atualiza uma pessoa na lista pelo ID
// retorna true se a atualização for bem-sucedida
// delay para simular uma operação de longa duração
public static boolean updatePessoa(long id, PessoaDTO pessoaAtualizada) {
    try {
        Thread.sleep(DELAY);
    } catch (InterruptedException e) {
        e.printStackTrace();
        return false;
    }

    // busca a pessoa pelo ID e atualiza
    Optional<PessoaDTO> pessoaOpt = pessoas.stream().filter(p -> p.getId() == id).findFirst();
    if (pessoaOpt.isPresent()) {
        PessoaDTO pessoa = pessoaOpt.get(); // pega valor da lista
        pessoa.setNome(pessoaAtualizada.getNome()); // troca nome
        pessoa.setIdade(pessoaAtualizada.getIdade()); // troca idade
        return true;
    }
    return false;
}
```
Agora para a `Controller` vamos mudar a forma que fazemos a validação, invés de deixar em uma unica funcionalidade teremos a mesma validação para duas funcionalidade, portanto vamos passar para um método privado em nossa `controller` e usa-lo no método de criação e no novo método de atualização, assim ficaremos com nossa `controller` final da seguinte forma
```java
package com.controllers;

import java.util.List;

import com.dto.PessoaDTO;
import com.models.PessoaModel;

// 
public final class PessoaController {

    public static List<PessoaDTO> getPessoasAsync() {
        var pessoas = PessoaModel.getPessoas();

        return pessoas;
    }

	// método de adicionar pessoa atualizado
    public static Boolean addPessoaAsync(PessoaDTO pessoa) {
        var resultadoValidacao = validarPessoa(pessoa);

        if (!resultadoValidacao) {
            return false; // Pessoa inválida
        }

        var sucesso = PessoaModel.addPessoa(pessoa);
        return sucesso;
    }

    public static boolean removePessoaAsync(long id) {
        return PessoaModel.removePessoa(id);
    }

    public static boolean calcularPodeBeber(int idade) {
        return idade >= 18;
    }

	// novo método de atualizar pessoa
    public static boolean atualizarPessoaAsync(long id, PessoaDTO pessoa) {
        // Verifica se a pessoa é válida
        if (!validarPessoa(pessoa)) {
            return false; // Pessoa inválida
        }

        // atualiza a pessoa pelo ID
        var sucesso = PessoaModel.updatePessoa(id, pessoa);
        return sucesso;
    }

	// novo método de validação
    private static boolean validarPessoa(PessoaDTO pessoa) {
        // Verifica se o nome não é nulo ou vazio
        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            return false;
        }
        // Verifica se a idade é maior ou igual a 0
        if (pessoa.getIdade() < 0) {
            return false;
        }
        return true;
    }
}
```
Desta forma deixamos nossa `controller` ainda mais reutilizável e poderemos passar para nossa camada de `View`, mas agora para que possamos atualizar uma pessoa de forma a usar apenas o método `SSR` vamos ter que fazer duas coisas, uma delas já fizemos quando criamos a funcionalidade de deletar pessoa que será o `POST` para atualização, então vamos ter que criar um novo arquivo, neste exemplo vamos chamar de `PessoaServletUpdate` e deveremos pegar o ID passado pela URL e pegaremos os outros parâmetros pelo body da requisição que serão nossos formulários de `nome` e `idade`
```java
package com.servletApi;

import java.io.PrintWriter;

import com.controllers.PessoaController;
import com.dto.PessoaDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PessoaServletUpdate", urlPatterns = {"/api/pessoassr/update/*"})
public class PessoaServletUpdate extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        try (PrintWriter out = res.getWriter()) {
            long id = 0;
            var msg = "";

            // Verifica se o ID foi passado na URL
            try {
                String idStr = req.getPathInfo().split("/")[1];
                id = Long.parseLong(idStr);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                msg = "ID inválido!";
            }
            
            // Verifica se o nome e a idade foram passados no body da requisição
            String nome = req.getParameter("nome");
            String idade = req.getParameter("idade");

            PessoaDTO pessoa = new PessoaDTO(nome, Integer.parseInt(idade));


            // Atualiza a pessoa
            var sucesso = PessoaController.atualizarPessoaAsync(id, pessoa);
            if (sucesso) {
                // Responde com sucesso e status de OK
                res.setStatus(HttpServletResponse.SC_OK);
                msg = "Pessoa atualizada com sucesso!";
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                msg = "Pessoa não encontrada ou dados inválidos!";
            }

            req.setAttribute("msg", msg);
            RequestDispatcher rd = req.getRequestDispatcher("/ssrPessoa.jsp");
            rd.forward(req, res);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }
}
```
Agora que nosso servlet já esta preparado para atualizar uma pessoa especificada na URL e pegar os parâmetros que mandaremos no body da requisição, podemos partir para a tela, nela vamos ter que trocar alguns dos valores que temos por inputs e colocar um botão de atualizar que aponte para nosso servlet de atualização
```jsp
<html>
<body>
    <a href="/paginasCrud/index.jsp"><-- Back to Index</a>
    <br>
    <h1>Create Person</h1>
    <form action="/paginasCrud/api/pessoassr" method="post">
        <label for="nome">Name:</label>
        <input type="text" id="nome" name="nome" required>
        <br>
        <label for="idade">Age:</label>
        <input type="number" id="idade" name="idade" required>
        <br>
        <button type="submit">Create</button>
    </form>
    <br>
    <%
        String msg = (String) request.getAttribute("msg");
        if (msg != null) {
    %>
        <div>
            <p><%= msg %></p>
        </div>
    <%
        }
    %>
    <button type="button" onclick="location.reload();">Refresh</button>
    <h1>List of People</h1>
    <ul>
        <%
            java.util.List<com.dto.PessoaDTO> pessoas = com.controllers.PessoaController.getPessoasAsync();
            for (var pessoa : pessoas) {
        %>
            <li>
                <div style="display:flex;">
                    <form action="/paginasCrud/api/pessoassr/update/<%= pessoa.getId() %>" method="post">
                        <span>Name: <input type="text" name="nome" value="<%= pessoa.getNome() %>" required></span>
                        -
                        <span>Age: <input type="number" name="idade" value="<%= pessoa.getIdade() %>" required></span>
                        -
                        <span>Can Drink: <%= com.controllers.PessoaController.calcularPodeBeber(pessoa.getIdade()) ? "Yes" : "No" %></span>
                        -
                        <span>Lucky Number: <%= pessoa.getNumeroSorte() %></span>
                        -
                        <button type="submit">Update</button>
                    </form>
                    <form action="/paginasCrud/api/pessoassr/delete/<%= pessoa.getId() %>" method="post" style="display:inline;">
                        <button type="submit">X</button>
                    </form>
                </div>
            </li>
        <%
            }
        %>
    </ul>
</body>
</html>
```
Com isso concluído podemos testar e veremos que que nossos registros poderão ser atualizados, como visto anteriormente com a "piscada", mas com a tela sendo sempre atualizada no servidor sem validação ou renderização na parte do cliente.
##### Em uma Pagina `CSR
Agora vamos partir para o `CSR`, teremos criar o método `doPut` em nosso `PessoaServlet`, ele irá pegar o id da pessoa da URL como fizemos com o `onDelete` e pegar os parâmetros do body como fizemos com o `doPost`
```java
@Override
protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    long id = 0;
    var msg = "";

    ObjectMapper mapper = new ObjectMapper();

    

    try (PrintWriter out = res.getWriter()) {
        // Verifica se o ID foi passado na URL
        try {
            String idStr = req.getPathInfo().split("/")[1];
            id = Long.parseLong(idStr);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            msg = "ID inválido!";
        }

		// pega parâmetros do body
        PessoaDTO pessoa;
        try {
            var jsonNode = mapper.readTree(req.getReader());
            String nome = jsonNode.get("nome").asText();
            int idade = jsonNode.get("idade").asInt();
            pessoa = new PessoaDTO(nome, idade);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\": \"Erro ao processar os dados!\"}");
            out.flush();
            return;
        }

        boolean sucesso = PessoaController.atualizarPessoaAsync(id, pessoa);

        if (sucesso) {
            res.setStatus(HttpServletResponse.SC_OK);
            msg = "Pessoa atualizada com sucesso!";
        } else {
            msg = "Pessoa não encontrada ou dados inválidos!";
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        out.print("{\"message\": \"" + msg + "\"}");
        out.flush();
    } catch (Exception e) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        e.printStackTrace();
    }
}
```

Com o servlet concluído vamos partir para a página, teremos o mesmo layout da tela que usamos `SSR` para nossos elementos, ou seja, teremos formulários e um botão de atualizar. Ao final devemos ter o seguinte código
```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <a href="index.jsp">Back to Index</a>
    <!-- quebra linha -->
    <br>
    <h1>Create Person</h1>
    <form onsubmit="adicionarPessoa(); return false;">
        <label for="nome">Name:</label>
        <input type="text" id="nome" name="nome" required>
        <br>
        <label for="idade">Age:</label>
        <input type="number" id="idade" name="idade" required>
        <br>
        <button type="submit">Create</button>
    </form>
    <br>
    <div id="mensagem"></div>
    <!-- botão de refresh -->
    <button type="button" onclick="buscarPessoas();">Refresh</button>
    <h1>Pessoas CSR</h1>
    <div id="pessoas">
        Loading...
    </div>

    <script>
	    // foi atualizado para ter a div e os inputs
        function buscarPessoas() {
            const pessoasDiv = document.getElementById('pessoas');
            pessoasDiv.innerHTML = 'Loading...';
            fetch('api/pessoa')
                .then(response => response.json())
                .then(data => {
                    const pessoasUl = document.createElement("ul");
                    data.forEach(pessoa => {
                        const li = document.createElement('li');
                        li.innerHTML = `
                            <div id="pessoa-${pessoa.id}">
                                Name: <input type="text" name="nome" value="${pessoa.nome}" /> - 
                                Age: <input type="number" name="idade" value="${pessoa.idade}" /> - 
                                Can Drink: ${pessoa.podeBeber ? 'Yes' : 'No'} - 
                                Lucky Number: ${pessoa.numeroSorte} 
                                <button type="button" onclick="atualizarPessoa(${pessoa.id})">Update</button>
                                <button type="button" onclick="removerPessoa(${pessoa.id})">X</button>
                            </div>`;
                        pessoasUl.appendChild(li);
                    });
                    pessoasDiv.innerHTML = pessoasUl.outerHTML;
                });
        }

        // função pra adicionar uma pessoa

        // função para remover uma pessoa

        // função para atualizar uma pessoa
        function atualizarPessoa(id) {
            // busca o seletor pelo id da div e os inputs de nome e idade
            const nome = document.querySelector(`#pessoa-${id} input[name="nome"]`).value;
            const idade = document.querySelector(`#pessoa-${id} input[name="idade"]`).value;
            
            fetch(`api/pessoa/${id}`, {
                method: 'PUT', // diz que é PUT
                headers: {
                    'Content-Type': 'application/json' // diz que o corpo da requisição é JSON
                },
                body: JSON.stringify({ nome: nome, idade: idade }) // converte o objeto para JSON
            })
            .then(response => {
                // verifica se a resposta foi bem sucedida
                if (response.status > 499) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const mensagemDiv = document.getElementById('mensagem');
                // exibe a mensagem de sucesso ou erro
                mensagemDiv.innerHTML = data.message || 'Person updated successfully!';
                buscarPessoas(); // atualiza a lista de pessoas
            })
            .catch(error => console.error('Error:', error));
        }

        buscarPessoas();

    </script>
</body>

</html>
```
## Conclusão 
Criamos duas páginas com as mesmas funcionalidades uma em que ocorre o chamado `postback` a que eu estava me referindo como "piscada" e outra página onde todas as ações eram realizadas por meio de JavaScript onde isso não ocorria, isso foi feito para mostrar que não existe apenas uma forma das coisas serem feitas e se você quisesse poderia até colocar os dois tipos na mesma página, onde para alguns ocorria o `postback` e outros não ocorreria ou onde o primeiro carregamento era com `SSR`, mas depois realizava as ações via `CSR`, decisões de qual tipo será utilizado em um projeto vão sempre depender das necessidades de seu software e negocio, oque foi mostrado aqui foi o básico das duas e como funcionariam softwares com diferentes estrategias. Também foi possível vermos um pouco sobre reutilização de código com nossas camadas e validações de inputs, com nossa `controller` que validava se o nome não era vazio e se a idade era positiva.

_Nota: Em muitos lugares da internet vão mostrar dados que não estão em uma lista, mas sim em um banco de dados e a atualização dos dados ocorre diretamente na camada de `view` da tela `.jsp`, tome cuidado pois isso é extremamente perigoso e também não permite uma validação de forma correta dos dados_

_Certifique-se de que não há nenhuma brecha para pessoas mal intencionadas tenham acesso aos dados que você esta guardando_

# Trabalhando com Arquivos Estáticos
Em desenvolvimento web temos alguns arquivos que são chamados de estáticos, ou seja, que não mudam independente da requisição, estes arquivos geralmente são arquivos `.js`, `.css`, `.pdf`, `.png` entre vários outros tipos que não serão dinâmicos em nossa aplicação, isso faz com que o possamos apenas buscar eles ou até mesmo mostrarmos eles para os usuários um exemplo disso seria um contrato de uso do site ou de LGPD

_Nota: Como foi feito na seção anterior haverá uma cópia do projeto para que seja possível manter um histórico, nesta seção o nome do projeto será `arquivosEstaticos`_

## Servindo Arquivos Estáticos
Para que possamos servir esses arquivos a unica coisa que precisamos fazer é adicionar ele na mesma pasta de nossas paginas, mas uma boa pratica para esse tipo de arquivo é criar uma pasta chamada `static/` e separar por tipos de arquivos como o exemplo abaixo
```
static/
-js/
--index.js
-css/
--index.css
-pdf/
--Contrato.pdf
--Lgpd.pdf
-data/
--links.json
-img/
--gatinho.png
--ok.jpg
```
Para vermos isso na prática vamos adicionar 3 tipos de arquivo 
1. Um que não é necessário para a aplicação funcionar
2. Um que é apenas um incomodo se não funcionar
3. Um que é essencial para o funcionamento da aplicação
### Arquivos Desnecessários para Funcionamento da Aplicação
Esses são arquivos por exemplo que não tem tanta prioridade no sistema como por exemplo uma imagem (`.png`, `.jpg`, etc...) ou um termo de uso que ninguém olha (`.pdf`), eles podem ser servidos de qualquer ligar seja a partir da pasta `static/` da sua aplicação ou de outro link externo, não importam tanto.

Vamos adicionar a pasta no projeto e um arquivo `.pdf` e um `.png`, o sistema de pastas deve ficar mais ou menos da seguinte forma, qualquer arquivo que você quiser os meus serão os seguintes:
```
webapp/
-static/
--pdf/
---Minuta-Padrao.pdf
--img/
---dog.png
-WEB-INF/
--web.xml
-csrPessoa.html
-index.jsp
-SSR.jsp
-ssrPessoa.jsp
```
Para acessar podemos colocar o caminho deles na URL de nosso site ou podemos criar um link, neste caso vou criar uma pagina chamada `StaticFiles.jsp` e dentro dela vou executar estes nossos testes com arquivos, vamos também adicionar nossa página no `index.jsp` para termos como acessar a página

**Index.jsp**
```jsp
<html>
<body>
    <h2>Hello World!</h2>

    <%= "Current time is: " + new java.util.Date() %>
    
    <ul>
        <li><a href="SSR.jsp">SSR</a></li>
        <li><a href="ssrPessoa.jsp">SSR Pessoas</a></li>
        <li><a href="csrPessoa.html">CSR Pessoas</a></li>
        <li><a href="StaticFiles.jsp">Static Files</a></li>
    </ul>
</body>
</html>
```

**StaticFiles.jsp**
```jsp
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div>
        <a href="static/pdf/Minuta-Padrao.pdf" target="_blank" rel="noopener noreferrer">View PDF</a>
    </div>
    <div>
        <img src="static/img/dog.png" alt="dog" />
    </div>
</body>
</html>
```

Estes arquivos não são necessários para nossa aplicação funcionar, portanto se eles por algum motivo sumirem ou serem modificados isso não implicara em nosso site.

_Você pode fazer um teste movendo esses arquivos para outro lugar por exemplo_

### Arquivos que Incomodam caso não Funcionem
Esses arquivos são arquivos que podem causar bugs visuais ou até mesmo tirar facilidades do usuário como por exemplo um arquivo `.css`, estes não implicam no funcionamento do site mas deixarão muito mais difícil de navegar por ele. Esse tipo de arquivo ainda pode ser colocado em outras fontes como `CDN`s ou como será o caso, em nossa aplicação. após criar o explorador de arquivos deve ficar assim:
```
webapp/
-static/
--css/
---index.css
--pdf/
---Minuta-Padrao.pdf
--img/
---dog.png
-WEB-INF/
--web.xml
-csrPessoa.html
-index.jsp
-SSR.jsp
-ssrPessoa.jsp
-StaticFiles.jsp
```

Dentro deste arquivo vamos colocar uma estilização simples, apenas para termos um lugar onde olhar
```css
input[type=text] {
  width: 100%;
  box-sizing: border-box;
  border: 2px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  background-color: white;
  background-image: url('/static/img/searchicon.png');
  background-position: 10px 10px; 
  background-repeat: no-repeat;
  padding: 12px 20px 12px 40px;
}
```
e vamos colocar na página `StaticFiles.jsp` o `input`
```jsp
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="static/css/index.css">
    <title>Document</title>
</head>
<body>
    <div>
        <a href="static/pdf/Minuta-Padrao.pdf" target="_blank" rel="noopener noreferrer">View PDF</a>
    </div>
    <div>
        <img src="static/img/dog.png" alt="dog" />
    </div>

	<!-- input inserido -->
    <form>
        <input type="text" name="search" placeholder="Search..">
    </form>
</body>
</html>
```
Este arquivo `.css` caso aconteça algo com ele resultará em um incomodo e provavelmente em reclamação dos usuários, mas não impede o funcionamento correto do site. Para testar pode ver a dica dos arquivos não necessários

### Arquivos Essênciais para Funcionamento
Por ultimo existem arquivos que são essênciais para o funcionamento do site, estes arquivos tem que ser garantidos que não irão falhar, maior parte das vezes é recomendável deixamos na sua aplicação que é um lugar onde você terá controle e poderá fazer alterações do seu agrando quando quiser. Esse tipo de arquivo geralmente são arquivos de dados de configuração ou funcionalidades do site, um exemplo são arquivos `.js`, como foi mostrado na seção anterior, JavaScript pode ser uma parte importante de como nossa aplicação funciona para o usuário dependendo da forma que é utilizado, então nestes casos precisamos garantir que temos controle e disponibilidade dele. Vamos criar um e ver como fica nossa pagina de teste
```
webapp/
-static/
--js/
---index.js
--css/
---index.css
--pdf/
---Minuta-Padrao.pdf
--img/
---dog.png
-WEB-INF/
--web.xml
-csrPessoa.html
-index.jsp
-SSR.jsp
-ssrPessoa.jsp
-StaticFiles.jsp
```

Vou utilizar a mesma função de listagem da pagina `CSR`, apenas retirando os inputs e botões
```js
function buscarPessoas(idElementoPessoas) {
    const pessoasDiv = document.getElementById(idElementoPessoas);
    pessoasDiv.innerHTML = 'Loading...';
    fetch('api/pessoa')
        .then(response => response.json())
        .then(data => {
            const pessoasUl = document.createElement("ul");
            data.forEach(pessoa => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <div id="pessoa-${pessoa.id}">
                        Name: ${pessoa.nome} - 
                        Age: ${pessoa.idade} - 
                        Can Drink: ${pessoa.podeBeber ? 'Yes' : 'No'} - 
                        Lucky Number: ${pessoa.numeroSorte} 
                    </div>`;
                pessoasUl.appendChild(li);
            });
            pessoasDiv.innerHTML = pessoasUl.outerHTML;
        });
}
```
Será necessário também fazer a referencia do arquivo `index.js`, adicionar a `div` que receberá as informações de pessoas e a chamada inicial para buscar as pessoas na página `StaticFiles.jsp`
```jsp
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="static/css/index.css">
    <title>Document</title>
</head>
<body>
    <div>
        <a href="static/pdf/Minuta-Padrao.pdf" target="_blank" rel="noopener noreferrer">View PDF</a>
    </div>
    <div>
        <img src="static/img/dog.png" alt="dog" />
    </div>

    <form>
        <input type="text" name="search" placeholder="Search..">
    </form>

    <h1>People List</h1>
    <div id="pessoas">
        Loading...
    </div>

    <script src="static/js/index.js"></script>
    <script>
        buscarPessoas('pessoas');
    </script>
</body>
</html>
```
Neste caso se nossa aplicação não encontrar o arquivo `static/js/index.js` acarretará em um erro e no não funcionamento da aplicação, esse é o pior dos casos onde o usuário é incapaz utilizar a aplicação

Para testar você pode usar o mesmo teste que nos outros dois casos

## Você Percebeu?
Caso tenha seguido a risca oque foi feito nesta seção ou pegado o código no Github, talvez não tenha percebido que, no segundo caso de arquivos que podem incomodar, foi utilizada uma imagem no estilo (`/static/img/searchicon.png`) que não estará aparecendo no site, mas os estilos ainda estarão funcionando, esse é um exemplo que pode acontecer no mundo real, onde varias pessoas não percebem que algo não esta lá

**_Aviso: Apesar de alguns arquivos não serem necessários para funcionamento do site ou não sejam tão perceptíveis, ainda precisamos garantir que tudo que fazemos é organizado e funcional, as vezes não é algo de tanta prioridade, pois o site ainda estará funcionando, mas sempre devemos presar pela perfeição. Lembre que quando outras pessoas veem nosso código ou nossos projetos elas julgam como trabalhamos e isso pode ter consequências de colegas, chefes e recrutadores_**

# Usando um Banco de Dados
Bancos de dados são ferramentas essenciais para a maior parte das aplicações eles nos permitem deixar nossos dados **persistentes**, isso significa que, podemos consultar dados salvos em nossos HDs e SSDs invés da memoria RAM.

_Nota: Como foi feito na seção anterior haverá uma cópia do projeto para que seja possível manter um histórico, nesta seção o nome do projeto será `bancoDeDados`_

## Mas Quais as Diferenças
Caso ainda não tenha notado, sempre que lançamos uma nova versão do nosso código para testarmos os dados que deletamos, adicionamos e alteramos voltam ao estado inicial, isso porque como estávamos em uma fase de teste estávamos usando uma lista estática, esta lista era guardada na memoria RAM do nosso computador, ou seja, eram dados **voláteis**, com bancos de dados é possível sempre lermos estes dados do HD ou SSD do computador e usarmos dados **persistentes**.

### Quais os Tipos de Dados
Existem dois tipos de dados:
- **Voláteis**: Estes dados são colocados em fontes que não permitem a persistência de dados, como por exemplo a **Memoria RAM**, mas apesar de não serem dados persistentes muitas vezes são dados que precisam ser acessados/alterados com extrema velocidade, é o caso de variáveis de um programa por exemplo, nestes casos desligar o computador ou matar o processo desses dados irá acarretar na perda destes dados
- **Persistentes**: São dados que serão guardados em fontes fora do seu programa, como por exemplo **arquivos**, **bancos de dados** (SQL ou noSQL) entre outras opções, nestes casos independente de o programa estar ou não rodando os dados continuarão e poderão ser consultados sempre, apesar da lentidão, pois agora estaremos procurando em um **HD** ou **SSD** pela informação que queremos, consultar, alterar ou deletar
## Tipos de Bancos de Dados
Em termos gerais, a principal diferença entre SQL e NoSQL reside no modelo de dados e na forma como interagem com os dados. Bancos de dados SQL são relacionais, utilizam uma estrutura rígida de tabelas com colunas e linhas, e empregam a linguagem SQL para consultas. Já NoSQL é um termo abrangente para bancos de dados não relacionais, que oferecem maior flexibilidade, podem trabalhar com dados não estruturados ou semiestruturados, e não utilizam a linguagem SQL padrão. 

SQL (Linguagem de Consulta Estruturada):
- **Modelo:** Relacional, com dados organizados em tabelas com colunas e linhas. 
- **Estrutura:** Rígida, com esquemas bem definidos antes da criação do banco de dados. 
- **Linguagem:** SQL, uma linguagem padrão para consultas e manipulação de dados. 
- **Consistência:** Prioriza a consistência dos dados, garantindo que as transações (operações de consulta, alteração, criação e deleção) sejam completas e confiáveis. 
- **Escalabilidade:** Pode ser desafiador escalar horizontalmente (adicionar mais servidores). 
- **Casos de uso:** Aplicativos que exigem alta consistência, transações complexas e modelagem de dados bem definida. 
- **Exemplos:** MySQL, PostgreSQL, Oracle, SQL Server. 

NoSQL:
- **Modelo:** Não relacional, com diferentes modelos de dados, como chave-valor, documento, coluna larga e grafo. 
- **Estrutura:** Flexível, permitindo armazenar dados com diferentes estruturas e sem a necessidade de um esquema rígido. 
- **Linguagem:** Varia conforme o tipo de banco de dados NoSQL, podendo incluir JSON, BSON, ou linguagens específicas do fornecedor. 
- **Consistência:** Prioriza a disponibilidade e a tolerância a falhas, podendo sacrificar a consistência total em alguns casos. 
- **Escalabilidade:** Mais fácil de escalar horizontalmente, adicionando mais nós ao sistema. 
- **Casos de uso:** Aplicativos que exigem alta escalabilidade, flexibilidade para lidar com dados não estruturados e desempenho em grandes volumes de dados. 
- **Exemplos:** MongoDB, Cassandra, Redis, DynamoDB.

## Preparando o Ambiente
Pra que possamos começar a usar um banco de dados precisamos antes obviamente precisamos de um banco de dados, existem varias formas de conseguir um:
- **Instalando na própria maquina** onde é bem simples, você procura um site de banco de dados, baixa o instalador e instala, isso fará com que você possa instalar na maior parte dos casos um banco de dados para cada maquina que você tiver, mas existem bancos de dados como SQL Server que permitem no mesmo computador
- Usando um **servidor fornecido em nuvem**, onde você pode contratar um provedor como Azure ou AWS para fornecer um banco de dados para você, é assim que as grandes empresas usam bancos de dados, mas isso traz a necessidade de pagar por serviços e o perigo de estar disponível na internet necessitando de uma atenção muito maior na parte de segurança
- E a que vamos utilizar, um **contêiner Docker**, desta forma podemos subir a quantidade que quisermos da maior parte dos bancos de dados existentes, isso nos permite também replicar configurações em ambientes de forma fácil, rápida e gratuita com outros colaboradores, tutorias ou avaliadores de nossos projetos
### Instalando Docker no DevContainer
Como neste tutorial estou usando DevContainer para Java do VS Code, ainda não tenho Docker instalado dentro dele portanto vou ter que instalar um Docker dentro de um contêiner Docker, para fazer isso preciso: 
1. Voltar para o root do meu projeto, ou seja, na pasta anterior a que estou trabalhando, escrevendo no terminal, `code .. -r` onde `code` é o programa VS Code `..` é a pasta pai da que estou, `-r` é para usar a mesma tela do VS Code para isso (se você esta usando DevContainer terá uma pasta chamada `.devcontainer` no projeto agora)
2. pressionar `Ctrl + Shift + P`
3. selecionar `Dev Containers: Configure Container Features` 
4. selecione a opção `Docker (Docker-in-Docker)` da devcontainers oficial
5. Selecione `Keep defaults`
6. Um popup irá aparecer no canto inferior direito pedindo para fazer o rebuild do contêiner, selecione `rebuild`
Isso irá criar uma nova imagem na sua maquina agora com o Docker instalado, como ocorreu uma nova build do nosso DevContainer teremos que refazer a instalação do `Maven`, para testar se ele esta instalado podemos usar o comando abaixo:
```bash
mvn -version

# isso resultará em
bash: mvn: command not found
```
Para instalar novamente só precisamos rodar o comando da primeira seção deste tutorial
```bash
# Para listar as versões do maven disponíveis (para para a listagem use a tecla q)
sdk list maven

# Para instalar a versão desejada (em 3.9.10 troque pela versão desejada)
sdk install maven 3.9.10
```
se rodarmos o primeiro comando agora veremos que irá nos retornar a versão do `Maven`, e para testarmos se docker foi instalado podemos usar o comando 
```bash
# retorna a versão do docker
docker -v
```
Agora para voltarmos ao nosso projeto java podemos usar `code bancoDeDados/ -r` onde `code` é o VS Code, o `bancoDeDados/` é a pasta que queremos abrir o VS Code e `-r` é que queremos abrir na mesma pagina

### Criando um Banco de Dados com Docker
Para usarmos o Docker para criar um banco de dados, é bem simples, precisamos apenas criar um arquivo chamado `docker-compose.yml` que irá definir a configuração do nosso banco de dados

_Nota: O arquivo `docker-compose.yml` não é o único arquivo para se criar uma imagem Docker, inclusive este é apenas para usarmos uma imagem já feita, existem outros passos para criar uma imagem do zero, mas como esse não é o foco deste tutorial não irei comentar mais sobre, porem é recomendado pesquisar mais sobre para entender como Docker funciona, suas facilidades e porque contêineres são tão usados na industria_

Vamos criar esse arquivo ao lado do `pom.xml` com o seguinte conteúdo
```yml
services:
  postgres:
    image: postgres:14.18-bookworm
    environment:
      POSTGRES_PASSWORD: "Postgres2022!"
    ports:
      - "5432:5432"
```
- `services`: para dizermos que estaremos declarando serviços
- `postgres`: para dar o nome de um serviço, este pode ser qualquer nome que quiser
- `image`: estamos declarando que a imagem que vamos usar é `postgres` na versão `14.18-bookworm`, podemos encontrar informações sobre a imagem no [docker hub da imagem](https://hub.docker.com/_/postgres)
- `environment`: para colocarmos variáveis de ambiente
- `POSTGRES_PASSWORD`: é uma das variáveis de ambiente da imagem do Postgres, esta será a senha para o usuário padrão do Postgres
- `ports`: quais portas `{porta na sua maquina}:{porta no container}` serão mapeadas, como cada contêiner tem seu próprio ambiente, é necessário mapear um `port` para que possamos acessar certas aplicações, neste caso vamos mapear a o `port` `5432` da aplicação Postgres que esta rodando no contêiner (numero a direita), para ser acessado na nossa maquina no `port` `5432` (número a esquerda), mas se este estivesse ocupado por outra aplicação poderíamos usar o outro

Caso ainda não tenha ficado claro `port`s são pontos de acesso liberados pelo seu computador para acessar aplicações, um exemplo é como estávamos acessando para testar nosso site usávamos o `localhost` que é um link que sua maquina tem para ela mesma (o mesmo seria 127.0.0.1) e temos `:` para falar que queremos especificar o porte para acessar e `8080` para acessarmos a porta `8080` que é onde esta aberto para rodar nossa aplicação

### Iniciando Serviços no Docker
Agora que temos um arquivo que diz quais serão as configurações de nosso contêiner Postgres podemos rodar o seguinte comando no terminal
```bash
docker compose up
```
Você verá que a imagem começará a ser baixada e depois que terminar o download, aparecerão varias mensagens, com a ultima sendo algo com `database system is ready to accept connections`, isso significa que agora você tem um contêiner de banco de dados Postgres rodando, parabéns, mas como não queremos sempre ter que deixar o terminal vivo para termos o banco de dados rodando, vamos fazer o seguinte no terminal pressione `Ctrl + C`, isso irá parar o contêiner do banco de dados, para subi-lo novamente use o seguinte comando
```bash
docker compose up -d
```
Onde `-d` significa detatchd, você verá que aparecerá apenas uma mensagem dizendo que o serviço foi iniciado, quando quiser parar o contêiner digite no terminal
```
docker compose stop
```
Isso fará com que o contêiner seja parado, mas ainda estará preparado para iniciar novamente de onde parou, caso queira remover o contêiner digite
```bash
docker compose down
```
Este comando fará com que o contêiner seja removido quase por completo (ainda terá o `volume` que é espaço no HD onde os dados ficarão em algum lugar caso tenha especificado)
### Consultas no Banco de Dados
Em aplicações que consultam o banco de dados é sempre bom deixar o desenvolvedor fazer as consultas que quiser e testar a aplicação diretamente no banco de dados, inclusive existem equipes inteiras dedicadas apenas para administração de bancos de dados quando um programa já esta em produção, por isso existem ferramentas apenas para fazer operações em bancos de dados que não são atreladas especificamente com desenvolvimento de software, algumas opções são:
- **Os que são gerais** como é o caso de `DBeaver` que permite utilizar uma grande variedade de bancos de dados
- **Os que são para bancos de dados específicos** que são utilizados para apenas um ou um grupo especifico de bancos de dados como é o caso do `SQL Server Menagemant Studio` que serve apenas para SQL Server
- **Os que integram com outras ferramentas** como é o caso da extensão para VS Code `SQLTools`, que é uma extensão que permite uma grande variedade de bancos de dados
Mas a ferramenta que vamos usar é uma que vamos fazer via Docker assim não precisaremos instalar nada localmente que é o `Adminer`, ele é uma ferramenta para gerenciamento de alguns tipos de bancos de dados incluindo Postgres. Vamos usar o `docker-compose.yml` para subir ele como um serviço da nossa aplicação, ao final o arquivo deve ficar assim
```yml
services:
  postgres:
    image: postgres:14.18-bookworm
    environment:
      POSTGRES_PASSWORD: "Postgres2022!"
    ports:
      - "5432:5432"

  adminer:
    image: adminer:5-standalone
    ports:
      - "8080:8080"
```
Isto eu simplesmente copiei da documentação da imagem na [página do Docker Hub](https://hub.docker.com/_/adminer), portanto você já deve ter notado que este esta usando a porta `8080` da nossa máquina, mas esta porta já é utilizada pelo nosso servidor que usamos para testar a aplicação, dito isso precisamos mudar o `port` que é usado para nossa maquina pelo servidor de aplicação ou mudamos onde é mapeado o `port` do `Adminer`. Neste caso vamos usar a segunda opção por ser mais fácil, mas em um cenário real você teria que avaliar quais seriam as consequências de fazer isso e se suas aplicações permitem você fazer isso, vou colocar o `port` da seguinte forma para o `Adminer` `8080` para a aplicação que esta rodando para a aplicação no contêiner e `8081` para ser mapeado em nosso computador deixando o resultado final da seguinte forma
```yml
services:
  postgres:
    image: postgres:14.18-bookworm
    environment:
      POSTGRES_PASSWORD: "Postgres2022!"
    ports:
      - "5432:5432"

  adminer:
    image: adminer:5-standalone
    ports:
      - "8081:8080"
```
Vamos remover os serviços usando `docker compose down` e logo em seguida subi-los novamente com `docker compose up -d` 

Se acessarmos no navegador `http://localhost:8081/`, vamos ser apresentados a uma tela de login onde colocaremos o sistema de banco de dados que queremos usar (selecione `PostgreSQL`), o servidor é em qual servidor esta o banco de dados, neste caso é nossa própria maquina (pode usar `localhost` ou `127.0.0.1` se estiver instalado na sua maquina como estamos no Docker vamos usar o nome do serviço que no caso é `postgres`), usuário será `postgres` a senha será a escolhida na variável de ambiente (`Postgres2022!`), o nome do banco de dados que queremos entrar (`postgres`), queremos fazer login permanente e por fim `entrar` 

Ao fazer login seremos apresentados com uma tela onde é criar `table`s e `view`s, rotinas de `procedure`s e `function`s, `sequencie`s e `type`s, nestas paginas podemos criar com uma interface simples as estruturas que queremos usar, mas por enquanto não vamos usar nenhum destes, pois queremos aprender `SQL`, vamos então para a aba a esquerda em `SQL Command` e teremos uma caixa de texto simples onde vamos escrever o seguinte comando SQL
```sql
SELECT 1 AS teste;
```
Na parte inferior da caixa de texto vamos clicar em `Execute`, o comando irá rodar em nosso banco de dados e no canto superior teremos uma tabela com o dado sendo mostrado, parabéns, este foi seu primeiro comando SQL

## Trabalhando com Bancos de Dados em Java
Agora que já temos um banco de dados e uma forma de fazer consultas sem depender da nossa aplicação, já podemos começar a codar, a primeira coisa que precisamos fazer é baixar uma biblioteca que nos permite usar um banco de dados Postgres no java, a que vamos usar é a `postgresql`, então vamos adicionar em nosso `pom.xml` o seguinte código na tag `<dependencies>` 
```xml
<!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.7.7</version>
</dependency>
```
Agora que temos a dependência necessária para criar a conexão podemos criar um forma de criar consultas e fazer operações no banco de dados, vamos criar um novo pacote dentro de nossa `Model` chamado `Database`, então vá na aba `Java Project`, clique com botão direito em `com.Models`, selecione `new`, selecione `Package...` e coloque o nome como `com.models.database`, com este pacote criado vamos clicar com botão direito nele agora, `new`, selecione `class` e de o nome da classe como `DatabaseConnection`.

Agora que temos esta nova classe eu vou adicionar alguns métodos mais complexos, para que possamos fazer reutilização de código mais tarde que vou explicar oque são nas linhas acima de cada comando e você poderá ler com mais calma
```java
package com.models.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class DatabaseConnection {
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=public&user=postgres&password=Postgres2022!";

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static String getUrl() {
        return URL;
    }
    

    /**
     * Estabelece uma conexão com o banco de dados usando as credenciais fornecidas.
     * Essa conexão é usada para executar consultas e comandos SQL.
     * A conexão deve ser fechada após o uso para evitar vazamentos de recursos.
     * @return Optional<Connection> - retorna uma conexão com o banco de dados ou vazio se não for possível estabelecer a conexão.
     */
    private static Optional<Connection> getConnection() {
        try {
	        // garante que o pacote do driver será carregado
            Class.forName("org.postgresql.Driver");
            return Optional.of(DriverManager.getConnection(URL));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Obtém os nomes dos campos do tipo especificado.
     * @param returnType o tipo de objeto que será retornado
     * @return um array de strings contendo os nomes dos campos do tipo especificado
     */
    private static String[] getReturnTypeFields(Class<?> returnType) {
        // Obtém os nomes dos campos do tipo especificado
        // Isso é usado para mapear os resultados da consulta para os campos do objeto
        Field[] fields = returnType.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * Busca quais campos existem no ResultSet e no tipo especificado.
     * Isso é usado para garantir que apenas os campos que existem no ResultSet e no tipo especificado sejam mapeados.
     * Isso é importante para evitar erros de reflexão ao tentar acessar campos que não existem no ResultSet ou no tipo especificado.
     * @param returnType o tipo de objeto que será retornado
     * @param resultSetMetadata os metadados do ResultSet
     * @return um array de strings contendo os nomes dos campos que existem no ResultSet e no tipo especificado
     * @throws SQLException
     */
    private static String[] getResultSetAllowedFields(Class<?> returnType, ResultSetMetaData resultSetMetadata) throws SQLException {
        // busca quais campos existem do ResultSet
        String[] returnTypeFields = getReturnTypeFields(returnType);
        var columnCount = resultSetMetadata.getColumnCount();
        List<String> columnNamesDict = new java.util.ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            if(Arrays.stream(returnTypeFields).anyMatch(resultSetMetadata.getColumnName(i + 1)::equals))
            {
                columnNamesDict.add(resultSetMetadata.getColumnName(i + 1));
            }
        }

        return columnNamesDict.toArray(new String[0]);
    }


    /**
     * Executa uma consulta SQL e retorna uma lista de objetos do tipo especificado.
     * O método utiliza reflexão para mapear os resultados da consulta para instâncias do tipo especificado.
     * @param query a consulta SQL a ser executada
     * @param returnType o tipo de objeto que será retornado
     * @param params os parâmetros a serem passados para a consulta (deve corresponder à ordem dos placeholders na consulta)
     * @return uma lista de objetos do tipo especificado
     */
    public static <T> List<T> runQueryForList(String query, Class<T> returnType, Object... params) {
        // T representa o tipo genérico do objeto que será retornado

        // inicializa a conexão como null
        Connection conn = null;

        // lista para armazenar os resultados
        List<T> results = new java.util.ArrayList<>();

        try {
            // obtém a conexão do banco de dados
            // se a conexão for bem-sucedida, atribui à variável conn
            // se não for possível estabelecer a conexão, lança uma exceção
            Optional<Connection> optionalConnection = getConnection();
            if (optionalConnection.isPresent()) {
                conn = optionalConnection.get();
            } else {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            // prepara a consulta SQL com os parâmetros fornecidos
            // utiliza PreparedStatement para evitar SQL Injection
            // e para facilitar a substituição dos placeholders (?) pelos valores reais
            // os parâmetros devem corresponder à ordem dos placeholders na consulta
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            // executa a consulta e obtém o resultado em um ResultSet
            var resultSet = stmt.executeQuery();

            // busca quais campos existem em comum entre o ResultSet e o tipo especificado
            String[] columnNames = getResultSetAllowedFields(returnType, resultSet.getMetaData());

            // cria um hashmap para mapear os nomes das colunas do ResultSet para seus índices
            // isso é útil para acessar os valores do ResultSet de forma eficiente
            // o hashmap é usado para evitar a necessidade de iterar sobre os nomes das colunas
            // a cada vez que um valor é acessado, melhorando o desempenho
            Map<String, Integer> columnNamesMap = new HashMap<>();
            for (String column : columnNames) {
                columnNamesMap.put(column, resultSet.findColumn(column));
            }

            // itera pelos valores do ResultSet
            while (resultSet.next()) {
                // para cada linha do ResultSet, cria uma nova instância do tipo especificado
                var instance = returnType.getDeclaredConstructor().newInstance();

                // itera pelos campos em comum entre o ResultSet e o tipo especificado
                for (String fieldName : columnNamesMap.keySet()) {
                    // usa reflexão para acessar o campo correspondente no objeto
                    var field = returnType.getDeclaredField(fieldName);
                    // define o campo como acessível, mesmo que seja privado
                    // isso é necessário para poder atribuir valores a campos privados
                    field.setAccessible(true);
                    // atribui o valor do ResultSet ao campo do objeto
                    // usa o método getObject do ResultSet para obter o valor da coluna correspondente
                    // o método getObject permite obter o valor de qualquer tipo de coluna
                    // e converte automaticamente para o tipo do campo do objeto do campo
                    field.set(instance, resultSet.getObject(columnNamesMap.get(fieldName)));
                }

                results.add(instance);  
            }

            return results;
            
        } catch (Exception e) {
            // captura exceções específicas de SQL
            // isso é útil para identificar problemas com a consulta ou com a conexão
            e.printStackTrace();
        } finally {
            // garante que a conexão seja fechada após o uso
            // isso é importante para evitar vazamentos de recursos
            // se a conexão for nula, não faz nada
            // se a conexão não for nula, tenta fechá-la
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // retorna a lista de resultados
        return results;
    }


    /**
     * @param query a consulta SQL a ser executada
     * @param typeConstructor o tipo de objeto que será retornado
     * @param params os parâmetros a serem passados para a consulta (deve corresponder à ordem dos placeholders na consulta)
     * @return um objeto do tipo especificado ou null se não houver resultados
     */
    public static <T> T runQuery(String query, Class<T> typeConstructor, Object... params) {
        // T representa o tipo genérico do objeto que será retornado

        // chama o método runQueryForList para obter uma lista de resultados
        // e retorna o primeiro elemento da lista ou null se a lista estiver vazia
        // isso é útil quando se espera apenas um único resultado da consulta
        // por exemplo, ao buscar um único registro pelo ID
        return runQueryForList(query, typeConstructor, params).stream().findFirst().orElse(null);
    }

    /**
     * @param command a instrução SQL a ser executada (INSERT, UPDATE, DELETE)
     * @param params os parâmetros a serem passados para a instrução (deve corresponder à ordem dos placeholders na instrução)
     * @return o número de linhas afetadas pela instrução
     */
    public static long runCommand(String command, Object... params) {
        // inicializa a conexão como null
        Connection conn = null;
        // variável para armazenar o número de linhas afetadas pela instrução
        // é inicializada com 0, pois se não houver linhas afetadas, o valor será 0
        // isso é útil para verificar se a instrução foi executada com sucesso
        long results = 0;
        try {
            
            // obtém a conexão do banco de dados
            // se a conexão for bem-sucedida, atribui à variável conn
            // se não for possível estabelecer a conexão, lança uma exceção
            Optional<Connection> optionalConnection = getConnection();
            if (optionalConnection.isPresent()) {
                conn = optionalConnection.get();
            } else {
                throw new RuntimeException("Failed to establish a database connection.");
            }


            // prepara a consulta SQL com os parâmetros fornecidos
            // utiliza PreparedStatement para evitar SQL Injection
            // e para facilitar a substituição dos placeholders (?) pelos valores reais
            // os parâmetros devem corresponder à ordem dos placeholders na consulta
            PreparedStatement stmt = conn.prepareStatement(command);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            // executa a instrução SQL e obtém o número de linhas afetadas
            // o método executeUpdate é usado para instruções que modificam os dados
            // como INSERT, UPDATE ou DELETE
            // retorna o número de linhas afetadas pela instrução
            results = stmt.executeUpdate();
            return results;

        } catch (Exception e) {
            // captura qualquer exceção que ocorra durante a execução da consulta
            // e imprime o stack trace para depuração
            e.printStackTrace();
        } finally {
            // garante que a conexão seja fechada após o uso
            // isso é importante para evitar vazamentos de recursos
            // se a conexão for nula, não faz nada
            // se a conexão não for nula, tenta fechá-la
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // retorna a lista de resultados
        return results;
    }
}
```
Depois desta enxurrada de informação em um único bloco de código, posso explicar para o que serve cada coisa:
- `getConnection`: É um método privado da classe que serve apenas para criar uma conexão com o banco de dados, depois disso o fechamento da conexão, para garantirmos que nenhum recurso do computador é perdido ou alocado de forma impropria, fica de por conta dos métodos que utilizam a conexão
- `runQueryForList`: É um método que irá nos permitir enviar um comando para o banco de dados via o parâmetro `query`, definir qual tipo de objeto será retornado no `returnType`, para que assim possamos mapear e ter autocomplete dos objetos retornados pelo banco de dados, por fim temos os `params` que são qualquer tipo de objeto no java, estes irão substituir o `?` na string da `query` como parâmetros na ordem que forem colocados no método
- `runQuery`: Tem os mesmos parâmetros que a `runQueryForList`, porém seu objetivo é apenas pegar o primeiro valor de um resultado do banco de dados
- `runCommand`: É um método que permite executarmos tarefas como `INSERT`, `UPDATE` e `DELETE` no banco de dados, como o objetivo deste não é buscar dados não possui tipo de retorno ou algo do tipo, apenas retorna a quantidade de valores afetados na operação, seus parâmetros são `command` que é a string do comando a ser executado no banco de dados e `params` que são os parâmetros que irão substituir o `?` na string do comando 

_Nota: o método `runQueryForList` utiliza de reflexão (reflection) para fazer o mapeamento dos campos, isso é uma técnica onde não sabemos exatamente o formato do objeto, mas fazemos seu mapeamento por meio dos nomes de seus campos_

### Nosso Primeiro `SELECT`
Agora que já temos uma explicação linha a linha do código e uma explicação mais abrangente, podemos partir para nosso primeiro teste para ver se todas as peças se encaixam, vamos colocar uma `query`, sem parâmetros, para a função de listagem de pessoas ao final o método ficará da seguinte forma
```java
// método para listar as pessoas
public static List<PessoaDTO> getPessoas() {
    try {
        Thread.sleep(DELAY);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

	// query a ser executada pelo banco de dados
	// seleciona da tabela pessoa 
	// as propriedades id, nome e idade 
	// ordenando pelo id em ordem descendente
    String sql = """
        SELECT 
            p.id,
            p.nome,
            p.idade
        FROM 
            pessoa p
        ORDER BY
            p.id DESC
        """;

    // passamos o codigo SQL e a classe que queremos ter de retorno
    // retorna pessoas
    return DatabaseConnection.runQueryForList(sql, PessoaDTO.class);
}
```
Como já deve ter pensado, isso acarretará em um erro, pois ainda não temos a tabela pessoa em nosso banco de dados, portanto vamos criar uma tabela com nosso `Adminer`. Na aba `SQL Command` vamos colocar o seguinte código para criação da tabela 
```sql
CREATE TABLE "pessoa" 
(
  "id" bigserial NOT NULL,
  PRIMARY KEY ("id"),
  "idade" integer NOT NULL,
  "nome" varchar(400) NOT NULL
);
```
- `CREATE TABLE "pessoa"`: significa que queremos criar uma tabela chamada `pessoa`, as propriedades desta tabela serão descritas entre `()` (parentese) e separadas por `,` (virgula)
- `"id" bigserial NOT NULL`: significa que queremos criar uma propriedade chamada `id` que será do tipo `bigserial` que é um equivalente ao `long` do java que automaticamente incrementa seu valor quando um novo registro é inserido e por fim `NOT NULL` que diz que esta propriedade nunca será nula
- `PRIMARY KEY ("id")`: significa que a propriedade `id` será a chave primaria da tabela, ou seja, poderemos usar este valor como relação em outras tabelas e poderemos ter certeza que sempre será único nunca podendo se repetir
- `"idade" integer NOT NULL`: significa que a tabela terá uma propriedade `idade` que será do tipo `integer` equivalente ao `int` no java e `NOT NULL` que diz que nunca será nulo
- `"nome" varchar(400) NOT NULL`: significa que a tabela terá uma propriedade `nome` que será do tipo `varchar` que é o equivalente a uma string no java `(400)` define o tamanho máximo desse texto e `NOT NULL` diz que esse valor nunca será nulo

_Nota: Uma boa pratica em projetos que rodamos scripts de forma manual é criar uma pasta `Scripts/` com tudo que não é rodado pelo código na versão do software que estamos desenvolvendo, assim temos um histórico do código que é rodado no banco de dados e também outras pessoas podem replicar nosso banco de dados em seus ambientes. Dito isso, vou criar um arquivo em `Scripts/v1/00 - Scripts.sql` e colocar o código da criação da tabela (código que usamos apenas para teste não são necessários)_

Agora que nosso código esta trazendo informações do banco de dados, podemos testar e veremos que não traz nada na tela, isso ocorre porque não temos valores inseridos na tabela `pessoa`, apenas para teste vamos inserir uma pessoa e ver oque acontece na tela, para inserir uma pessoa é simples, vamos rodar o seguinte código no `Adminer` 
```sql
INSERT INTO pessoa 
(idade, nome)
VALUES 
(18,'Breno');
```
- `INSERT INTO pessoa`: diz que queremos inserir na tabela pessoa
- `(idade, nome)`: são os campos que queremos inserir, o campo `id` não é usado pois ele é automaticamente incrementado por ser do tipo `bigserial`
- `VALUES`: diz que os valores abaixo serão as linha a serem inseridas
- `(18,'Breno')`: são os dados a serem inseridos representando as colunas declaradas acima do `VALUES` e podemos inserir quantos quisermos se separarmos por `,` (virgula)
Após rodar esse comando vamos notar e clicarmos no botão `Refresh` do nosso site vamos ter o nosso registro

### Nosso Primeiro `INSERT`
Agora que já temos tudo em mãos já fica mais fácil de executarmos outros comandos, para que possamos criar novos dados devemos vamos criar o mesmo `INSERT` que usamos para fazer nosso teste com o `SELECT`, porem convertendo para receber comandos dinâmicos
```java
// retorna true se a adição afetou algo
// delay para simular uma operação de longa duração
public static boolean addPessoa(PessoaDTO pessoa) {
    try {
        Thread.sleep(DELAY);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    String sql = """
        INSERT INTO pessoa 
        (nome, idade)
        VALUES 
        (?, ?)
        """;

    return DatabaseConnection.runCommand(sql, pessoa.getNome(), pessoa.getIdade()) > 0;
}
```
Onde a explicação do comando é
- `INSERT INTO pessoa`: diz que queremos inserir na tabela pessoa
- `(idade, nome)`: são os campos que queremos inserir, o campo `id` não é usado pois ele é automaticamente incrementado por ser do tipo `bigserial`
- `VALUES`: diz que os valores abaixo serão as linha a serem inseridas
- `(?,?)`: são os dados a serem inseridos representando as colunas declaradas acima do `VALUES`, onde o primeiro `?` é o atributo `nome` e o segundo é o `idade` da pessoa 
Após rodar esse comando vamos notar e clicarmos no botão `Refresh` do nosso site vamos ter o nosso registro
Se testarmos em nosso site a inserção será bem sucedida e aparecerá um novo registro, com isso poderemos remover a constante `idPessoaAtual`, pois não será mais utilizada
### Nosso Primeiro `DELETE`
Já que temos a opção de criar dados, agora podemos deletar eles sem medo, vamos adaptar o nosso método de deleção para o seguinte
```java
// remove uma pessoa da lista pelo ID
// retorna true se a remoção afetou algo
// delay para simular uma operação de longa duração
public static boolean removePessoa(long id) {
    try {
        Thread.sleep(DELAY);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    String sql = """
        DELETE FROM pessoa 
        WHERE id = ?
        """;
    return DatabaseConnection.runCommand(sql, id) > 0;
}
```
Onde a explicação do comando é 
- `DELETE FROM pessoa`: delete valores da tabela `pessoa`
- `WHERE id = ?`: onde a coluna `id` tem o valor da variável passada 
Se testarmos na tela veremos que os valores são removidos da listagem após isso, neste caso estamos usando oque chamamos de `hard delete`, pois deletamos as linhas dos registros no banco de dados, mas outra opção caso não seja possível deletar um registro, seja por problemas de negocio, que pode ser que um dado seja importante no futuro ou pelas relações que existem no banco de dados entre tabelas, é possível fazer oque é chamado de `soft delete` que é quando uma tabela tem uma propriedade de `ativo` (isso pode variar para status entre vários outros), onde quanto é 1/verdadeiro esta ativo no sistema ou é 0/falso que é filtrado na hora da consulta, ai não usamos `DELETE` mas sim o `UPDATE`
### Nosso Primeiro `UPDATE`
Chegamos então na ultima operação do site que é a de atualizar registros, para isso precisamos passar todas as propriedades de uma pessoa
```java
// atualiza uma pessoa na lista pelo ID
// retorna true se a atualização afetou algo
// delay para simular uma operação de longa duração
public static boolean updatePessoa(long id, PessoaDTO pessoaAtualizada) {
    try {
        Thread.sleep(DELAY);
    } catch (InterruptedException e) {
        e.printStackTrace();
        return false;
    }

    String sql = """
        UPDATE pessoa 
        SET 
	         nome = ?
	        ,idade = ?
        WHERE 
	        id = ?
        """;
    return DatabaseConnection.runCommand(sql, pessoaAtualizada.getNome(), pessoaAtualizada.getIdade(), id) > 0;
}
```
Onde a explicação do comando é 
- `UPDATE pessoa`: diz que vai atualizar registros da tabela `pessoa`
- `SET`: fala as propriedades que serão atualizadas
- `nome = ?`: diz que a propriedade `nome` será atualizado com o valor da variável
- `,idade = ?`: diz que a propriedade `idade` será atualizado com o valor da variável
- `WHERE`: fala que haverá um filtro para os valores que serão afetados
- `id = ?`: o filtro será onde o `id` for igual a variável passada 
Se testarmos em nosso site vamos perceber que os valores serão atualizados igual antes, mas agora são atualizados de forma persistente por conta do banco de dados

# Conclusão 
Com nosso banco de dados funcionando com nossa aplicação, podemos dizer que esta concluída uma aplicação `CRUD` com integração com banco de dados, validações, usando tanto páginas `SSR` com `postback` e páginas `SGR` que usam uma `API` servlet.

Este é apenas um projeto de treinamento/estudo e apesar de muitos dos pontos abordados terem sido feitos tentando seguir o máximo de boas praticas ainda há muito a ser melhorado com a interface de nosso site, algumas validações adicionais (como é o caso do `nome`, que caso passe de 400 caracteres, acarretará em uma `Exception` por conta da coluna no banco de dados), a `connectionString` esta fixa no projeto assim como as variáveis de ambiente do `docker-compose.yml`, que deveriam estar em variáveis de ambiente, entre muitas outras coisas que podem ser melhoradas no projeto, mas espero que desta forma você possa aprender com mais facilidade alguns conceitos sobre desenvolvimento web com java e apesar de termos utilizados ferramentas um pouco mais rusticas como é o caso da biblioteca `postgresql` e não de algo mais sofisticado como um `ORM` ou uma ferramenta de administração de bancos de dados mais moderna como foi o caso do `Adminer` ou tecnologias das camadas de `View` mais modernas, espero que você tenha entendido mais a fundo como as coisas funcionam na base.

_**Muito obrigado por acompanhar este tutorial e bons estudos**_
