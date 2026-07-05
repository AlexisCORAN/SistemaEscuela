/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package shared;

/**
 *
 * @author Alexis
 */
public enum Bimestre {
    PRIMERO(1),
    SEGUNDO(2),
    TERCERO(3),
    CUARTO(4);

    private final int valorId;

    Bimestre(int valorId) {
        this.valorId = valorId;
    }

    public int getValorId() {
        return valorId;
    }

    public static Bimestre desdeId(int id) {
        for (Bimestre b : Bimestre.values()) {
            if (b.getValorId() == id) {
                return b;
            }
        }
        throw new IllegalArgumentException("ID de bimestre inválido en la base de datos: " + id);
    }
}
