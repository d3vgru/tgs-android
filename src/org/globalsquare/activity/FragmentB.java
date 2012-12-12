package org.globalsquare.activity;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import org.globalsquare.*;

public class FragmentB extends SherlockFragment {
Button button;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
{
return inflater.inflate(R.layout.frag_b, group, false);
}
@Override
public void onActivityCreated (Bundle savedInstanceState)
{
super.onActivityCreated(savedInstanceState);
button = (Button) getActivity().findViewById(R.id.button2);
button.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
builder.setTitle("Fragment B");
builder.setMessage("What would you like to do?");
builder.setPositiveButton("Nothing", null);
builder.setNegativeButton("Leave me alone!", null);
builder.show();
}
});
}
}