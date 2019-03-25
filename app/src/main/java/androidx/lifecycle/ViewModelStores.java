package androidx.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Factory methods for {@link ViewModelStore} class.
 */
@SuppressWarnings("WeakerAccess")
public class ViewModelStores {

    private ViewModelStores() {
    }

    /**
     * 如果你的 Activity 实现了 ViewModelStoreOwner 接口具备了提供 ViewModelStore 的功能就直接获取返回
     * TODO 为 Activity 创建 ViewModelStore
     */
    @Deprecated
    @NonNull
    @MainThread
    public static ViewModelStore of(@NonNull FragmentActivity activity) {
        return activity.getViewModelStore();
    }

    /**
     * 如果你的 Fragment 实现了 ViewModelStoreOwner 接口具备了提供 ViewModelStore 的功能就直接获取返回
     * TODO 为 Fragment 创建 ViewModelStore
     */
    @Deprecated
    @NonNull
    @MainThread
    public static ViewModelStore of(@NonNull Fragment fragment) {
        return fragment.getViewModelStore();
    }
}
