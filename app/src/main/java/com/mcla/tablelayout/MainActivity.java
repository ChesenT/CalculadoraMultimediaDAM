package com.mcla.tablelayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;
    private TextView tvOperacion;

    private Button buttonUno, buttonDos, buttonTres, buttonCuatro, buttonCinco, buttonSeis,
            buttonSiete, buttonOcho, buttonNueve, buttonCero,
            buttonSuma, buttonResta, buttonMultiplica, buttonDivide,
            buttonIgual, buttonLimpiar;

    private Button buttonPotencia, buttonModulo, buttonDel, buttonPunto;

    private double num1 = 0;
    private double num2 = 0;

    // Para repetir con "=" (estilo Windows)
    private double lastNum2 = 0;
    private String lastOperador = "";

    private String operador = "";
    private boolean esperandoSegundoNumero = false;

    // NUEVO: comportamiento Windows
    private boolean empezarNuevoNumero = false; // sustituye al escribir
    private boolean resultadoMostrado = false;  // tras pulsar "="

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textResultado);
        tvOperacion = findViewById(R.id.textHistorial);

        buttonUno = findViewById(R.id.buttonUno);
        buttonDos = findViewById(R.id.buttonDos);
        buttonTres = findViewById(R.id.buttonTres);
        buttonCuatro = findViewById(R.id.buttonCuatro);
        buttonCinco = findViewById(R.id.buttonCinco);
        buttonSeis = findViewById(R.id.buttonSeis);
        buttonSiete = findViewById(R.id.buttonSiete);
        buttonOcho = findViewById(R.id.buttonOcho);
        buttonNueve = findViewById(R.id.buttonNueve);
        buttonCero = findViewById(R.id.buttonCero);

        buttonSuma = findViewById(R.id.buttonSuma);
        buttonResta = findViewById(R.id.buttonResta);
        buttonMultiplica = findViewById(R.id.buttonMultiplica);
        buttonDivide = findViewById(R.id.buttonDivide);

        buttonIgual = findViewById(R.id.buttonIgual);
        buttonLimpiar = findViewById(R.id.buttonLimpiar);

        buttonPotencia = findViewById(R.id.buttonPotencia);
        buttonModulo = findViewById(R.id.buttonModulo);
        buttonDel = findViewById(R.id.buttonDel);
        buttonPunto = findViewById(R.id.buttonPunto);

        //Asignamos OnClickListener
        buttonUno.setOnClickListener(this);
        buttonDos.setOnClickListener(this);
        buttonTres.setOnClickListener(this);
        buttonCuatro.setOnClickListener(this);
        buttonCinco.setOnClickListener(this);
        buttonSeis.setOnClickListener(this);
        buttonSiete.setOnClickListener(this);
        buttonOcho.setOnClickListener(this);
        buttonNueve.setOnClickListener(this);
        buttonCero.setOnClickListener(this);

        buttonSuma.setOnClickListener(this);
        buttonResta.setOnClickListener(this);
        buttonMultiplica.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);

        buttonPotencia.setOnClickListener(this);
        buttonModulo.setOnClickListener(this);
        buttonDel.setOnClickListener(this);
        buttonPunto.setOnClickListener(this);

        buttonIgual.setOnClickListener(this);
        buttonLimpiar.setOnClickListener(this);

        // Al iniciar: mostrar 0 (como Windows)
        tv.setText("0");
        tvOperacion.setText("");
    }

    @Override
    public void onClick(View v) {

        // Numeros
        if (v == buttonUno) escribirNumero("1");
        else if (v == buttonDos) escribirNumero("2");
        else if (v == buttonTres) escribirNumero("3");
        else if (v == buttonCuatro) escribirNumero("4");
        else if (v == buttonCinco) escribirNumero("5");
        else if (v == buttonSeis) escribirNumero("6");
        else if (v == buttonSiete) escribirNumero("7");
        else if (v == buttonOcho) escribirNumero("8");
        else if (v == buttonNueve) escribirNumero("9");
        else if (v == buttonCero) escribirNumero("0");

            // Punto decimal (.)
        else if (v == buttonPunto) {
            String actual = tv.getText().toString();

            if (resultadoMostrado) {
                tvOperacion.setText("");
                tv.setText("0,");
                resultadoMostrado = false;
                empezarNuevoNumero = false;
                return;
            }

            if (empezarNuevoNumero) {
                tv.setText("0,");
                empezarNuevoNumero = false;
                return;
            }

            if (!actual.contains(",")) {
                tv.setText(actual + ",");
            }
        }

        // Operadores (incluye ^ y %)
        else if (v == buttonSuma || v == buttonResta || v == buttonMultiplica || v == buttonDivide
                || v == buttonPotencia) {

            String nuevoOperador = "";
            if (v == buttonSuma) nuevoOperador = "+";
            else if (v == buttonResta) nuevoOperador = "-";
            else if (v == buttonMultiplica) nuevoOperador = "*";
            else if (v == buttonDivide) nuevoOperador = "/";
            else if (v == buttonPotencia) nuevoOperador = "^";

            // Si estoy esperando segundo número y aún no he escrito nada, cambiar operador (Windows)
            if (esperandoSegundoNumero && empezarNuevoNumero) {
                operador = nuevoOperador;
                tvOperacion.setText(formatear(num1) + " " + " " + operador);
                return;
            }

            // Guardar/actualizar num1 con lo que hay en pantalla
            String textoPantalla = tv.getText().toString();
            if (!textoPantalla.isEmpty()) {
                double current = toDouble(textoPantalla);


                if (!operador.isEmpty() && esperandoSegundoNumero && !empezarNuevoNumero) {
                    // Encadenar: 2 + 3 + 4 ...
                    num1 = calcular(num1, current, operador);
                    tv.setText(formatear(num1));
                } else {
                    num1 = current;
                }
            }

            operador = nuevoOperador;
            esperandoSegundoNumero = true;
            empezarNuevoNumero = true;   // el siguiente número sustituye
            resultadoMostrado = false;

            tvOperacion.setText(formatear(num1) + " " + " " +operador);

            // IMPORTANTE: NO ponemos 0 aquí -> se queda mostrando el num1 (Windows)
            tv.setText(formatear(num1));
        }


        // Botón % estilo Windows real
// Botón % estilo Windows real
        else if (v == buttonModulo) {

            // Solo funciona si hay operador activo
            if (!operador.isEmpty() && !tv.getText().toString().isEmpty()) {

                double valorActual = toDouble(tv.getText().toString());
                double porcentaje = 0;

                if (operador.equals("+") || operador.equals("-")) {
                    // B% de A
                    porcentaje = num1 * valorActual / 100;
                } else if (operador.equals("*") || operador.equals("/")) {
                    // Para multiplicar o dividir solo convierte B en B/100
                    porcentaje = valorActual / 100;
                } else if (operador.equals("^")) {
                    porcentaje = valorActual / 100;
                }

                // Mostrar el porcentaje en la pantalla (abajo)
                tv.setText(formatear(porcentaje));

                // ✅ IMPORTANTE: actualizar historial arriba con el porcentaje calculado
                // Ej: "50+10" (o con espacios si tú quieres)
                tvOperacion.setText(formatear(num1) + " " + operador + " " + formatear(porcentaje));

                // ✅ Guardar num2 ya convertido para que "=" use 10 y no 20
                num2 = porcentaje;

                // Ahora num2 ya es el porcentaje transformado
                empezarNuevoNumero = true;   // el siguiente número reemplaza (como Windows)
                resultadoMostrado = false;
            }
        }


        // Igual
        else if (v == buttonIgual) {

            // Caso 1: tengo operador y un segundo número (o si no he escrito, uso el mismo num1 como hace Windows a veces)
            if (!operador.isEmpty()) {

                String texto = tv.getText().toString();
                if (texto.isEmpty()) texto = "0";

                // Si justo acabo de pulsar operador y no escribí segundo número, en Windows suele usar num1 como num2
                if (empezarNuevoNumero) {
                    num2 = num1;
                } else {
                    num2 = toDouble(texto);

                }

                double resultado = calcular(num1, num2, operador);

                tvOperacion.setText(formatear(num1) + " " + operador + " " + formatear(num2) + " =");
                tv.setText(formatear(resultado));

                lastNum2 = num2;
                lastOperador = operador;

                num1 = resultado;
                num2 = 0;
                operador = "";
                esperandoSegundoNumero = false;

                // tras resultado: siguiente número sustituye
                resultadoMostrado = true;
                empezarNuevoNumero = true;
            }

            // Caso 2: repetir operación si pulso "=" otra vez (Windows)
            else if (!lastOperador.isEmpty()) {
                double resultado = calcular(num1, lastNum2, lastOperador);

                tvOperacion.setText(formatear(num1) + " " + lastOperador + " " + formatear(lastNum2) + " =");
                tv.setText(formatear(resultado));

                num1 = resultado;

                resultadoMostrado = true;
                empezarNuevoNumero = true;
            }
        }

        // DEL (borrar último)
        else if (v == buttonDel) {
            String actual = tv.getText().toString();

            // Si acabo de mostrar resultado y doy DEL, lo dejo en 0 (comportamiento típico)
            if (resultadoMostrado) {
                tv.setText("0");
                resultadoMostrado = false;
                empezarNuevoNumero = false;
                return;
            }

            if (actual.isEmpty() || actual.equals("0")) {
                tv.setText("0");
            } else {
                String nuevo = actual.substring(0, actual.length() - 1);
                if (nuevo.isEmpty() || nuevo.equals("-")) tv.setText("0");
                else tv.setText(nuevo);
            }
        }

        // Limpiar / C
        else if (v == buttonLimpiar) {
            tv.setText("0");
            tvOperacion.setText("");
            num1 = 0;
            num2 = 0;
            operador = "";
            esperandoSegundoNumero = false;

            lastNum2 = 0;
            lastOperador = "";

            empezarNuevoNumero = false;
            resultadoMostrado = false;
        }
    }

    // Escribir números estilo Windows:
    // - si hay "0", sustituye
    // - si acabo de mostrar resultado, sustituye
    // - si acabo de pulsar operador (empezarNuevoNumero), sustituye
    private void escribirNumero(String n) {

        String actual = tv.getText().toString();

        if (resultadoMostrado) {
            tvOperacion.setText("");
        }

        if (resultadoMostrado || empezarNuevoNumero || actual.equals("0")) {
            actual = n;
            resultadoMostrado = false;
            empezarNuevoNumero = false;
        } else {
            actual = actual + n;
        }

        // Si tiene decimal
        if (actual.contains(",")) {

            String[] partes = actual.split(",");

            // Parte entera limpia
            String entero = partes[0].replace(".", "");
            String decimal = partes.length > 1 ? partes[1] : "";

            double numero = Double.parseDouble(entero); // aquí entero YA está limpio (sin puntos)

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');

            DecimalFormat df = new DecimalFormat("#,##0", symbols);

            String enteroFormateado = df.format(numero);

            tv.setText(enteroFormateado + "," + decimal);

        } else {

            String limpio = actual.replace(".", "");
            double numero = Double.parseDouble(limpio);

            tv.setText(formatear(numero));
        }
    }

    private double toDouble(String s) {
        if (s == null || s.isEmpty()) return 0;

        // Quita miles (.) y cambia decimal (,) por (.)
        s = s.replace(".", "").replace(",", ".");
        return Double.parseDouble(s);
    }


    // Metodo auxiliar para calcular
    private double calcular(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b != 0 ? a / b : 0;
            case "^": return Math.pow(a, b);
            default: return 0;
        }
    }

    // Formato: máximo 2 decimales y separador "."
    private String formatear(double n) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // miles
        symbols.setDecimalSeparator(',');  // decimales

        DecimalFormat df = new DecimalFormat("#,##0.##", symbols);
        return df.format(n);
    }

}
