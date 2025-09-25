# Sistema de Gestão de Projetos e Equipes

Projeto desenvolvido como parte da avaliação **A3 – Programação de Soluções Computacionais**.  
Aqui aplicamos **POO, Spring Boot e banco de dados relacional** em um sistema simples para cadastrar usuários, projetos e equipes.

##  Tecnologias utilizadas
- **Java 17**
- **Spring Boot 3**
- **Maven**
- **Spring Data JPA**
- **Thymeleaf**
- **Banco H2** (perfil de desenvolvimento e testes)
- **PostgreSQL** (perfil de produção)

##  Estrutura do projeto
- `models` → entidades principais (Usuario, Projeto, Equipe)  
- `repositories` → repositórios JPA  
- `controllers` → controladores REST e MVC  
- `templates` → páginas Thymeleaf simples (listas)  
- `resources` → configurações e perfis (H2 e PostgreSQL)  

##  Como executar
1. Rodar todos os testes:
   ```bash
   mvn test
   ```

2. Iniciar a aplicação (perfil padrão usa H2 em memória):
   ```bash
   mvn spring-boot:run
   ```

3. Executar com perfil `prod` (Postgres) — configure `application-prod.properties` com sua URL/usuário/senha e rode:
   ```bash
   mvn -Dspring-boot.run.profiles=prod spring-boot:run
   ```

4. Usuários de teste (criadas nos testes):
   - admin / admin  (perfil ADMIN)
   - col / col      (perfil COLABORADOR)

5. Testes úteis:
   - Rodar apenas os testes de segurança:
     ```bash
     mvn -Dtest=**/*Security* test
     ```

6. Endpoints importantes:
   - `GET /api/teste`  — retorna texto simples para checar se a API está no ar
   - `GET /api/usuarios` — CRUD de usuários (protegido: apenas ADMIN)
   - `GET /relatorios` — página com projetos agrupados por status (requer autenticação)

7. Observações:
   - Os templates Thymeleaf ficam em `src/main/resources/templates`.
   - O banco H2 é usado em memória para testes e perfil `dev`.
   - Para desenvolvimento, use `mvn -Dspring-boot.run.profiles=dev spring-boot:run` se quiser habilitar configurações específicas de `dev`.
