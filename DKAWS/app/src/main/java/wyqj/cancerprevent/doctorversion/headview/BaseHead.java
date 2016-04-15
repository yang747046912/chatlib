package wyqj.cancerprevent.doctorversion.headview;

import android.view.View;

/**
 * Created by 杨才 on 2016/2/3.
 */
public abstract class BaseHead<T> {
    abstract View getHeadView(int layoutID);

    abstract void setUpView(T object);
}
