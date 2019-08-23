 package android.example.firebaseui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

 public class MainActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers;
     private static final int My_request_code=101;
     Button btn_sign;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_sign=findViewById(R.id.btn_sign_out);
        //init provider
        providers= Arrays.asList(
             new AuthUI.IdpConfig.EmailBuilder().build(), //Email builder
                new AuthUI.IdpConfig.PhoneBuilder().build(), //Phone builder
                new AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook builder
                new AuthUI.IdpConfig.GoogleBuilder().build() //Google builder
               // new AuthUI.IdpConfig.GitHubBuilder().build() //Github builder
        );
        showSignInoptions();
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               btn_sign.setEnabled(false);
                               showSignInoptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

     private void showSignInoptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(),My_request_code
        );
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(requestCode==My_request_code){
             IdpResponse response = IdpResponse.fromResultIntent(data);
             if(resultCode==RESULT_OK){
                 //Get User
                 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                 Toast.makeText(this,""+user.getEmail(),Toast.LENGTH_SHORT).show();

                 //set sign-out
                 btn_sign.setEnabled(true);
             }
             else{
                 Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
             }
         }
     }
 }
