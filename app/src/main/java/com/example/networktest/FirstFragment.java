package com.example.networktest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.networktest.R.id.textview_network;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        Button networkBtn = view.findViewById(R.id.button_network);
        final TextView textView = (TextView) view.findViewById(textview_network);

        final  EditText email = (EditText) view.findViewById(R.id.email);
        final EditText password = (EditText) view.findViewById(R.id.password);
        networkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="https://acm-app-backend.herokuapp.com/api/v1/authenticate";

                final JSONObject params = new JSONObject();
                try {
                    final String emailStr = email.getText().toString();
                    final String passwordStr = password.getText().toString();
                    params.put("email", emailStr);
                    params.put("password", passwordStr);
                } catch (JSONException e) {
                    Log.e("LOGIN", "Parse params error");
                    e.printStackTrace();
                }
// Request a string response from the provided URL.
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(@NonNull JSONObject response) {
                                // Display the first 500 characters of the response string.
                                try {
                                    @NonNull final String msg = response.getString("success");
                                    Log.d("Token", response.getString("token"));
                                    if (msg == "true")
                                        NavHostFragment.findNavController(FirstFragment.this)
                                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                                    textView.setText(msg);
                                    Log.d("LOGIN RESPONSE", msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.e("LOGIN", "Could not parse reponse " + response);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                Volley.newRequestQueue(view.getContext()).add(jsonRequest);
            }
        });
    }
}