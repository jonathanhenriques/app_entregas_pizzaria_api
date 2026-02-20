-- 1. CADASTRO DE MOTOBOYS (Equipe Completa)
INSERT INTO tb_motoboys (nome, cpf, telefone) VALUES ('João Motoboy (ID 1)', '111.111.111-11', '11 99999-1001');
INSERT INTO tb_motoboys (nome, cpf, telefone) VALUES ('Pedro Entregas (ID 2)', '222.222.222-22', '11 99999-1002');
INSERT INTO tb_motoboys (nome, cpf, telefone) VALUES ('Lucas Rápido (ID 3)', '333.333.333-33', '11 99999-1003');
INSERT INTO tb_motoboys (nome, cpf, telefone) VALUES ('Marcos Novato (ID 4)', '444.444.444-44', '11 99999-1004');

-- 2. PEDIDOS NA COZINHA (Disponíveis para qualquer um pegar)
-- Endpoint para testar: GET /api/pedidos/disponiveis-para-entrega
INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido)
VALUES ('Ana Souza', 'Rua das Flores, 100', '11 91111-1111', 'Mussarela', 'NF-1001', 'AGUARDANDO_PREPARO', CURRENT_TIMESTAMP);

INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido)
VALUES ('Bruno Lima', 'Av. Paulista, 500', '11 92222-2222', 'Calabresa', 'NF-1002', 'AGUARDANDO_PREPARO', CURRENT_TIMESTAMP);

INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido)
VALUES ('Carla Dias', 'Rua Augusta, 300', '11 93333-3333', 'Portuguesa', 'NF-1003', 'AGUARDANDO_PREPARO', CURRENT_TIMESTAMP);

INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido)
VALUES ('Diego Costa', 'Rua da Consolação, 10', '11 94444-4444', 'Frango c/ Catupiry', 'NF-1004', 'AGUARDANDO_PREPARO', CURRENT_TIMESTAMP);


-- 3. PEDIDOS EM ROTA (Motoboys já pegaram)
-- Endpoint para testar: GET /api/motoboys/1/entregas-pendentes

-- Entregas do João (ID 1)
INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido, motoboy_id)
VALUES ('Eliana Rocha', 'Bairro A, Rua 1', '11 95555-5555', 'Marguerita', 'NF-2001', 'SAIU_PARA_ENTREGA', CURRENT_TIMESTAMP, 1);

INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido, motoboy_id)
VALUES ('Fabio Junior', 'Bairro A, Rua 2', '11 96666-6666', '4 Queijos', 'NF-2002', 'SAIU_PARA_ENTREGA', CURRENT_TIMESTAMP, 1);

-- Entregas do Pedro (ID 2)
INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido, motoboy_id)
VALUES ('Gabriela Silva', 'Bairro B, Rua 10', '11 97777-7777', 'Baiana', 'NF-3001', 'SAIU_PARA_ENTREGA', CURRENT_TIMESTAMP, 2);

-- Entregas do Lucas (ID 3)
INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido, motoboy_id)
VALUES ('Helio Santos', 'Bairro C, Rua 20', '11 98888-8888', 'Atum', 'NF-4001', 'SAIU_PARA_ENTREGA', CURRENT_TIMESTAMP, 3);


-- 4. HISTÓRICO (Já entregues / Finalizados)
-- Endpoint para testar: GET /api/pedidos/historico/1

-- Histórico do João (ID 1)
INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido, motoboy_id, data_hora_entrega)
VALUES ('Igor Menezes', 'Centro, Rua X', '11 90000-0001', 'Pepperoni', 'NF-9001', 'ENTREGUE', '2023-10-01 19:00:00', 1, '2023-10-01 19:40:00');

INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido, motoboy_id, data_hora_entrega)
VALUES ('Julia Roberts', 'Centro, Rua Y', '11 90000-0002', 'Chocolate', 'NF-9002', 'ENTREGUE', '2023-10-02 20:00:00', 1, '2023-10-02 20:30:00');

-- Histórico do Pedro (ID 2)
INSERT INTO tb_pedidos (nome_cliente, endereco, telefone, sabores, nota_fiscal, status, data_hora_pedido, motoboy_id, data_hora_entrega)
VALUES ('Kleber Machado', 'Zona Sul, Rua Z', '11 90000-0003', 'Mussarela', 'NF-9003', 'ENTREGUE', '2023-10-03 21:00:00', 2, '2023-10-03 21:45:00');
