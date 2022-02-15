package com.julia.topic9_database_i;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // creating variables for our edittext, button and dbhandler
    private EditText tituloCaja, comentarioCaja, comentarioBuscadoCaja;
    private Button addCommentBtn, verBtn, borrarBtn;
    private DBHandler dbHandler;
    private Spinner spinner;
    private List<String>  comentarios;
    private ArrayAdapter<String> arrayAdapter;
    int positionTituloSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing all our variables.
        tituloCaja = findViewById(R.id.titulo);
        comentarioCaja = findViewById(R.id.comentario);
        addCommentBtn = findViewById(R.id.botonCrear);
        verBtn = findViewById(R.id.botonVer);
        spinner = findViewById(R.id.spinner);
        comentarioBuscadoCaja = findViewById(R.id.comentarioBuscado);
        borrarBtn = findViewById(R.id.botonBorrar);

        // creating a new dbhandler class and passing our context to it.
        dbHandler = new DBHandler(MainActivity.this);

        // //------------------------------BOTON AÑADIR COMENTARIO
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is to get data from all edit text fields.
                String titulo = tituloCaja.getText().toString();
                String comentario = comentarioCaja.getText().toString();

                // validating if the text fields are empty or not.
                if (titulo.isEmpty() && comentario.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }
                // on below line we are calling a method to add new course to sqlite data and pass all our values to it.
                dbHandler.addNewCourse(titulo, comentario);

                // after adding the data we are displaying a toast message.
                Toast.makeText(MainActivity.this, "Comment added...", Toast.LENGTH_SHORT).show();
                tituloCaja.setText("");
                comentarioCaja.setText("");

                //actualizar el spinner
                comentarios = dbHandler.getProducts();
                arrayAdapter.notifyDataSetChanged();
            }
        });

        //------------------------------BOTON VER
        verBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringTitulo = comentarios.get(positionTituloSeleccionado);
                Log.d("CON", stringTitulo);
                String resultado = dbHandler.buscarComentario(stringTitulo);
                Log.d("CON", resultado);
                comentarioBuscadoCaja.setText(resultado);
            }
        });

        //------------------------------BOTON BORRAR
        borrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringTitulo = comentarios.get(positionTituloSeleccionado);
                dbHandler.borrarComentario(stringTitulo);
                Toast.makeText(MainActivity.this, "Comment deleted...", Toast.LENGTH_SHORT).show();
                //actualizar el spinner
                comentarios = dbHandler.getProducts();
                arrayAdapter.notifyDataSetChanged();
            }
        });

        //------------------------------------------------------------------------------------------------------
        // GESTION DEL SPINNER
        comentarios = dbHandler.getProducts();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, comentarios);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                positionTituloSeleccionado = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

}