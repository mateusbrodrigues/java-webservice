package br.inatel.dm111promo.api.user.controller;

public record UserRequest(String name,
                          String email,
                          String password,
                          String role) {
}
