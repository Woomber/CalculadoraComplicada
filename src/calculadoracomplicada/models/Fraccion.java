package calculadoracomplicada.models;

/**
 * Clase de Fracciones.
 *
 * @author Yael Arturo Chavoya Andalón 14300094
 */
public class Fraccion {

    private long numerador;
    private long denominador;

    /**
     * Constructor sin parámetros, defecto a 0 enteros.
     */
    public Fraccion() {
        this(0, 1);
    }

    /**
     * Constructor con numerador. El denominador será 1.
     *
     * @param numerador El numerador a colocar.
     */
    public Fraccion(long numerador) {
        this(numerador, 1);
    }

    /**
     * Constructor con numerador y denominador. La fracción se simplificará
     * automáticamente.
     *
     * @param numerador El numerador a colocar.
     * @param denominador El denominador a colocar.
     */
    public Fraccion(long numerador, long denominador) {
        this.set(numerador, denominador);
    }

    /**
     * Obtiene el numerador de la fracción.
     *
     * @return El numerador.
     */
    public long getNumerador() {
        return numerador;
    }

    /**
     * Obtiene el denominador de la fracción.
     *
     * @return El denominador.
     */
    public long getDenominador() {
        return denominador;
    }

    /**
     * Setter para validar las fracciones y simplificarlas.
     *
     * @param numerador El numerador a colocar.
     * @param denominador El denominador a colocar.
     * @return Verdadero si la fracción era válida.
     */
    public final boolean set(long numerador, long denominador) {
        if (denominador == 0) {
            return false;
        }
        this.numerador = numerador;
        this.denominador = denominador;
        this.simplificar();
        return true;
    }

    /**
     * Simplifica la fracción a su mínima expresión.
     */
    private void simplificar() {
        if (denominador < 0) {
            numerador *= -1;
            denominador *= -1;
        }
        long mcd = Math.abs(Fraccion.mcd(numerador, denominador));
        numerador /= mcd;
        denominador /= mcd;
    }

    /**
     * Calcula el máximo común divisor de dos números.
     *
     * @param a Primer número
     * @param b Segundo número
     * @return El máximo común divisor entre los números.
     */
    private static long mcd(long a, long b) {
        while (a != 0 && b != 0) {
            if (Math.abs(a) > Math.abs(b)) {
                a %= b;
            } else {
                b %= a;
            }
        }
        return a == 0 ? b : a;
    }

    /**
     * Suma dos fracciones.
     *
     * @param a Sumando A
     * @param b Sumando B
     * @return La suma de A + B
     */
    public static Fraccion sumar(Fraccion a, Fraccion b) {
        return new Fraccion(
                a.numerador * b.denominador
                + a.denominador * b.numerador,
                a.denominador * b.denominador);
    }

    /**
     * Resta dos fracciones.
     *
     * @param a Minuendo A
     * @param b Sustraendo B
     * @return La resta de A - B
     */
    public static Fraccion restar(Fraccion a, Fraccion b) {
        return new Fraccion(
                a.numerador * b.denominador
                - a.denominador * b.numerador,
                a.denominador * b.denominador);
    }

    /**
     * Multiplica dos fracciones.
     *
     * @param a Factor A
     * @param b Factor B
     * @return El producto de A * B
     */
    public static Fraccion multiplicar(Fraccion a, Fraccion b) {
        return new Fraccion(
                a.numerador * b.numerador,
                a.denominador * b.denominador);
    }

    /**
     * Divide dos fracciones.
     *
     * @param a Dividendo A
     * @param b Divisor B
     * @return El cociente de A / B
     */
    public static Fraccion dividir(Fraccion a, Fraccion b) {
        if (b.numerador == 0) {
            return null;
        }
        return new Fraccion(
                a.numerador * b.denominador,
                a.denominador * b.numerador);
    }

    /**
     * Convierte una cadena que contiene una fracción a un objeto tipo Fraccion.
     *
     * @param cadena La cadena a convertir.
     * @return Un objeto Fraccion si se pudo convertir, null si no.
     */
    public static Fraccion parse(String cadena) {
        String[] separar = cadena.split("/");
        long num, den;
        try {
            switch (separar.length) {
                case 1:
                    num = Long.parseLong(separar[0]);
                    den = 1;
                    break;
                case 2:
                    num = Long.parseLong(separar[0]);
                    den = Long.parseLong(separar[1]);
                    break;
                default:
                    return null;
            }
        } catch (Exception ex) {
            return null;
        }

        return new Fraccion(num, den);
    }

    /**
     * Convierte la fracción a una cadena.
     *
     * @return Una cadena con la fracción
     */
    @Override
    public String toString() {
        if (denominador == 0) {
            return "NaN";
        }
        if (denominador == 1) {
            return "" + numerador;
        }
        return numerador + "/" + denominador;
    }
}
