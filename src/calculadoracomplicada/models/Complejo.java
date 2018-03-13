package calculadoracomplicada.models;

/**
 *
 * @author Yael Arturo Chavoya Andalón 14300094
 */
public class Complejo {

    private double real;
    private double imaginario;

    public static final String IDENTIFIER = "i";

    /**
     * Constructor sin parámetros, defecto a 0.
     */
    public Complejo() {
        this.real = 0;
        this.imaginario = 0;
    }

    /**
     * Constructor con número real. El imaginario será 0.
     *
     * @param real El número real a colocar.
     */
    public Complejo(double real) {
        this.real = real;
        this.imaginario = 0;
    }

    /**
     * Constructor con número real e imaginario.
     *
     * @param real El número real a colocar.
     * @param imaginario El número imaginario a colocar.
     */
    public Complejo(double real, double imaginario) {
        this.real = real;
        this.imaginario = imaginario;
    }

    /**
     * Obtiene la parte real del número.
     *
     * @return La parte real.
     */
    public double getReal() {
        return real;
    }

    /**
     * Establece la parte real del número.
     *
     * @param real La parte real.
     */
    public void setReal(double real) {
        this.real = real;
    }

    /**
     * Obtiene la parte imaginaria del número.
     *
     * @return La parte imaginaria.
     */
    public double getImaginario() {
        return imaginario;
    }

    /**
     * Establece la parte imaginaria del número.
     *
     * @param imaginario La parte imaginaria.
     */
    public void setImaginario(double imaginario) {
        this.imaginario = imaginario;
    }

    /**
     * Establece la parte real e imaginaria del número.
     *
     * @param real La parte real.
     * @param imaginario La parte imaginaria.
     */
    public void set(double real, double imaginario) {
        this.real = real;
        this.imaginario = imaginario;
    }

    /**
     * Suma dos números complejos.
     *
     * @param a Sumando A
     * @param b Sumando B
     * @return LA suma de A + B
     */
    public static Complejo sumar(Complejo a, Complejo b) {
        return new Complejo(a.real + b.real, a.imaginario + b.imaginario);
    }

    /**
     * Resta dos números complejos.
     *
     * @param a Minuendo A
     * @param b Sustraendo B
     * @return La resta de A - B
     */
    public static Complejo restar(Complejo a, Complejo b) {
        return new Complejo(a.real - b.real, a.imaginario - b.imaginario);
    }

    /**
     * Multiplica dos números complejos.
     *
     * @param a Minuendo A
     * @param b Sustraendo B
     * @return La resta de A - B
     */
    public static Complejo multiplicar(Complejo a, Complejo b) {
        return new Complejo(
                a.real * b.real - a.imaginario * b.imaginario,
                a.real * b.imaginario + a.imaginario * b.real
        );
    }

    /**
     * Divide dos números complejos.
     *
     * @param a Dividendo A
     * @param b Divisor B
     * @return El cociente de A / B
     */
    public static Complejo dividir(Complejo a, Complejo b) {
        Complejo complemento = new Complejo(b.real, -b.imaginario);

        Complejo numerador = multiplicar(a, complemento);
        double denominador = multiplicar(b, complemento).real;

        numerador.real /= denominador;
        numerador.imaginario /= denominador;

        return numerador;

    }

    /**
     * Convierte una cadena que contiene un número complejo a un objeto tipo
     * Complejo.
     *
     * @param cadena La cadena a convertir.
     * @return Un objeto Complejo si se pudo convertir, null si no.
     */
    public static Complejo parse(String cadena) {
        double factor = 1.0;
        double real = 0.0, imaginario = 0.0;
        String minus = "";

        if (cadena.matches("-.+")) {
            cadena = cadena.substring(1);
            factor = -1.0;
        }

        if (cadena.matches(".*-.*")) {
            minus = "-";
        }

        String[] division = cadena.split("(\\-|\\+)");

        real = factor * Double.parseDouble(division[0]);

        if (division.length == 2) {
            imaginario = Double.parseDouble(minus + division[1].replaceAll("i", ""));
        }

        return new Complejo(real, imaginario);
    }

    /**
     * Decide el formato que debe tomar un número flotante como cadena según su
     * número de dígitos.
     *
     * @param number El número a dar formato.
     * @return La cadena con formato.
     */
    private static String formatString(double number) {
        final String FORMAT = "%.6f";
        if (String.valueOf(number).replaceAll(".*\\.(.*)", "$1").length() > 6) {
            return String.format(FORMAT, number);
        }
        return String.valueOf(number);

    }

    /**
     * Convierte el número complejo a una cadena.
     *
     * @return Una cadena con el número complejo.
     */
    @Override
    public String toString() {

        if (this.imaginario == 0) {
            return formatString(this.real);
        }

        if (this.real == 0) {
            return formatString(this.imaginario) + Complejo.IDENTIFIER;
        }

        if (this.imaginario < 0) {
            return formatString(this.real) + formatString(this.imaginario)
                    + Complejo.IDENTIFIER;
        }

        return formatString(this.real) + "+" + formatString(this.imaginario)
                + Complejo.IDENTIFIER;
    }

}
