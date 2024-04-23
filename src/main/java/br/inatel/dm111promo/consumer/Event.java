package br.inatel.dm111promo.consumer;

public record Event(EventType type, Operation operation, SuperMarketListMessage data) {
}
