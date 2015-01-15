package com.sanderson82.picarcontroller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
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

    public static final String MyPREFERENCES = "MyPrefs";
    public static String PREF_MOTOR_SERVER = "motor_server";
    public static String PREF_MOTOR_SERVER_PORT = "motor_server_port";
    public static String PREF_CAM_SERVER = "cam_server";
    public static String PREF_CAM_SERVER_PORT = "cam_server_port";
    public static String PREF_CAM_WIDTH = "cam_width";
    public static String PREF_CAM_HEIGHT = "cam_height";
    static SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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

        EditText camWidthField;
        EditText camHeightField;

        TextView resultTextView;

        public PlaceholderFragment() {
        }

        OnClickListener m_onClickListener = new OnClickListener() {
            @Override
            public void onClick(View p_v)
            {
                switch(p_v.getId())
                {
                    case R.id.connectButton:

                        String server = ipAddressField.getText().toString();
                        int port = Integer.parseInt(portField.getText().toString());

                        ClientController.INSTANCE.connect(server, port);

                        String camServer = camIPAddressField.getText().toString();
                        int camPort = Integer.parseInt(camPortField.getText().toString());

                        int width = Integer.parseInt(camWidthField.getText().toString());
                        int height = Integer.parseInt(camHeightField.getText().toString());

                        WebCamController.INSTANCE.setHostPort(server, port);
                        WebCamController.INSTANCE.setImageSize(width, height);

                        SharedPreferences.Editor edit = sharedpreferences.edit();
                        edit.putString(PREF_MOTOR_SERVER, server);
                        edit.putInt(PREF_MOTOR_SERVER_PORT, port);
                        edit.putString(PREF_CAM_SERVER, camServer);
                        edit.putInt(PREF_CAM_SERVER_PORT, camPort);
                        edit.putInt(PREF_CAM_HEIGHT, height);
                        edit.putInt(PREF_CAM_WIDTH, width);
                        edit.commit();

                        if (!ClientController.INSTANCE.isConnected()) {

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.container, new ControllerFragment());
                            ft.addToBackStack(null);
                            ft.commit();
                        } else {
                            resultTextView.setVisibility(View.VISIBLE);
                            resultTextView.setText("Connection failed");
                        }

                        break;
                }
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ipAddressField = (EditText) rootView.findViewById(R.id.ipAddressField);
            ipAddressField.setText(sharedpreferences.getString(PREF_MOTOR_SERVER, "192.168.2.10"));
            portField = (EditText) rootView.findViewById(R.id.portTextField);
            portField.setText(sharedpreferences.getInt(PREF_MOTOR_SERVER_PORT, 5000));

            camIPAddressField = (EditText) rootView.findViewById(R.id.camIPAddressField);
            camIPAddressField.setText(sharedpreferences.getString(PREF_CAM_SERVER, "192.168.2.10"));
            camPortField = (EditText) rootView.findViewById(R.id.camPortTextField);
            camPortField.setText(sharedpreferences.getInt(PREF_CAM_SERVER_PORT, 4000));

            camWidthField = (EditText) rootView.findViewById(R.id.camWidthTextField);
            camWidthField.setText(sharedpreferences.getInt(PREF_CAM_WIDTH, 320));
            camHeightField = (EditText) rootView.findViewById(R.id.camHeightTextField);
            camHeightField.setText(sharedpreferences.getInt(PREF_CAM_HEIGHT, 240));
            resultTextView = (TextView) rootView.findViewById(R.id.resultTextView);
            Button connectButton = (Button) rootView.findViewById(R.id.connectButton);
            connectButton.setOnClickListener(m_onClickListener);
            return rootView;
        }



    }
}
