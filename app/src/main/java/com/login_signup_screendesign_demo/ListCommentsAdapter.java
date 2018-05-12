package com.login_signup_screendesign_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListCommentsAdapter extends ArrayAdapter {

    String[] commenteduser;
    String[] usercomment;
    Context comContext;
    TextView usercmd, cmd;

    public ListCommentsAdapter(@NonNull Context context, int resource, String[] username, String[] comment) {
        super(context, resource, username);
        this.commenteduser = username;
        this.usercomment = comment;
        this.comContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) comContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.list_comments, parent, false);
        usercmd = view.findViewById(R.id.username);
        cmd = view.findViewById(R.id.comm);
        if (usercmd == null && cmd == null) {
            Toast.makeText(comContext, "No Comments", Toast.LENGTH_SHORT).show();
        } else
            usercmd.setText(commenteduser[position]);
        cmd.setText(usercomment[position]);


        return view;
    }
}
