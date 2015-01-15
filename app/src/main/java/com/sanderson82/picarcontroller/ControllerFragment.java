package com.sanderson82.picarcontroller;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.camera.simplemjpeg.MjpegView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ControllerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int ZONES = 11;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private OnTouchListener steeringListener = new OnTouchListener() {
        float lastTouchX = 0;
        float lastTouchY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {

            int height = v.getHeight();
            int width = v.getWidth();

            int midHeight = height / 2;
            int midWidth = width / 2;

            ImageView iv = (ImageView) v;

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    lastTouchX = motionEvent.getX();
                    lastTouchY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    lastTouchX = midHeight;
                    lastTouchY = midWidth;
                    break;
            }


            System.out.println("height=" + height + " width=" + width);
            System.out.println("x=" + lastTouchX + " y=" + lastTouchY);
            handleControllerMessage((int) lastTouchX, (int) lastTouchY, height, width);
            return false;
        }
    };

    public ControllerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControllerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControllerFragment newInstance(String param1, String param2) {
        ControllerFragment fragment = new ControllerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Gets the percent value from the value, assuming the middle is 0 and the outsides are 100
     *
     * @param value
     * @param width
     * @return Returns the percentage from the middle
     */
    public static int getPercent(int value, int width) {
        int midPoint = width / 2;
        int distanceFromMid = Math.abs(midPoint - value);
        double percentage = 100 * ((double) distanceFromMid) / midPoint;
        System.out.println("percentage = " + (int) percentage);

        return (int) percentage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_controller, container, false);

        ImageView steeringImage = (ImageView) rootView.findViewById(R.id.steeringImageView);
        steeringImage.setOnTouchListener(steeringListener);

        MjpegView mv = (MjpegView) rootView.findViewById(R.id.mv);
        WebCamController.INSTANCE.connect(mv);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Sends the messages through the controller based on the input from the controller
     *
     * @param x_loc
     * @param y_loc
     * @param height
     * @param width
     */
    private void handleControllerMessage(int x_loc, int y_loc, int height, int width) {
        int nsPercent = getPercent(y_loc, height);
        int ewPercent = getPercent(x_loc, width);

        if (y_loc > height / 2)
            ClientController.INSTANCE.sendCommand(ClientCommand.MOVE_FORWARD.getValue(), nsPercent);
        else
            ClientController.INSTANCE.sendCommand(ClientCommand.MOVE_REVERSE.getValue(), nsPercent);

        if (x_loc > width / 2)
            ClientController.INSTANCE.sendCommand(ClientCommand.STEER_RIGHT.getValue(), ewPercent);
        else
            ClientController.INSTANCE.sendCommand(ClientCommand.STEER_LEFT.getValue(), ewPercent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onResume() {
        super.onResume();
        WebCamController.INSTANCE.resumePlayback();
    }

    public void onPause() {
        super.onPause();
        WebCamController.INSTANCE.stopPlayback();
    }

    public void onDestroy() {
        WebCamController.INSTANCE.freeMemory();
        super.onDestroy();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
