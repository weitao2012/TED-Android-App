package com.tedx.adapters;


import java.util.List;
import java.util.Map;


import com.catchnotes.tedapp.R;
import com.tedx.activities.GroupLazyActivity;
import com.tedx.helpers.Common;
import com.tedx.utility.RemoteImageView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class GroupLazyAdapter extends SimpleAdapter implements Filterable{
	public static final String IMAGE = "LazyAdapter_image";

	private boolean mDone = false;
	private boolean mFlinging = false;
	private GroupLazyActivity mActivity;

	public GroupLazyAdapter(GroupLazyActivity context, List<? extends Map<String, String>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mActivity = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// see if we need to load the next page
		if (!mDone && (getCount() - position) <= 1) {
			if (mActivity.isTaskFinished()) {
				mActivity.loadNextPage();
			}
		}

		View ret = super.getView(position, convertView, parent);
		if (ret != null) {
			RemoteImageView riv = (RemoteImageView) ret.findViewById(android.R.id.icon);
			if (riv != null && !mFlinging) {
				riv.loadImage();
			}
		}
		
		return ret;
	}

	public void setStopLoading(boolean done) {
		mDone = done;
	}

	public void setFlinging(boolean flinging) {
		mFlinging = flinging;
	}

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    
	@Override
	public void setViewImage(final ImageView image, final String value) {
		if (value != null && value.length() > 0 && image instanceof RemoteImageView) {
			RemoteImageView riv = (RemoteImageView) image;
			riv.setLocalURI(Common.getCacheFileName(value));
			riv.setRemoteURI(value);
			super.setViewImage(image, R.drawable.missingphoto);
		} else {
			image.setVisibility(View.GONE);
		}
	}
}