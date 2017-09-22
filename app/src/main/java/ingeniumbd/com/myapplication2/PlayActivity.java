package ingeniumbd.com.myapplication2;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton playprevious,plyabtn,playnext;
    private SeekBar seekBar;
    private MediaPlayer mp = new MediaPlayer();
    ArrayList<Blog> songlists=new ArrayList<Blog>();
    private DatabaseReference mDatabase;
    private ListView mBlogList;
    private ProgressBar progressBar;
    private Uri u;
    TextView currentDurationOfsong, durationOfsong, currentSong;


    String url;

    private int position;

    FirebaseHelper helper;
    CustomAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        position = bundle.getInt("position");

        playprevious=(ImageButton)findViewById(R.id.previous);
        plyabtn=(ImageButton)findViewById(R.id.play);
        playnext=(ImageButton)findViewById(R.id.next);
        seekBar=(SeekBar)findViewById(R.id.seekbar);
        currentDurationOfsong = (TextView) findViewById(R.id.currentdurationOfsong);
        durationOfsong = (TextView) findViewById(R.id.durationOfsong);
        currentSong = (TextView) findViewById(R.id.currentSong);
        currentSong.setText("অধ্যায় ১");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Songlist");
        retrieve();


        if (mp!=null){
            plyabtn.setImageResource(R.drawable.pause);
            mp.stop();
            mp.release();
        }
        u=Uri.parse(url.toString());
        mp=MediaPlayer.create(getApplicationContext(),u);

        playprevious.setOnClickListener(this);
        plyabtn.setOnClickListener(this);
        playnext.setOnClickListener(this);
        seekBar.setOnClickListener(this);



        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(position<songlists.size())
                {
                    mp.reset();
                    try {
                        position=(position+1)%songlists.size();
                        mp.setDataSource(songlists.get(position).getAudiofile().toString());
                        currentSong.setText(songlists.get(position).getTitle().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                }
                else
                {
                    mp.release();
                }
            }
        });
        mp.start();
        seekBar.setMax(mp.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

        Thread t = new runThread();
        t.start();

        mBlogList = (ListView) findViewById(R.id.listview);
        helper = new FirebaseHelper(mDatabase);
        adapter = new CustomAdapter(this, helper.retrieve());
        mBlogList.setAdapter(adapter);
        mBlogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position<songlists.size())
                {
                    mp.reset();
                    try {
                        position=(position)%songlists.size();
                        mp.setDataSource(songlists.get(position).getAudiofile().toString());
                        currentSong.setText(songlists.get(position).getTitle().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                }
                else
                {
                    mp.release();
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }


    public class runThread extends Thread {


        @Override
        public void run() {
            while (true) {

                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("Runwa", "run: " + 1);
                if (mp != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            long second = (mp.getDuration() / 1000) % 60;
                            long minute = (mp.getDuration() / (1000 * 60)) % 60;
                            long hour = (mp.getDuration() / (1000 * 60 * 60)) % 24;
                            String totalDuration = String.format("%02d:%02d:%02d", hour, minute, second);

                            long second1 = (mp.getCurrentPosition() / 1000) % 60;
                            long minute1 = (mp.getCurrentPosition() / (1000 * 60)) % 60;
                            long hour1 = (mp.getCurrentPosition() / (1000 * 60 * 60)) % 24;
                            String currentDuration = String.format("%02d:%02d:%02d", hour1, minute1, second1);

                            durationOfsong.setText(""+totalDuration);
                            currentDurationOfsong.setText(""+currentDuration);
                            seekBar.setProgress(mp.getCurrentPosition());
                        }
                    });

                     //Log.d("Runwa", "run: " + mp.getCurrentPosition());

                }
            }
        }

    }



    public ArrayList<Blog> retrieve(){


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dp:dataSnapshot.getChildren()){

                    Blog blog=dp.getValue(Blog.class);

                    songlists.add(blog);

                    for(int position=0;position<songlists.size();position++){
                        // Toast.makeText(PlayerActivity.this, ""+songlists.get(position), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return songlists;

    }



    @Override
    public void onClick(View view) {

        int id=view.getId();

        switch (id){

            case R.id.play:
                if (mp.isPlaying()){
                   plyabtn.setImageResource(R.drawable.play);
                    mp.pause();
                }
                else {
                   plyabtn.setImageResource(R.drawable.pause);
                    mp.start();
                }
                break;

            case R.id.next:
                //plyabtn.setImageResource(R.drawable.pausebtn);
                if(position<songlists.size())
                {
                    mp.reset();
                    try {
                        position=(position+1)%songlists.size();
                        mp.setDataSource(songlists.get(position).getAudiofile().toString());
                        currentSong.setText(songlists.get(position).getTitle().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                }
                else
                {
                    mp.release();
                }
                break;

            case R.id.previous:
               // plyabtn.setImageResource(R.drawable.pausebtn);
                if(position<songlists.size())
                {
                    mp.reset();
                    try {
                        position=(position-1<0)?songlists.size()-1:position-1;
                        mp.setDataSource(songlists.get(position).getAudiofile().toString());
                        currentSong.setText(songlists.get(position).getTitle().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                }
                else
                {
                    mp.release();
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "IngeniumBD");
            String sAux = "\nLet me recommend you our other mobile application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/developer?id=ingenium%20BD&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
            return true;

        }else if (id == R.id.exit) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Application?");
            alertDialogBuilder
                    .setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                    finish();
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
