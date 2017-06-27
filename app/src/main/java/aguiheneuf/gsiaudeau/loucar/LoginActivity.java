package aguiheneuf.gsiaudeau.loucar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static String Ressource;
    private static Integer REQUEST_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

        public void OnClickConnect(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("id", "abcd");
        Ressource = "Connecté";
        // Inflate the menu; this adds items to the action bar if it is present.
        Toast.makeText(LoginActivity.this, Ressource, Toast.LENGTH_SHORT).show();
            startActivity(intent);
    }

}
