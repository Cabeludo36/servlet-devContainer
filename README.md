# Como criar um servlet com **visual studio code devContainers**

_Para este tutorial é necessário estar ou em um Github Codespaces ou ter Docker instalado na maquina base_

_Vou vou deixar entre `()` o que eu for colocando caso esteja acompanhando e fazendo_
## 1. Preparando o Ambiente

### Criando um DevContainer
Primeiramente é necessário criar um `devContainer`, pressionando `ctrl + shift + P` e selecionando a opção `Dev Containers: Add Dev Container Configuration Files...` isso fará algumas perguntas:
1. Qual tipo de contêiner você vai usar? -> Java
2. Qual versão vai usar? -> Mais recente (21-bullseye)
3. Caso pergunte se você que instalar `Maven` ou `Gradle`, não selecione nenhuma, vamos instalar ela no contêiner manualmente, pois SDKMAN pode bugar com `devContainer` as vezes
### Instalando Dependências Pendentes
Como comentado no passo anterior optamos por não instalar Maven de cara, por conta de alguns bugs que podem ocorrer entre o `SDKMAN` e devContainer

```bash
# Para listar as versões do maven disponiveis
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
6. dê um `Group ID` do projeto (com.example)
7. dê um Nome ao projeto
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
- `webapp/`: para o código que vai definir nossa aplicação web, onde podemos definir paginas `HTML` (mas que vão ter extensão `.jsp`) e onde poderemos definir nossos endpoints `servlets`
- `index.jsp`: arquivo com a pagina inicial do seu projeto, que no momento tem apenas um `hello world`
- `WEB-INF/`: pasta onde vamos definir o arquivo de nossos servlets
- `web.xml`: arquivo com a informação que o servidor olha pra saber **onde buscar** servlets
- `pom.xml`: arquivo que Maven usa para definir quais pacotes/bibliotecas devem ser instalados para a aplicação funcionar

## Configurando o projeto

### 1. Entrando na pasta do projeto
Para que o VS Code entenda que estamos em um projeto Java e evitarmos ter mais informações em nosso explorador de arquivos no terminal use o comando `code meuprojeto/ -r` onde 
- `code`: é para chamarmos o VS Code
- `meuprojeto/`: que é usada para falarmos que queremos abrir o VS Code na pasta `meuprojeto/` 
- `-r`: para definir que queremos apenas fazer reload da tela que estamos invés de abrir uma nova
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

Para priar uma classe Java no VS Code temos varias opções
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
	// este metodo é chamado quando o servlet recebe uma requisição HTTP GET
	// onde HttpServletRequest req contém informações sobre a requisição
	// e HttpServletResponse res é usado para enviar uma resposta ao cliente
	@Override // sobrescreve metodo da classe base HttpServlet
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
Este servlet que criamos irá enviar para o cliente uma pagina HTML, porem nosso serviço web ainda não sabe que ele existe, precisamos declarar sua existencia no arquivo `web.xml`
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
Com o arquivo configurado podemos então criar uma referencia ao nosso servlet
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
- `servlet-mapping`: é usada para as informações de mapeamento web desse sevlet
- `url-pattern`: é a URI que irá chamar a função `onGet`, `onPost`, `onPut`, `onDelete` dependendo do método HTTP
### Testando o servlet
#### Gerando um build da nossa aplicação
Para que um servidor possa rodar nossa aplicação é necessário fazer primeiramente o build (compilar) dela para que seja possível executar o serviço

Para gerar um build temos duas partes, que devem ser feitas a cada nova versão do código: 
##### Gerar a compilação dos arquivos
Temos que compilar nossa aplicação java para que o `Java Runtime` entenda como interpretar nossos arquivos, mas antes se você seguiu a risca o que estou fazendo neste tutorial tem uma coisa que ainda não fizemos que é definir qual é o alvo de compilação do Maven ou seja qual versão do java esta instalada, temos que fazer isso pois o VS Code não traz a versão correta que esta instalada, mas sim a dê um template predefinido, no arquivo  `pom.xml` no bloco `<properties>` deixe da seguinte forma:
```xml
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<maven.compiler.source>1.21</maven.compiler.source>
	<maven.compiler.target>1.21</maven.compiler.target>
</properties>
```
_no lugar dos `1.21` troque pela sua versão_

Com isso resolvido vá na aba `Java Projects`, clique com o botão direito no projeto (meuprojeto) e selecione `Rebuild All`, isso irá gerar uma pasta chamada `target/`, onde nossa classe `ServletHello` compilada (agora com a extensão `.class`) estará 
##### Gerar o arquivo `.war`
Para que possamos executar nossa aplicação, agora **como um projeto**, temos que gerar om arquivo `.war`, para fazer isso basta irmos na aba `Maven`, expandir a aba do seu projeto (meuprojeto), expandir `Plugins`, expandir `war`, em `exploded` clicar com o botão direito e selecionar `Run` 

E assim será gerado um novo diretório na pasta `target` com o nome do seu projeto, neste caso o caminho completo ficou `target/meuprojeto/` 

_Nota: nenhum arquivo da pasta `target/` pode ser alterado, caso o contrario isso pode resultar no projeto não funcionando corretamente_ 
#### Subindo um servidor
Para que possamos testar o servlet é necessário termos um servidor, maior parte das fontes java usam o `Tomcat`, ele pode ser instalado na maquina local, mas como estamos em um devContainer porque não queremos instalar coisas localmente podemos usar a extensão `Community Server Connectors` que nos permitira baixar no contêiner e executar nossa aplicação

Na aba `Servers` em seu explorador de arquivos, clique no icone de servidor com um `+` selecione a opção `yes` para baixar um servidor, selecione `Apache Tomcat` > selecione Continuar, aceite os termos da licença, e ele começará a fazer download do `Tomcat`

Quando o download tiver concluído aparecerá um servidor `Tomcat` com isso podemos simplesmente clicar com o botão direito na pasta de nosso projeto na pasta `target/` (`target/meuprojeto`), e rodar `debug on server` ou `run on server`, neste caso vou debugar > selecionar o servidor que quero rodar a aplicação, selecionar que não desejo opções adicionais e dar um nome de projeto para depuração (`meuprojeto`)

Você irá perceber que no canto inferior direito aparecerá uma notificação de que um port foi aberto do contêiner para a maquina local, se clicar em `Abrir no Browser`, você será redirecionado para a url `http://localhost:8080/` com algumas informações sobre o `Tomcat`, esse é o root do servidor que o `Tomcat` abriu para nós

Se navegarmos para `http://localhost:8080/{nome da pasta em target/}/` (`meuprojeto`) veremos que a mensagem padrão é `Hello World` como especificado no arquivo `src/main/webapp/index.jsp`
```html
<html>
<body>
	<h2>Hello World!</h2>
</body>
</html>
```
É possível editar esse arquivo para deixar mais apresentável, mas ainda não vimos nosso servlet em ação para que possamos fazer isso precisamos ir para o endpoint que definimos no `src/main/webapp/WEB-INF/web.xml` neste caso ficou `http://localhost:8080/meuprojeto/api/hello`, vamos ver a mensagem que colocamos lá para fazer o display

Mas lembrando, o ideal é deixar telas dinâmicas, e na maior parte das vezes usamos ferramentas backend para formar essas paginas ou enviar informações para elas, isso incluindo também outras operações como 
- Create: Criar Dados (POST)
- Read: Ler Dados (GET)
- Update: Atualizar Dados (PUT)
- Delete: Deletar Dados (DELETE)
