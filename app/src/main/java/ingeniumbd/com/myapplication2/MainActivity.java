package ingeniumbd.com.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Blog> songlists=new ArrayList<Blog>();
    private DatabaseReference mDatabase;
    Button fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (Button) findViewById(R.id.fba);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("url", "https://firebasestorage.googleapis.com/v0/b/my-application2-af5d5.appspot.com/o/songs%2FChapter%201.mp3?alt=media&token=d6191eab-59f2-4883-9784-f186fcdf7a8c");
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
