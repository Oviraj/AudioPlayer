package ingeniumbd.com.myapplication2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FirebaseHelper {
    private DatabaseReference mDatabase;
    ArrayList<Blog> songlists=new ArrayList<>();

    public FirebaseHelper(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public ArrayList<Blog> retrieve()
    {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dp: dataSnapshot.getChildren()){

                    Blog songlist=dp.getValue(Blog.class);
                    songlists.add(songlist);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return songlists;
    }
}
