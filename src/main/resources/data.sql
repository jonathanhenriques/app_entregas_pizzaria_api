-- ==================================================================================
-- 1. CADASTRO DE MOTOBOYS
-- ==================================================================================
INSERT INTO tb_motoboys (id, nome, cpf, telefone)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'João Motoboy', '111.111.111-11', '11 99999-1001');

INSERT INTO tb_motoboys (id, nome, cpf, telefone)
VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b22', 'Pedro Entregas', '222.222.222-22', '11 99999-1002');

INSERT INTO tb_motoboys (id, nome, cpf, telefone)
VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Lucas Rápido', '333.333.333-33', '11 99999-1003');

INSERT INTO tb_motoboys (id, nome, cpf, telefone)
VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', 'Marcos Novato', '444.444.444-44', '11 99999-1004');

-- ==================================================================================
-- 2. CADASTRO DE USUÁRIOS
-- Senha padrão para testes: 'admin123' (exemplo de hash BCrypt)
-- ==================================================================================
INSERT INTO tb_usuarios (id, login, password, role)
VALUES (random_uuid(), 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00dmxs.TVuHOnu', 'ADMIN');

INSERT INTO tb_usuarios (id, login, password, role)
VALUES (random_uuid(), 'joao', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00dmxs.TVuHOnu', 'USER');

-- ==================================================================================
-- 3. PEDIDOS NA COZINHA
-- ==================================================================================
INSERT INTO tb_pedidos (id, nome_cliente, endereco, telefone, sabores, valor_total, forma_pagamento, status, data_hora_pedido, nota_fiscal, distancia_km, tempo_estimado_min)
VALUES (random_uuid(), 'Ana Souza', 'Rua das Flores, 100', '11 91111-1111', 'Mussarela', 45.00, 'DINHEIRO', 'AGUARDANDO_PREPARO', CURRENT_TIMESTAMP, 'NF-1001', NULL, NULL);

INSERT INTO tb_pedidos (id, nome_cliente, endereco, telefone, sabores, valor_total, forma_pagamento, status, data_hora_pedido, nota_fiscal, distancia_km, tempo_estimado_min)
VALUES (random_uuid(), 'Bruno Lima', 'Av. Paulista, 500', '11 92222-2222', 'Calabresa', 55.50, 'CARTAO CREDITO', 'AGUARDANDO_PREPARO', CURRENT_TIMESTAMP, 'NF-1002', NULL, NULL);

-- ==================================================================================
-- 4. HISTÓRICO PARA O RANKING (ENTREGUE HOJE)
-- ==================================================================================
INSERT INTO tb_pedidos (id, nome_cliente, endereco, telefone, sabores, valor_total, forma_pagamento, status, data_hora_pedido, data_hora_entrega, motoboy_id, nota_fiscal, distancia_km, tempo_estimado_min)
VALUES (random_uuid(), 'Teste Rapidez', 'Rua Proxima, 10', '11 99999-9999', 'Portuguesa', 60.00, 'PIX', 'ENTREGUE', DATEADD('MINUTE', -30, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'NF-H999', 2.0, 10);

INSERT INTO tb_pedidos (id, nome_cliente, endereco, telefone, sabores, valor_total, forma_pagamento, status, data_hora_pedido, data_hora_entrega, motoboy_id, nota_fiscal, distancia_km, tempo_estimado_min)
VALUES (random_uuid(), 'Kleber Bambam', 'Academia', '11 93333-3333', 'Frango', 35.00, 'PIX', 'ENTREGUE', DATEADD('MINUTE', -45, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b22', 'NF-H001', 4.0, 20);

-- Exemplo de comando SQL para promover um usuário no seu banco H2
-- UPDATE tb_usuarios SET role = 'ADMIN' WHERE login = 'seu_login';