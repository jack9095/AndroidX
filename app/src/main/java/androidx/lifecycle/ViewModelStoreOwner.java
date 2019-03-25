package androidx.lifecycle;

import androidx.annotation.NonNull;

/**
 * 此接口实现的职责是在配置更改期间保留所拥有的 ViewModelStore，并在要销毁此范围时调用 viewModelStore clear（）。
 * SDK27+版本，都是V4包下的 FragmentActivity 和 Fragment 实现了这二个接口
 */
@SuppressWarnings("WeakerAccess")
public interface ViewModelStoreOwner {

    @NonNull
    ViewModelStore getViewModelStore();
}
