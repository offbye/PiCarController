package com.sanderson82.picarcontroller;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements ControllerFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        EditText ipAddressField;
        EditText portField;

        EditText camIPAddressField;
        EditText camPortField;

        TextView resultTextView;
        OnClickListener m_onClickListener=new OnClickListener() {
            @Override
            public void onClick(View p_v)
            {
                switch(p_v.getId())
                {
                    case R.id.connectButton:

                        String server = ipAddressField.getText().toString();
                        int port = Integer.parseInt(portField.getText().toString());

                        ClientController.INSTANCE.connect(server, port);

                        server = camIPAddressField.getText().toString();
                        port = Integer.parseInt(camPortField.getText().toString());

                        WebCamController.INSTANCE.setHostPort(server, port);

                        if (!ClientController.INSTANCE.isConnected()) {
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, new ControllerFragment())
                                    .commit();
                        } else {
                            resultTextView.setVisibility(View.VISIBLE);
                            resultTextView.setText("Connection failed");
                        }

                        break;
                }
            }
        };

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ipAddressField = (EditText) rootView.findViewById(R.id.ipAddressField);
            portField = (EditText) rootView.findViewById(R.id.portTextField);

            camIPAddressField = (EditText) rootView.findViewById(R.id.camIPAddressField);
            camPortField = (EditText) rootView.findViewById(R.id.camPortTextField);

            resultTextView = (TextView) rootView.findViewById(R.id.resultTextView);
            Button connectButton = (Button) rootView.findViewById(R.id.connectButton);
            connectButton.setOnClickListener(m_onClickListener);
            return rootView;
        }

    }
}
