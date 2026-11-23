package model.enums;

public enum CargoInstrutor {
    AUXILIAR,
    SENSEI,
    COORDENADOR;

    public static CargoInstrutor getCargoByName(String nome) {
        for (CargoInstrutor cargo : CargoInstrutor.values()) {
            if (cargo.name().equalsIgnoreCase(nome)) {
                return cargo;
            }
        }
        return null;
    }
}
