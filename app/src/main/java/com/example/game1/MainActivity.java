package com.example.game1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {
    private long startTime = 15 * 60 * 1000; // 15 MINS IDLE TIME

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameSurface(this));

//Не смотрите пожалуйста я ещё не дорисовала все спрайты и кнопки, там вместо многих спрайтов мои старые иконки и рисунки с других проектов вы ничего не поймёте всё рано


    }
}