package com.nitoelchidoceti.ciceroneguias.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nitoelchidoceti.ciceroneguias.R;

public class MessagesFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_messages,container,false);
        Toast.makeText(view.getContext(), "fragment Messages", Toast.LENGTH_SHORT).show();

        return view;
    }
}
