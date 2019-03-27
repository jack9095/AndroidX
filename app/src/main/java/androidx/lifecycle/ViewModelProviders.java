package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider.Factory;

/**
 * ViewModel 的创建不可直接 new，需要使用这个 ViewModelProviders 才能与 Activity 或者 Fragment 的生命周期关联起来！
 * ViewModel 的存在是依赖 Activity 或者 Fragment的，不管你在什么地方获取 ViewModel，
 * 只要你用的是相同的 Activity 或者 Fragment，那么获取到的 ViewModel 将是同一个 (前提是key值是一样的)，所以 ViewModel 也具有数据共享的作用！
 */
public class ViewModelProviders {

    /**
     * @deprecated This class should not be directly instantiated  不应直接实例化此类
     */
    @Deprecated
    public ViewModelProviders() {
    }

    /**
     * 通过Activity获取可用的Application或者检测Activity是否可用
     */
    private static Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        return application;
    }

    /**
     * 通过Fragment获取Activity或者检测Fragment是否可用
     */
    private static Activity checkActivity(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
        }
        return activity;
    }

    /**
     * 通过Fragment获得ViewModelProvider
     * ViewModelProvider.AndroidViewModelFactory 来实例化新的 ViewModels
     */
    @NonNull
    @MainThread
    public static ViewModelProvider of(@NonNull Fragment fragment) {
        return of(fragment, null);
    }

    /**
     * 使用 ViewModelProvider.AndroidViewModelFactory 来实例化新的 ViewModels.
     */
    @NonNull
    @MainThread
    public static ViewModelProvider of(@NonNull FragmentActivity activity) {
        return of(activity, null);
    }

    /**
     * 通过给定的工厂来实例化一个新的 ViewModels.
     */
    @NonNull
    @MainThread
    public static ViewModelProvider of(@NonNull Fragment fragment, @Nullable Factory factory) {
        Application application = checkApplication(checkActivity(fragment));
        if (factory == null) {
            // 获取默认的单例 AndroidViewModelFactory，它内部是通过反射来创建具体的 ViewModel
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return new ViewModelProvider(fragment.getViewModelStore(), factory);
    }

    /**
     * 通过给定的工厂来实例化一个新的 ViewModels.
     */
    @NonNull
    @MainThread
    public static ViewModelProvider of(@NonNull FragmentActivity activity, @Nullable Factory factory) {
        Application application = checkApplication(activity);
        if (factory == null) {
            // 获取默认的单例 AndroidViewModelFactory，它内部是通过反射来创建具体的 ViewModel
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return new ViewModelProvider(activity.getViewModelStore(), factory);
    }

    /**
     * 工厂可创建 AndroidViewModel 和 ViewModel，具有空构造函数的.
     * <p>
     * 不推荐使用 ViewModelProvider.AndroidViewModelFactory
     */
    @SuppressWarnings("WeakerAccess")
    @Deprecated
    public static class DefaultFactory extends ViewModelProvider.AndroidViewModelFactory {
        /**
         * 不推荐使用 ViewModelProvider.AndroidViewModelFactory 和
         * ViewModelProvider.AndroidViewModelFactory.getInstance(Application)的方式创建 AndroidViewModelFactory
         */
        @Deprecated
        public DefaultFactory(@NonNull Application application) {
            super(application);
        }
    }
}
