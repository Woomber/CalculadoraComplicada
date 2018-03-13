package calculadoracomplicada;

import calculadoracomplicada.models.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Vista Calculadora
 *
 * @author Yael Arturo Chavoya Andalón 14300094
 */
public class CalcView extends JFrame {

    // Layouts y panels
    private GroupLayout layoutBase;
    private FlowLayout layoutModes;
    private GridLayout layoutButtons;
    private JPanel panelModes, panelButtons;

    // TextField de respuesta
    private JTextField txtResult;

    // Botones
    private JButton[] btnOperations, btnNumbers, btnClear;
    private JButton btnPoint, btnImaginary;
    private JButton btnSign;

    private JRadioButton[] radioModes;

    // Modo actual
    private CalcMode mode;

    // Operación actual
    private String operation;
    
    // Operandos
    private String data1;
    private String data2;

    /**
     * Constructor
     */
    public CalcView() {

        setStyle(1);

        buildWindow();
        buildButtons();
        buildScreen();
        buildMode();

        setupWindowPanels();

        setMode(CalcMode.Complejos);
    }

    //<editor-fold defaultstate="collapsed" desc="Diseño interfaz">
    
    /**
     * Establece el estilo para la ventana
     * 
     * @param style Estilo a escoger
     */
    private void setStyle(int style) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getInstalledLookAndFeels()[style]
                            .getClassName());
            this.repaint();
        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | UnsupportedLookAndFeelException ex) {
            System.out.println("Error cargando estilo: " + ex.getMessage());
        }
    }

    /**
     * Crea la ventana.
     */
    private void buildWindow() {
        this.setTitle("Calculadora");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 250);
        this.layoutBase = new GroupLayout(this.getContentPane());
        this.setMinimumSize(new Dimension(265, 385));
    }

    /**
     * Crea los paneles para organizar el contenido.
     */
    private void setupWindowPanels() {
        this.layoutBase.setHorizontalGroup(
                layoutBase.createParallelGroup()
                        .addComponent(this.txtResult, 250, 250, Integer.MAX_VALUE)
                        .addComponent(this.panelModes, 250, 250, Integer.MAX_VALUE)
                        .addComponent(this.panelButtons, 250, 250, Integer.MAX_VALUE)
        );
        this.layoutBase.setVerticalGroup(
                layoutBase.createSequentialGroup()
                        .addComponent(this.txtResult, 40, 40, 70)
                        .addComponent(this.panelModes, 35, 35, 35)
                        .addComponent(this.panelButtons, 250, 250, Integer.MAX_VALUE)
        );
        this.setLayout(layoutBase);
        this.pack();
    }

    /**
     * Crea los botones y les asigna sus listeners.
     */
    private void buildButtons() {
        this.btnNumbers = new JButton[10];
        for (int i = 0; i < btnNumbers.length; i++) {
            this.btnNumbers[i] = new JButton(String.valueOf(i));
            this.btnNumbers[i].addActionListener(this.numberButtonPressed);
        }

        this.btnPoint = new JButton(".");
        this.btnPoint.addActionListener(this.pointButtonPressed);

        this.btnImaginary = new JButton("i");
        this.btnImaginary.addActionListener(this.imaginaryButtonPressed);

        this.btnSign = new JButton("±");
        this.btnSign.addActionListener(this.signButtonPressed);

        this.btnClear = new JButton[2];
        this.btnClear[0] = new JButton("CE");
        this.btnClear[1] = new JButton("C");
        this.btnClear[0].addActionListener(this.clearButtonPressed);
        this.btnClear[1].addActionListener(this.clearButtonPressed);

        this.btnOperations = new JButton[5];
        btnOperations[0] = new JButton("=");
        btnOperations[1] = new JButton("+");
        btnOperations[2] = new JButton("-");
        btnOperations[3] = new JButton("x");
        btnOperations[4] = new JButton("÷");

        for (JButton btn : this.btnOperations) {
            btn.addActionListener(this.operationButtonPressed);
        }

        this.layoutButtons = new GridLayout(5, 4, 1, 1);
        this.panelButtons = new JPanel(layoutButtons);

        panelButtons.add(btnClear[0]);
        panelButtons.add(btnClear[1]);
        panelButtons.add(btnSign);
        panelButtons.add(btnOperations[4]);
        panelButtons.add(btnNumbers[7]);
        panelButtons.add(btnNumbers[8]);
        panelButtons.add(btnNumbers[9]);
        panelButtons.add(btnOperations[3]);
        panelButtons.add(btnNumbers[4]);
        panelButtons.add(btnNumbers[5]);
        panelButtons.add(btnNumbers[6]);
        panelButtons.add(btnOperations[2]);
        panelButtons.add(btnNumbers[1]);
        panelButtons.add(btnNumbers[2]);
        panelButtons.add(btnNumbers[3]);
        panelButtons.add(btnOperations[1]);
        panelButtons.add(btnImaginary);
        panelButtons.add(btnNumbers[0]);
        panelButtons.add(btnPoint);
        panelButtons.add(btnOperations[0]);

        for (Component btn : panelButtons.getComponents()) {
            btn.setFont(new Font("Arial", Font.PLAIN, 24));
        }
    }

    /**
     * Crea la pantalla de resultados y le asigna su listener.
     */
    private void buildScreen() {
        this.txtResult = new JTextField();
        this.txtResult.setEditable(false);
        this.txtResult.setHorizontalAlignment(JTextField.RIGHT);
        this.txtResult.setText("0");
        this.txtResult.setFont(new Font("Consolas", Font.PLAIN, 28));
        this.txtResult.getDocument().addDocumentListener(txtResultChanged);
    }

    /**
     * Crea los botones de radio de cambio de modo y les asigna sus listeners.
     */
    private void buildMode() {

        this.radioModes = new JRadioButton[2];
        this.radioModes[0] = new JRadioButton(CalcMode.Complejos.toString());
        this.radioModes[1] = new JRadioButton(CalcMode.Fracciones.toString());

        this.layoutModes = new FlowLayout();
        this.panelModes = new JPanel(layoutModes);

        ButtonGroup modosGroup = new ButtonGroup();
        modosGroup.add(radioModes[0]);
        modosGroup.add(radioModes[1]);

        this.radioModes[0].addActionListener(this.radioClick);
        this.radioModes[1].addActionListener(this.radioClick);

        this.panelModes.add(radioModes[0]);
        this.panelModes.add(radioModes[1]);

        this.radioModes[0].setSelected(true);

    }
    //</editor-fold>

    /**
     * Borrar todos cálculos
     */
    private void clear() {

        clearOld();
        clearThis();
    }

    /**
     * Borrar los cálculos anteriores al actual.
     */
    private void clearOld() {
        this.data1 = "";
        this.operation = "";
    }

    /**
     * Borrar el dato ingresado actualmente sin borrar los anteriores.
     */
    private void clearThis() {
        this.data2 = "0";
        this.txtResult.setText(data2);
    }

    /**
     * Establece el modo de calculadora, cambia los botones y borra los
     * cálculos.
     *
     * @param mode El modo en el que se va a manejar
     */
    private void setMode(CalcMode mode) {
        switch (mode) {
            case Fracciones:
                btnImaginary.setVisible(false);
                btnPoint.setText("/");
                break;

            case Complejos:
                btnImaginary.setVisible(true);
                btnPoint.setText(".");
                break;

        }
        this.mode = mode;
        this.clear();
    }

    /**
     * Cambiar el tamaño de letra de la pantalla de resultados dependiendo de
     * la longitud de su contenido.
     */
    private void resizeScreenFont() {
        if (this.txtResult.getText().length() > 15) {
            this.txtResult.setFont(new Font("Consolas", Font.PLAIN, 16));
        } else {
            this.txtResult.setFont(new Font("Consolas", Font.PLAIN, 28));
        }
    }
    
    /**
     * Resuelve los cálculos, aplicando el operador a los operandos.
     */
    void solve() {
        String result;

        if (this.data1.equals("")) {
            this.data1 = "0";
        }

        if (this.mode == CalcMode.Fracciones) {

            Fraccion a = Fraccion.parse(this.data1);
            Fraccion b = Fraccion.parse(this.data2);

            Fraccion resultado;

            switch (this.operation) {
                case "+":
                    resultado = Fraccion.sumar(a, b);
                    break;
                case "-":
                    resultado = Fraccion.restar(a, b);
                    break;
                case "x":
                    resultado = Fraccion.multiplicar(a, b);
                    break;
                case "=":
                    resultado = a;
                    break;
                default:
                    resultado = Fraccion.dividir(a, b);
            }
            result = resultado.toString();

        } else {

            Complejo a = Complejo.parse(this.data1);
            Complejo b = Complejo.parse(this.data2);

            Complejo resultado;

            switch (this.operation) {
                case "+":
                    resultado = Complejo.sumar(a, b);
                    break;
                case "-":
                    resultado = Complejo.restar(a, b);
                    break;
                case "x":
                    resultado = Complejo.multiplicar(a, b);
                    break;
                case "=":
                    resultado = a;
                    break;
                default:
                    resultado = Complejo.dividir(a, b);
            }
            result = resultado.toString();
            if (result.matches(".*NaN.*")) {
                result = "NaN";
            }
        }
        result = result.replaceAll("^0\\.0$", "0");
        data1 = result;

        clearThis();
        this.txtResult.setText(result);
    }


    //<editor-fold defaultstate="collapsed" desc="Listeners">
    
    /**
     * Espera a que se presionen los radios de cambio de modo
     */
    private final ActionListener radioClick = (ActionEvent e) -> {
        if (e.getSource() == CalcView.this.radioModes[0]) {
            CalcView.this.setMode(CalcMode.Complejos);
        }
        if (e.getSource() == CalcView.this.radioModes[1]) {
            CalcView.this.setMode(CalcMode.Fracciones);
        }
    };

    /**
     * Cambia a ingresar la parte imaginaria si no existe aún
     */
    private final ActionListener imaginaryButtonPressed = (ActionEvent e) -> {
        if (!this.data2.matches(".+i")) {
            this.data2 += "+0i";
        }
        this.txtResult.setText(data2);
    };

    /**
     * Cambia entre signo positivo y negativo en la parte que se esté ingresando
     */
    private final ActionListener signButtonPressed = (ActionEvent e) -> {
        if (this.data2.matches(".+i")) {
            //Es un número imaginario
            if (this.data2.matches("\\-.+\\-.+")) {
                //Es negativo y negativo
                this.data2 = "-" + this.data2.substring(1).replaceAll("\\-", "+");
            } else {
                //Es positivo
                if (this.data2.matches(".+\\+.+")) {
                    this.data2 = this.data2.replaceAll("\\+", "-");
                } else {
                    this.data2 = this.data2.replaceAll("\\-", "+");
                }
            }
        } else {
            //Es un número real
            if (this.data2.matches("\\-.+")) {
                this.data2 = this.data2.substring(1);
            } else {
                this.data2 = "-" + this.data2;
            }
        }
        this.txtResult.setText(this.data2);
    };

    /**
     * Cambia el tamaño de la pantalla cuando cambia el texto
     */
    private final DocumentListener txtResultChanged = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            resizeScreenFont();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            resizeScreenFont();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }

    };

    /**
     * Espera a que se presione un número
     */
    private final ActionListener numberButtonPressed = (ActionEvent e) -> {

        // Si hay un cero, quitarlo para evitar tener "04"
        if (this.data2.matches("-?0")) {
            this.data2 = this.data2.replaceAll("(.*)0", "$1");
        }

        // Obtener el número presionado
        String number = String.valueOf(((JButton) e.getSource()).getText());

        if (this.data2.matches(".+i")) {
            // Si tiene imaginario, colocar nuevos números en esta parte
            if (this.data2.matches(".+[\\+\\-]0i")) {
                //Remover el cero si tiene
                this.data2 = this.data2.replaceAll("(.*)0i", "$1");
            } else {
                this.data2 = this.data2.replaceAll("(.*)i", "$1");
            }
            this.data2 += number + "i";
        } else {
            this.data2 += number;
        }
        this.txtResult.setText(data2);
    };

    /**
     * Espera a que se presione el botón de punto
     */
    private final ActionListener pointButtonPressed = (ActionEvent e) -> {
        // Si ya hay un punto ("." o "/"), no colocar otro
        if(this.data2.matches(".*\\..+\\..*i")){
            return;
        }
        if (this.data2.matches(".*[\\./].*") && !this.data2.matches(".*i")) {
            return;
        }
        
        if (this.mode == CalcMode.Complejos) {
            if (this.data2.matches(".+i")) {
                // Si está en parte imaginaria, colocarlo ahí
                this.data2 = this.data2.replaceAll("(.*)i", "$1");
                this.data2 += ".i";
            } else {
                this.data2 += ".";
            }

        } else if (this.mode == CalcMode.Fracciones) {
            this.data2 += "/";
        }
        this.txtResult.setText(data2);
    };

    /**
     * Borrar al presionar un botón de borrar
     */
    private final ActionListener clearButtonPressed = (ActionEvent e) -> {
        if (e.getSource() == this.btnClear[0]) {
            this.clearThis();
        } else {
            this.clear();
        }
    };

    /**
     * Esperar a que se presione un botón de operación
     */
    private final ActionListener operationButtonPressed = (ActionEvent e) -> {
        // Obtener la operación
        String button = ((JButton) e.getSource()).getText();

        // Si se presiona igual dos veces, quitar operación
        if (button.equals("=") && this.operation.equals("=")) {
            this.operation = "";
        }

        // Primer operando listo
        if (this.operation.equals("")) {
            this.data1 = this.data2;
            this.operation = button;
            if (button.equals("=")) {
                solve();
            } else {
                clearThis();
            }
            return;
        }

        //Segundo operando listo
        solve();
        this.operation = button;

    };
    //</editor-fold>

}
