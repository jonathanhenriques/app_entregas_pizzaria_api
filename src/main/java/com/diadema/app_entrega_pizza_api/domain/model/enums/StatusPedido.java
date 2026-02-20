package model.enums;

public enum StatusPedido {
    AGUARDANDO_PREPARO, // Pedido feito pelo cliente
    EM_PREPARO,         // Cozinha aceitou
    SAIU_PARA_ENTREGA,  // Motoboy pegou
    ENTREGUE            // Finalizado
}