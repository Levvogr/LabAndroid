package com.example.voiceassistent;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceassistent.database.DBHelper;
import com.example.voiceassistent.message.Message;
import com.example.voiceassistent.message.MessageEntity;
import com.example.voiceassistent.message.MessageListAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    protected Button sendButton;
    protected EditText questionText;
    protected RecyclerView chatMessageList;
    protected TextToSpeech textToSpeech;
    protected MessageListAdapter messageListAdapter;
    protected SharedPreferences sPref;
    public static final String APP_PREFERENCES = "mysettings";
    private boolean isLight = true;
    private String THEME = "THEME";
    DBHelper dBHelper;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        isLight = sPref.getBoolean(THEME, true);
        if(!isLight){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        super.onCreate(savedInstanceState);
        Log.e("LOG","onCreate");
        setContentView(R.layout.activity_main);
        sendButton = findViewById(R.id.sendButton);
        questionText = findViewById(R.id.questionField);
        chatMessageList = findViewById(R.id.chatMessageList);
        messageListAdapter = new MessageListAdapter();
        chatMessageList.setLayoutManager(new LinearLayoutManager(this));
        chatMessageList.setAdapter(messageListAdapter);
        dBHelper = new DBHelper(this);
        database = dBHelper.getWritableDatabase();
        Cursor cursor = database.query(dBHelper.TABLE_MESSAGES,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int messageIndex = cursor.getColumnIndex(dBHelper.FIELD_MESSAGE);
            int dateIndex = cursor.getColumnIndex(dBHelper.FIELD_DATE);
            int sendIndex = cursor.getColumnIndex(dBHelper.FIELD_SEND);

            do{
                MessageEntity entity = new MessageEntity(cursor.getString(messageIndex),
                        cursor.getString(dateIndex), cursor.getInt(sendIndex));
                Message message = new Message(entity);
                messageListAdapter.messageList.add(message);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSend();
            }
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("ru"));
                }
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("chat",messageListAdapter.messageList);
        //outState.putString("chatWindow",chatWindow.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        messageListAdapter.messageList =
                (ArrayList<Message>) savedInstanceState.getSerializable("chat");
        //chatWindow.append(savedInstanceState.getString("chatWindow"));
    }

    protected void onSend() {
        if(questionText.getText().toString().toLowerCase().equals("очистить")){
            messageListAdapter.messageList.clear();
            messageListAdapter.notifyDataSetChanged();
            questionText.setText("");
            database.delete(dBHelper.TABLE_MESSAGES, null, null);
            return;
        }
        AI.getAnswer(questionText.getText().toString(), new Consumer<String>() {
            @Override
            public void accept(String answer) {
                //chatWindow.append("\n" + questionText.getText().toString());
                messageListAdapter.messageList.add(
                        new Message("\n" + questionText.getText().toString()+"\n", true));

                //chatWindow.append("\n" + answer);
                messageListAdapter.messageList.add(
                        new Message("\n" + answer+"\n", false));
                messageListAdapter.notifyDataSetChanged();
                chatMessageList.scrollToPosition(messageListAdapter.messageList.size()-1);
                questionText.setText("");
                textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.day_settings:
                isLight=true;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                //установка дневной темы
                break;
            case R.id.night_settings:
                //установка ночной темы
                isLight=false;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("LOG","onStop");
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(THEME, isLight);
        editor.apply();
        database.delete(dBHelper.TABLE_MESSAGES, null, null);
        for (int i = 0; i < messageListAdapter.messageList.size(); i++) {
            MessageEntity entity = new MessageEntity(messageListAdapter.messageList.get(i));
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.FIELD_MESSAGE, entity.text);
            contentValues.put(DBHelper.FIELD_SEND, entity.isSend);
            contentValues.put(DBHelper.FIELD_DATE, entity.date);
            database.insert(dBHelper.TABLE_MESSAGES,null,contentValues);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}