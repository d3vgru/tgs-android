package org.globalsquare.activity;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import org.globalsquare.*;

public class FragmentA extends SherlockFragment {
Button button;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
{
return inflater.inflate(R.layout.frag_a, group, false);
}
@Override
public void onActivityCreated (Bundle savedInstanceState)
{
super.onActivityCreated(savedInstanceState);
button = (Button) getActivity().findViewById(R.id.button1);
button.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Toast.makeText(getActivity(), "You clicked button on Fragment A", Toast.LENGTH_LONG).show();
}
});
}
}