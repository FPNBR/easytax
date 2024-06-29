# Projeto EasyTax

Essa API tem como objetivo incentivar doações por meio de incentivos fiscais, permitindo que as doações sejam deduzidas do Imposto de Renda, aliviando a carga tributária dos doadores e aumentando a participação.

## Hackathon realizado pela Uniesp e Marjosports, em 2024, que consistia resolver um dos dois desafios propostos.

### Desafio 2

Sistema:

A aplicação deverá conter:

	1.	Login:
	•	a. Deverá ser cadastrado um usuário na plataforma
	•	b. Usuário deverá realizar login para utilizar a plataforma
	•	c. Campo obrigatório do cadastro CPF, uma vez que o mesmo é o responsável pela doação (seja ela feita por ele ou por terceiro)
	•	d. O tipo de usuário CLIENTE deverá ter acesso APENAS aos dados do CPF do mesmo
	•	e. Um master do Role, então deverá consultar qualquer CPF
	2.	Deverá ser realizado a integração com a API governamental fictícia da receita através da URL
	3.	Mostrar um extrato contendo todas as transações do cliente na plataforma governamental fictícia
	4.	É interessante buscar alguém do Desafio 1 para verificar a inclusão dos dados das suas consultas

Extras:

Não será de obrigatoriedade, porém, é contado como pontos extras:

	1.	Testes unitários
	2.	Testes automatizados
	3.	Estrutura de banco de dados
	4.	Layout da aplicação
	5.	Documentação do seu sistema
	6.	Atualização da tela em tempo real

## Tecnologias Utilizadas

- Java
- Spring Boot
- Maven
- JUnit
- Mockito

## Estrutura do Projeto

O projeto segue a estrutura padrão de um projeto Maven:

- `src/main/java`: Contém o código fonte do projeto.
- `src/test/java`: Contém os testes unitários do projeto.

## Testes

Os testes unitários são escritos usando JUnit e Mockito. Um exemplo de teste pode ser encontrado em `src/test/java/br/com/easytax/easytax/service/ClientServiceTest.java`.

## Como Executar o Projeto

Para executar o projeto, você precisa ter o Java e o Maven instalados em seu sistema. Em seguida, você pode executar o projeto usando o comando Maven:

```bash
mvn spring-boot:run
```

## Como Executar os Testes
Para executar os testes, você pode usar o comando Maven:

```bash
mvn test
```

## Documentação da aplicação com Swagger
A documentação da API pode ser acessada em `http://localhost:8080/swagger-ui.html`.
![Diagrama ER](./swagger.png)

## Resposta da API

Todas as respostas da API seguem o formato `ApiResponse`. Aqui está um exemplo de uma resposta bem-sucedida:

```json
{
  "success": true,
  "message": "Operação realizada com sucesso",
  "data": {
    // Dados da resposta
  }
}
```

Em caso de erro é similar:
```json
{
  "success": false,
  "message": "Ocorreu um erro x",
  "data": {
    // Dados da resposta
  }
}
```

## Autenticação
Este projeto usa autenticação oauth2. Para autenticar, você precisa enviar um cabeçalho Authorization com o valor Bearer {token} em todas as requisições, substituindo {token} pelo token que você recebeu durante o login.

Exemplo de autenticação bem sucedida:
```json
{
  "success": true,
  "message": "Login realizado com sucesso",
  "data": {
    "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJlYXN5dGF4Iiwic3ViIjoiNjgyNTMzNjIwNzIiLCJleHAiOjE3MTk2ODkxNTMsImlhdCI6MTcxOTY4ODg1Mywicm9sZXMiOlsiQURNSU4iXX0.sMmkOw99txSyg-ax4oZACnid5AMbJhwGGWn8eRelI5oyxzbwTHyH1m4b3PWs-f5PQoLgErf6Bs-08Ob5rKfZjAkvihZp7atSWHsbn9CGHVW5YECF1zRmPpZWQXcD02PnIeK2iC0fb05zYWs3Jg0Mt4BQmRhY0WiUO4WhnT9yrf8Sx67KluPaTCJOMHAy0Q3ECSlisU6_iORPlcCwPBaAY65iPU-X9s3oQVGF2YkTw6GVwXHJbd9nvZWf7N59pHI20BMJKgOYaO29H8smXhYS6rWZaTqv0s-zRGUDIw82jWXX7JcEo7HrC_qzv1ntTbOsMSWsZDzHu9bbqqexKKyaog",
    "refresh_token": "v6XaHheNUQIx3ENAwqUlxKA0AvNWgjmf",
    "expires_in": 300
  }
}
```

## Diagrama Entidade-Relacionamento
![Diagrama ER](./ER.png)