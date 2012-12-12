package org.globalsquare;

import android.os.Bundle;

import org.globalsquare.framework.MessageListFragment;

public class OverviewListFragment extends MessageListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setEmptyText(getActivity().getString(R.string.noSquaresMsg));
		setListShown(true);
	}

}
