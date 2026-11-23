package model.enums;

public enum Faixa {
    BRANCA,
    AMARELA,
    VERMELHA,
    LARANJA,
    VERDE,
    ROXA,
    MARROM,
    PRETA;


    public static Faixa getFaixaByName(String nome) {
        for (Faixa faixa : Faixa.values()) {
            if (faixa.name().equalsIgnoreCase(nome)) {
                return faixa;
            }
        }
        return null;
    }
}
