INSERT INTO project (id, titulo, status) VALUES (1, 'Projeto Demo A', 'PLANEJADO');
INSERT INTO project (id, titulo, status) VALUES (2, 'Projeto Demo B', 'EM_ANDAMENTO');

INSERT INTO task (titulo, descricao, prioridade, projeto_id)
VALUES ('Tarefa 1', 'Exemplo A', 2, 1);
INSERT INTO task (titulo, descricao, prioridade, projeto_id)
VALUES ('Tarefa 2', 'Exemplo B', 3, 1);
INSERT INTO task (titulo, descricao, prioridade, projeto_id)
VALUES ('Tarefa 3', 'Exemplo C', 1, 2);
