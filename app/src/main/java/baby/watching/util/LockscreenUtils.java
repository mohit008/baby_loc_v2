package baby.watching.util;

import android.app.Activity;

public class LockscreenUtils {
    private OnLockStatusChangedListener mLockStatusChangedListener;

    public interface OnLockStatusChangedListener
    {
        void onLockStatusChanged(boolean isLocked);
    }
    public void lock(Activity activity) {
        mLockStatusChangedListener = (OnLockStatusChangedListener) activity;
    }
    public void unlock() {
        if(mLockStatusChangedListener!=null)
        {
            mLockStatusChangedListener.onLockStatusChanged(false);
        }
    }
}
