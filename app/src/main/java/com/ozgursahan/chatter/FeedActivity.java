package com.ozgursahan.chatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ozgursahan.chatter.adapter.MessageAdapter;
import com.ozgursahan.chatter.databinding.ActivityFeedBinding;
import com.ozgursahan.chatter.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityFeedBinding binding;

    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    //private StorageReference storageReference;

    MessageAdapter messageAdapter;

    ArrayList<Message> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFeedBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        messageArrayList = new ArrayList<>();

        auth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        //storageReference=firebaseStorage.getReference();


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)); //aşağıdan gösterim
        messageAdapter=new MessageAdapter(messageArrayList);
        binding.recyclerView.setAdapter(messageAdapter);

        getData();

    }

    private void getData() {
                                                                            // AZALAN TARİH DESCENDING (ARTAN ASCENDING)
        firebaseFirestore.collection("Messages").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null) {
                    Toast.makeText(FeedActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if(value!=null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();

                        String userEmail = (String) data.get("useremail");
                        String comment = (String) data.get("comment");

                        Message message = new Message(userEmail,comment);
                        messageArrayList.add(message);

                    }

                    messageAdapter.notifyDataSetChanged();

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            if (item.getItemId() == R.id.signout) {
            //Sign Out

            auth.signOut();

            Intent intentToMain =new Intent(FeedActivity.this,MainActivity.class);
            startActivity(intentToMain);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchButtonClicked (View view) {

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void sendButtonClicked (View view) {

        String comment = binding.commentText.getText().toString();

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

        HashMap<String,Object> messageData = new HashMap<>();
        messageData.put("useremail",email);
        messageData.put("comment",comment);
        messageData.put("date", FieldValue.serverTimestamp());

        firebaseFirestore.collection("Messages").add(messageData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FeedActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}