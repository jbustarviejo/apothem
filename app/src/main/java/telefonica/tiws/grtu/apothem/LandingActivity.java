package telefonica.tiws.grtu.apothem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisActivity=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button button = (Button) findViewById(R.id.goButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                thisActivity.finish();
            }
        });
    }
}
