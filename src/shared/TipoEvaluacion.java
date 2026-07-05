package shared;

public enum TipoEvaluacion {
    PRACTICA(0.20),    
    TAREA(0.20),
    PARCIAL(0.30),
    BIMESTRAL(0.30);   

    private final double peso;

    TipoEvaluacion(double peso) {
        this.peso = peso;
    }

    public double getPeso() {
        return this.peso;
    }
}